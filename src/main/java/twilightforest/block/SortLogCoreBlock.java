package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.mixin.AbstractHorseAccessor;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;

import java.util.*;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.COMMON_CONFIG.MAGIC_TREES.disableSorting.get();
	}

	@Override
	void performTreeEffect(Level level, BlockPos pos, RandomSource rand) {
		Map<List<IItemHandler>, Vec3> inputMap = new HashMap<>();
		Map<IItemHandler, Vec3> outputMap = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.COMMON_CONFIG.MAGIC_TREES.sortingRange.get())) { // Get every itemHandler from every block in the area
			if (!blockPos.equals(pos)) {
				BlockEntity blockEntity = level.getBlockEntity(blockPos);
				if (blockEntity != null) {
					// Put it in the input if its within 2 blocks
					if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
						List<IItemHandler> handlers = new ArrayList<>();
						for (Direction side : Direction.values()) {
							blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).ifPresent(handlers::add);
						}
						if (!handlers.isEmpty()) {
							inputMap.put(handlers, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
						}
					} else { // Output if its outside that range
						for (Direction side : Direction.values()) {
							blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).ifPresent(iItemHandler -> outputMap.put(iItemHandler, Vec3.upFromBottomCenterOf(blockPos, 1.9D)));
						}
					}
				}
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>(); // Keep track of entities we already have for inputs, so we can skip over them when looking for outputs

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			List<IItemHandler> handlers = new ArrayList<>();
			for (Direction side : Direction.values()) {
				entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).ifPresent(handlers::add);
			}
			if (!handlers.isEmpty()) {
				inputMap.put(handlers, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				alreadyUsedForInput.add(entity);
			}
		});

		if (inputMap.isEmpty()) return; // No input

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity) && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			for (Direction side : Direction.values()) {
				entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).ifPresent(iItemHandler -> outputMap.put(iItemHandler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D)));
			}
		});

		if (outputMap.isEmpty()) return; // No output

		for (Map.Entry<List<IItemHandler>, Vec3> inputHandlers : inputMap.entrySet()) {
			boolean transferred = false;
			for (IItemHandler inputIItemHandler : inputHandlers.getKey()) {
				for (int i = 0; i < inputIItemHandler.getSlots(); i++) {
					ItemStack inputStack = inputIItemHandler.extractItem(i, 1, true);
					if (!inputStack.isEmpty()) {
						Map<Integer, IItemHandler> outputsByCount = new HashMap<>();

				for (Storage<ItemVariant> outputStorage : outputMap.keySet()) {
					long stored = outputStorage.simulateExtract(inputVariant, Long.MAX_VALUE, null);
					if (stored != Long.MAX_VALUE) // don't sort infinite inventories
							outputsByCount.put(stored, outputStorage);
				}

				for (Long count : outputsByCount.keySet().stream().sorted(Collections.reverseOrder(Long::compareTo)).toList()) {
					try (Transaction t = TransferUtil.getTransaction()) {
						Storage<ItemVariant> outputStorage = outputsByCount.get(count);
							if (view.extract(inputVariant, 1, t) != 1)
							break; // failed to extract, catastrophe
						if (outputStorage.insert(inputVariant, 1, t) != 1)
							continue; // failed to insert, skip and try next one

						// everything is good
							transferred = true;
							t.commit();

									Vec3 xyz = outputMap.get(outputIItemHandler);
									Vec3 diff = inputHandlers.getValue().subtract(xyz);

						for (ServerPlayer serverplayer : ((ServerLevel) level).players()) {//This is just particle math, we send a particle packet to every player in range
							if (serverplayer.distanceToSqr(xyz) < 4096.0D) {
								ParticlePacket particlePacket = new ParticlePacket();
								double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
								double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
								double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
								particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE.get(), false, xyz, new Vec3(x, y, z).scale(1D / diff.length()));
								TFPacketHandler.CHANNEL.sendToClient(particlePacket, serverplayer);
							}
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

	@Nullable
	private static Storage<ItemVariant> getEntityStorage(Entity entity) {
		if (entity instanceof ContainerEntity container) {
			return InventoryStorage.of(container, null);
		} else if (entity instanceof AbstractHorseAccessor horse) {
			return InventoryStorage.of(horse.getInventory(), null);
		}
		return null;
	}
}
