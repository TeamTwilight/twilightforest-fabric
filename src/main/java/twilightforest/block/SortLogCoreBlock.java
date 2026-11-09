package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.util.BlockCapabilityDirectionalCache;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	private static final MapCodec<SpecialMagicLogBlock> CODEC = simpleCodec(SortLogCoreBlock::new);

	public static void init() {
		ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, level) -> {
			BlockCapabilityDirectionalCache<Storage<ItemVariant>> cache = level.getAttached(TFDataAttachments.SORTING_CORE_CAPABILITY_CACHE);
			if (cache != null)
				cache.invalidate(blockEntity.getBlockPos());
		});
	}

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableSortingCore;
	}

	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		Map<List<Storage<ItemVariant>>, Vec3> inputMap = new HashMap<>();
		Map<Storage<ItemVariant>, Vec3> outputMap = new HashMap<>();

		BlockCapabilityDirectionalCache<Storage<ItemVariant>> capabilityCache = level.getAttachedOrCreate(TFDataAttachments.SORTING_CORE_CAPABILITY_CACHE);

		for (BlockEntity blockEntity : WorldUtil.getLoadedBlockEntitiesInRange(level, pos, TFConfig.sortingCoreRange)) {
			BlockPos blockPos = blockEntity.getBlockPos();
			if (!blockPos.equals(pos)) {
				List<Storage<ItemVariant>> handlers = capabilityCache.getAllSides(level, blockPos);
				// Put it in the input if its within 2 blocks
				if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
					if (!handlers.isEmpty()) {
						inputMap.put(handlers, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				} else { // Output if its outside that range
					for (Storage<ItemVariant> handler : handlers) {
						outputMap.put(handler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				}
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>(); // Keep track of entities we already have for inputs, so we can skip over them when looking for outputs

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.is(TFEntityTypeTags.SORTABLE_ENTITIES)).forEach(entity -> {
			List<Storage<ItemVariant>> handlers = new ArrayList<>();
			for (Direction side : Direction.values()) {
				Storage<ItemVariant> handler = getEntityStorage(entity, side);
				if (handler != null) handlers.add(handler);
			}
			if (!handlers.isEmpty()) {
				inputMap.put(handlers, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				alreadyUsedForInput.add(entity);
			}
		});

		if (inputMap.isEmpty()) return; // No input

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity) && entity.is(TFEntityTypeTags.SORTABLE_ENTITIES)).forEach(entity -> {
			for (Direction side : Direction.values()) {
				Storage<ItemVariant> handler = getEntityStorage(entity, side);
				if (handler != null) outputMap.put(handler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (outputMap.isEmpty()) return; // No output

		for (Map.Entry<List<Storage<ItemVariant>>, Vec3> inputHandlers : inputMap.entrySet()) {
			boolean transferred = false;
			for (Storage<ItemVariant> inputIItemHandler : inputHandlers.getKey()) {
				for (StorageView<ItemVariant> inputView : inputIItemHandler) {
					ItemVariant itemResource = inputView.getResource();
					if (itemResource.isBlank()) continue;
					try (Transaction tx = Transaction.openOuter()) {
						if (inputView.extract(itemResource, 1, tx) == 0) continue;
						Map<Storage<ItemVariant>, Integer> outputCounts = new HashMap<>();

						for (Storage<ItemVariant> outputHandler : outputMap.keySet()) {
							int count = 0;
							for (StorageView<ItemVariant> outputView : outputHandler) {
								if (itemResource.equals(outputView.getResource())) count += (int) outputView.getAmount();
							}
							if (count > 0) outputCounts.put(outputHandler, count);
						}

						List<Storage<ItemVariant>> sortedOutputs = outputCounts.entrySet().stream()
							.sorted(Map.Entry.<Storage<ItemVariant>, Integer>comparingByValue().reversed())
							.map(Map.Entry::getKey)
							.toList();

						for (Storage<ItemVariant> outputHandler : sortedOutputs) {
							if (outputHandler.insert(itemResource, 1, tx) == 1) {
								tx.commit();
								transferred = true;

								Vec3 xyz = outputMap.get(outputHandler);
								Vec3 diff = inputHandlers.getValue().subtract(xyz);

								ParticlePacket particlePacket = new ParticlePacket();
								double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
								double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
								double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
								particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE, false, false, xyz, new Vec3(x, y, z).scale(1D / diff.length()));

								for (ServerPlayer player : PlayerLookup.around(level, new Vec3i((int) xyz.x(), (int) xyz.y(), (int) xyz.z()), 64.0D)) {
									ServerPlayNetworking.send(player, particlePacket);
								}
								break;
							}
						}
					}
					if (transferred) break;// If we transferred the item from this Entry already, we break, since all IItemHandlers in one entry come from the same source
				}
				if (transferred) break; // Again, since we only transfer once per source, break
			}
		}
	}

	private static @Nullable Storage<ItemVariant> getEntityStorage(Entity entity, @Nullable Direction direction) {
		return entity instanceof Container container ? ContainerStorage.of(container, direction) : null;
	}
}