package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
		Map<Storage<ItemVariant>, Vec3> inputHandlers = new HashMap<>();
		Map<Storage<ItemVariant>, Vec3> outputHandlers = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.COMMON_CONFIG.MAGIC_TREES.sortingRange.get())) {
			if (!blockPos.equals(pos)) {
				Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, null);
				if (storage != null) {
					boolean inputRange = Math.abs(blockPos.getX() - pos.getX()) <= 2
							&& Math.abs(blockPos.getY() - pos.getY()) <= 2
							&& Math.abs(blockPos.getZ() - pos.getZ()) <= 2;
					if (inputRange && storage.supportsExtraction()) {
						inputHandlers.put(storage, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					} else if (!inputRange && storage.supportsInsertion()) {
						outputHandlers.put(storage, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				}
			}
		}

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			Storage<ItemVariant> storage = getEntityStorage(entity);
			if (storage != null && storage.supportsExtraction())
				inputHandlers.put(storage, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
		});

		if (inputHandlers.isEmpty()) return;

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			Storage<ItemVariant> storage = getEntityStorage(entity);
			if (storage != null && storage.supportsInsertion() && !inputHandlers.containsKey(storage))
				outputHandlers.put(storage, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
		});

		if (outputHandlers.isEmpty()) return;

		for (Storage<ItemVariant> inputStorage : inputHandlers.keySet()) {
			for (StorageView<ItemVariant> view : inputStorage.nonEmptyViews()) {
				ItemVariant inputVariant = view.getResource();
				boolean transferred = false;

				Map<Long, Storage<ItemVariant>> outputsByCount = new Long2ObjectOpenHashMap<>();

				for (Storage<ItemVariant> outputStorage : outputHandlers.keySet()) {
					long stored = StorageUtil.simulateExtract(outputStorage, inputVariant, Long.MAX_VALUE, null);
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

						Vec3 xyz = outputHandlers.get(outputStorage);
						Vec3 diff = inputHandlers.get(inputStorage).subtract(xyz);

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

				if (transferred) break;
			}
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
