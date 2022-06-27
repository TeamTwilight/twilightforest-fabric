package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
	void performTreeEffect(Level level, BlockPos pos, RandomSource rand) {
		Map<Storage<ItemVariant>, Vec3> inputHandlers = new HashMap<>();
		Map<Storage<ItemVariant>, Vec3> outputHandlers = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, 16)) {
			if (!blockPos.equals(pos)) {
				Storage<ItemVariant> storage = TransferUtil.getItemStorage(level, blockPos);

				if (storage != null) {
					if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
						inputHandlers.put(storage, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					} else outputHandlers.put(storage, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
				}
			}
		}

		// TODO: PORT other entities
		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			if (entity instanceof Player player)
				inputHandlers.put(PlayerInventoryStorage.of(player), entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
		});

		if (inputHandlers.isEmpty()) return;

		level.getEntities((Entity)null, new AABB(pos).inflate(16), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			if (entity instanceof Player player) {
				Storage<ItemVariant> playerInv = PlayerInventoryStorage.of(player);
				if (!inputHandlers.containsKey(playerInv))
					outputHandlers.put(playerInv, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (outputHandlers.isEmpty()) return;

		for (Storage<ItemVariant> inputIItemHandler : inputHandlers.keySet()) {
			try (Transaction t = TransferUtil.getTransaction()) {
				for (StorageView<ItemVariant> slot : inputIItemHandler) {
					long input = TransferUtil.simulateExtractView(slot, slot.getResource(), 1);
					if (input != 0) {
						boolean transferred = false;

						Map<Integer, Storage<ItemVariant>> outputsByCount = new HashMap<>();

						for (Storage<ItemVariant> outputIItemHandler : outputHandlers.keySet()) {
							int count = 0;
							for (StorageView<ItemVariant> slotJ : inputIItemHandler) {
								if (slotJ.getResource().isOf(slot.getResource().getItem())) count += slotJ.getAmount();
							}
							if (count > 0) outputsByCount.put(count, outputIItemHandler);
						}

						for (Integer count : outputsByCount.keySet().stream().sorted(Comparator.comparingInt(Integer::intValue).reversed()).toList()) {
							Storage<ItemVariant> outputIItemHandler = outputsByCount.get(count);
							ItemVariant firstProperStack = ItemVariant.blank();
							for (StorageView<ItemVariant> slotJ : inputIItemHandler) {
								ItemStack outputStack = slotJ.getResource().toStack((int) slotJ.getAmount());

								if (firstProperStack.isBlank() && outputStack.isEmpty()) {
									firstProperStack = slotJ.getResource(); //We reference the index of the first empty slot, in case there is no stacks that aren't at max size
								} else if (ItemStack.isSameItemSameTags(slot.getResource().toStack((int) input), outputStack)
										&& outputStack.getCount() < outputStack.getMaxStackSize()
										/*&& outputStack.getCount() < outputIItemHandler.getSlotLimit(j)*/) {
									firstProperStack = slotJ.getResource();
									break;
								}
							}
							if (firstProperStack != null) { //If there weren't any non-full stacks, we transfer to an empty space instead
								long newStack = slot.extract(slot.getResource(), 1, t);
								if (newStack != 0 && outputIItemHandler.simulateInsert(firstProperStack, newStack, t) == 0) {//TODO Check
									outputIItemHandler.insert(firstProperStack, newStack, t);
									transferred = true;

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
						}
						if (transferred) break;
					}
				}
				t.commit();
			}
		}
	}
}
