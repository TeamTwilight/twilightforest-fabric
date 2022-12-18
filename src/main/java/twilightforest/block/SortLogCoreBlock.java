package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;

import java.util.Comparator;
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
				Storage<ItemVariant> storage = TransferUtil.getItemStorage(level, blockPos);

				if (storage != null) {
					boolean inInputRange = Math.abs(blockPos.getX() - pos.getX()) <= 2
							&& Math.abs(blockPos.getY() - pos.getY()) <= 2
							&& Math.abs(blockPos.getZ() - pos.getZ()) <= 2;
					if (inInputRange && storage.supportsExtraction()) {
						inputHandlers.put(storage, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					} else if (!inInputRange && storage.supportsInsertion()) {
						outputHandlers.put(storage, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				}
			}
		}

		// fabric: transfer API entity support when
		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			if (entity instanceof Player player)
				inputHandlers.put(PlayerInventoryStorage.of(player), entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			else if (entity instanceof ContainerEntity container) {
				inputHandlers.put(InventoryStorage.of(container, null), entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (inputHandlers.isEmpty()) return;

		level.getEntities((Entity)null, new AABB(pos).inflate(16), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			if (entity instanceof Player player) {
				Storage<ItemVariant> playerInv = PlayerInventoryStorage.of(player);
				if (!inputHandlers.containsKey(playerInv))
					outputHandlers.put(playerInv, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			} else if (entity instanceof ContainerEntity container) {
				InventoryStorage storage = InventoryStorage.of(container, null);
				if (!inputHandlers.containsKey(storage))
					outputHandlers.put(storage, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (outputHandlers.isEmpty()) return;

		for (Storage<ItemVariant> inputIItemHandler : inputHandlers.keySet()) {
			try (Transaction t = TransferUtil.getTransaction()) {
				for (StorageView<ItemVariant> slot : inputIItemHandler) {
					if (slot.isResourceBlank())
						continue;
					ItemVariant contents = slot.getResource();
					boolean transferred = false;

					Map<Long, Storage<ItemVariant>> outputsByCount = new HashMap<>();

					for (Storage<ItemVariant> outputIItemHandler : outputHandlers.keySet()) {
						long count = outputIItemHandler.simulateExtract(contents, Long.MAX_VALUE, t);
						if (count > 0) outputsByCount.put(count, outputIItemHandler);
					}

					for (Long count : outputsByCount.keySet().stream().sorted(Comparator.comparingLong(Long::longValue).reversed()).toList()) {
						Storage<ItemVariant> outputIItemHandler = outputsByCount.get(count);

						try (Transaction nested = t.openNested()) {
							long extracted = slot.extract(contents, 1, nested);
							if (extracted != 1)
								break; // nothing to extract, this shouldn't happen really
							long inserted = outputIItemHandler.insert(contents, 1, nested);
							if (inserted != 1)
								continue; // didn't insert the right amount, skip it

							// everything is good
							transferred = true;
							nested.commit();

							Vec3 xyz = outputHandlers.get(outputIItemHandler);
							Vec3 diff = inputHandlers.get(inputIItemHandler).subtract(xyz);

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
				t.commit();
			}
		}
	}
}
