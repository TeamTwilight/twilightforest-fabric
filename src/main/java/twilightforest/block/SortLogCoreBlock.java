package twilightforest.block;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.network.PacketDistributor;
import twilightforest.network.ParticlePacket;
import twilightforest.util.WorldUtil;

import io.github.fabricators_of_create.porting_lib.transfer.item.wrapper.IInventoryStorage;
import io.github.fabricators_of_create.porting_lib.transfer.item.wrapper.InventoryStorage;

import java.util.*;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableSortingCore;
	}

	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		Map<List<Storage<ItemVariant>>, Vec3> inputMap = new HashMap<>();
		Map<Storage<ItemVariant>, Vec3> outputMap = new HashMap<>();

		// Scan block entities in range
		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.sortingCoreRange)) {
			if (blockPos.equals(pos)) continue;

			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity == null) continue;

			boolean isInput = Math.abs(blockPos.getX() - pos.getX()) <= 2
				&& Math.abs(blockPos.getY() - pos.getY()) <= 2
				&& Math.abs(blockPos.getZ() - pos.getZ()) <= 2;

			List<Storage<ItemVariant>> handlers = new ArrayList<>();
			for (Direction side : Direction.values()) {
				Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, blockPos, side);
				if (storage != null && storage.supportsExtraction()) {
					handlers.add(storage);
				}
			}

			if (!handlers.isEmpty()) {
				if (isInput) {
					inputMap.put(handlers, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
				} else {
					for (Storage<ItemVariant> handler : handlers) {
						outputMap.put(handler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				}
			}
		}

		// Scan entities for input (within 2 blocks)
		List<Entity> alreadyUsedForInput = new ArrayList<>();
		level.getEntities((Entity) null, new AABB(pos).inflate(2),
			entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES))
			.forEach(entity -> {
				List<Storage<ItemVariant>> handlers = new ArrayList<>();
				getEntityStorages(entity, handlers);
				if (!handlers.isEmpty()) {
					inputMap.put(handlers, entity.position().add(0, entity.getBbHeight() + 0.9, 0));
					alreadyUsedForInput.add(entity);
				}
			});

		if (inputMap.isEmpty()) return;

		// Scan entities for output (within 16 blocks, not already used as input)
		level.getEntities((Entity) null, new AABB(pos).inflate(16),
			entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity)
				&& entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES))
			.forEach(entity -> {
				List<Storage<ItemVariant>> handlers = new ArrayList<>();
				getEntityStorages(entity, handlers);
				for (Storage<ItemVariant> handler : handlers) {
					outputMap.put(handler, entity.position().add(0, entity.getBbHeight() + 0.9, 0));
				}
			});

		if (outputMap.isEmpty()) return;

		// Perform the sorting
		for (Map.Entry<List<Storage<ItemVariant>>, Vec3> inputEntry : inputMap.entrySet()) {
			boolean transferred = false;
			for (Storage<ItemVariant> inputStorage : inputEntry.getKey()) {
				for (StorageView<ItemVariant> view : inputStorage) {
					if (view.isResourceBlank()) continue;

					ItemVariant inputVariant = view.getResource();

					// Find output storages that already contain this item, count how many
					Map<Long, Storage<ItemVariant>> outputsByCount = new HashMap<>();
					for (Storage<ItemVariant> outputStorage : outputMap.keySet()) {
						long count = 0;
						for (StorageView<ItemVariant> outputView : outputStorage) {
							if (outputView.getResource().equals(inputVariant)) {
								count += outputView.getAmount();
							}
						}
						if (count > 0) outputsByCount.put(count, outputStorage);
					}

					// Try outputs sorted by most matching items first
					for (Long count : outputsByCount.keySet().stream().sorted(Comparator.comparingLong(Long::longValue).reversed()).toList()) {
						Storage<ItemVariant> outputStorage = outputsByCount.get(count);

						try (Transaction tx = Transaction.openOuter()) {
							long extracted = inputStorage.extract(inputVariant, 1, tx);
							if (extracted == 0) continue;

							long inserted = outputStorage.insert(inputVariant, 1, tx);
							if (inserted == 1) {
								tx.commit();
								transferred = true;

								// Spawn particles
								Vec3 xyz = outputMap.get(outputStorage);
								Vec3 diff = inputEntry.getValue().subtract(xyz);
								ParticlePacket particlePacket = new ParticlePacket();
								double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
								double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
								double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
								particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE.get(), false, xyz, new Vec3(x, y, z).scale(1D / diff.length()));
								PacketDistributor.sendToPlayersNear(level, null, xyz.x(), xyz.y(), xyz.z(), 64.0D, particlePacket);
								break;
							}
						}
					}
					if (transferred) break;
				}
				if (transferred) break;
			}
		}
	}

	/**
	 * Get Fabric Transfer API storages from an entity (chest minecart, llama, donkey, etc.)
	 */
	private void getEntityStorages(Entity entity, List<Storage<ItemVariant>> handlers) {
		if (entity instanceof Container container) {
			// Wrap vanilla Container with Porting-Lib's InventoryStorage
			IInventoryStorage storage = InventoryStorage.of(container, null);
			handlers.add(storage);
		}
	}
}