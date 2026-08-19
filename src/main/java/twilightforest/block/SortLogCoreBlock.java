package twilightforest.block;

import twilightforest.fabric.network.PacketDistributor;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.util.WorldUtil;

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

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.sortingCoreRange)) {
			if (blockPos.equals(pos)) {
				continue;
			}

			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity == null) {
				continue;
			}

			Vec3 storagePos = Vec3.upFromBottomCenterOf(blockPos, 1.9D);

			if (Math.abs(blockPos.getX() - pos.getX()) <= 2
				&& Math.abs(blockPos.getY() - pos.getY()) <= 2
				&& Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {

				List<Storage<ItemVariant>> storages = new ArrayList<>(6);

				for (Direction side : Direction.values()) {
					Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, blockPos, side);
					if (storage != null) {
						storages.add(storage);
					}
				}

				if (!storages.isEmpty()) {
					inputMap.put(storages, storagePos);
				}
			} else {
				for (Direction side : Direction.values()) {
					Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, blockPos, side);
					if (storage != null) {
						outputMap.put(storage, storagePos);
					}
				}
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>();

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.is(TFEntityTypeTags.SORTABLE_ENTITIES)).forEach(entity -> {
			Storage<ItemVariant> storage = getEntityStorage(entity);

			if (storage != null) {
				inputMap.put(List.of(storage), entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				alreadyUsedForInput.add(entity);
			}
		});

		if (inputMap.isEmpty()) {
			return;
		}

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity) && entity.is(TFEntityTypeTags.SORTABLE_ENTITIES)).forEach(entity -> {
			Storage<ItemVariant> storage = getEntityStorage(entity);

			if (storage != null) {
				outputMap.put(storage, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (outputMap.isEmpty()) {
			return;
		}

		for (Map.Entry<List<Storage<ItemVariant>>, Vec3> inputEntry : inputMap.entrySet()) {
			boolean transferred = false;

			for (Storage<ItemVariant> inputStorage : inputEntry.getKey()) {
				for (Iterator<StorageView<ItemVariant>> it = inputStorage.nonEmptyIterator(); it.hasNext(); ) {
					StorageView<ItemVariant> inputView = it.next();
					ItemVariant itemVariant = inputView.getResource();

					if (itemVariant.isBlank()) {
						continue;
					}

					try (Transaction tx = Transaction.openOuter()) {
						if (inputView.extract(itemVariant, 1, tx) != 1) {
							continue;
						}

						Map<Storage<ItemVariant>, Long> outputCounts = new HashMap<>();

						for (Storage<ItemVariant> outputStorage : outputMap.keySet()) {
							long count = 0;

							for (Iterator<StorageView<ItemVariant>> iter = outputStorage.nonEmptyIterator(); iter.hasNext(); ) {
								StorageView<ItemVariant> outputView = iter.next();
								if (itemVariant.equals(outputView.getResource())) {
									count += outputView.getAmount();
								}
							}

							if (count > 0) {
								outputCounts.put(outputStorage, count);
							}
						}

						List<Storage<ItemVariant>> sortedOutputs = outputCounts.entrySet()
							.stream()
							.sorted(Map.Entry.<Storage<ItemVariant>, Long>comparingByValue().reversed())
							.map(Map.Entry::getKey)
							.toList();

						for (Storage<ItemVariant> outputStorage : sortedOutputs) {
							if (outputStorage.insert(itemVariant, 1, tx) == 1) {
								tx.commit();
								transferred = true;

								Vec3 xyz = outputMap.get(outputStorage);
								Vec3 diff = inputEntry.getValue().subtract(xyz);

								ParticlePacket particlePacket = new ParticlePacket();

								double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
								double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
								double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;

								particlePacket.queueParticle(
									TFParticleType.SORTING_PARTICLE,
									false,
									false,
									xyz,
									new Vec3(x, y, z).scale(1D / diff.length())
								);

								PacketDistributor.sendToPlayersNear(
									level,
									null,
									xyz.x(),
									xyz.y(),
									xyz.z(),
									64.0D,
									particlePacket
								);

								break;
							}
						}
					}

					if (transferred) {
						break;
					}
				}

				if (transferred) {
					break;
				}
			}
		}
	}

	private static @Nullable Storage<ItemVariant> getEntityStorage(Entity entity) {
		if (entity instanceof Container container) {
			return ContainerStorage.of(container, null);
		}

		return null;
	}
}