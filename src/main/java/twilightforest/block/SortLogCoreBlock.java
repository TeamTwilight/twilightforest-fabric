package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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
		Map<IItemHandler, Vec3> inputHandlers = new HashMap<>();
		Map<IItemHandler, Vec3> outputHandlers = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.COMMON_CONFIG.MAGIC_TREES.sortingRange.get())) {
			if (!blockPos.equals(pos)) {
				BlockEntity blockEntity = level.getBlockEntity(blockPos);

				if (blockEntity != null) {
					blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
						if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
							inputHandlers.put(iItemHandler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
						} else outputHandlers.put(iItemHandler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					});
				}
			}
		}

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity ->
				entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(iItemHandler ->
						inputHandlers.put(iItemHandler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D))));

		if (inputHandlers.isEmpty()) return;

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity ->
				entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(iItemHandler -> {
					if (!inputHandlers.containsKey(iItemHandler))
						outputHandlers.put(iItemHandler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				}));

		if (outputHandlers.isEmpty()) return;

		for (IItemHandler inputIItemHandler : inputHandlers.keySet()) {
			for (int i = 0; i < inputIItemHandler.getSlots(); i++) {
				ItemStack inputStack = inputIItemHandler.extractItem(i, 1, true);
				if (!inputStack.isEmpty()) {
					boolean transferred = false;

					Map<Integer, IItemHandler> outputsByCount = new HashMap<>();

					for (IItemHandler outputIItemHandler : outputHandlers.keySet()) {
						int count = 0;
						for (int j = 0; j < outputIItemHandler.getSlots(); j++) {
							ItemStack stack = outputIItemHandler.getStackInSlot(j);
							if (stack.is(inputStack.getItem())) count += stack.getCount();
						}
						if (count > 0) outputsByCount.put(count, outputIItemHandler);
					}

					for (Integer count : outputsByCount.keySet().stream().sorted(Comparator.comparingInt(Integer::intValue).reversed()).toList()) {
						IItemHandler outputIItemHandler = outputsByCount.get(count);
						int firstProperStack = -1;
						for (int j = 0; j < outputIItemHandler.getSlots(); j++) {
							if (outputIItemHandler.isItemValid(j, inputStack)) {
								ItemStack outputStack = outputIItemHandler.getStackInSlot(j);

								if (firstProperStack == -1 && outputStack.isEmpty()) {
									firstProperStack = j; //We reference the index of the first empty slot, in case there is no stacks that aren't at max size
								} else if (ItemStack.isSameItemSameTags(inputStack, outputStack)
										&& outputStack.getCount() < outputStack.getMaxStackSize()
										&& outputStack.getCount() < outputIItemHandler.getSlotLimit(j)) {
									firstProperStack = j;
									break;
								}
							}
						}
						if (firstProperStack != -1) { //If there weren't any non-full stacks, we transfer to an empty space instead
							ItemStack newStack = inputIItemHandler.extractItem(i, 1, false);
							if (!newStack.isEmpty() && outputIItemHandler.insertItem(firstProperStack, newStack, true).isEmpty()) {
								outputIItemHandler.insertItem(firstProperStack, newStack, false);
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
										TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverplayer), particlePacket);
									}
								}
								break;
							}
						}
					}
					if (transferred) break;
				}
			}
		}
	}
}
