package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.Vec3;
import twilightforest.client.particle.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;

import java.util.*;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	public SortLogCoreBlock(Properties props) {
		super(props);
	}

	@Override
	@SuppressWarnings("ConstantConditions") //Pretty sure this error is a non-issue
	void performTreeEffect(Level world, BlockPos pos, Random rand) {
		Map<Container, BlockPos> inputChests = new HashMap<>();
		Map<Container, BlockPos> outputChests = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, 16)) {
			if (!blockPos.equals(pos) && world.getBlockState(blockPos).getBlock() instanceof ChestBlock chestBlock) {
				Container chest = ChestBlock.getContainer(chestBlock, world.getBlockState(blockPos), world, blockPos, true);
				BlockPos newPos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()); //It has to be a new blockPos, won't work properly otherwise

				if (newPos.closerThan(pos, 2)) inputChests.put(chest, newPos);
				else outputChests.put(chest, newPos);
			}
		}

		if (outputChests.isEmpty()) return; //Just so we don't iterate through input chests for no reason if there's nothing to sort into
		for (Container inputChest : inputChests.keySet()) {
			for (int i = 0; i < inputChest.getContainerSize(); i++) {
				ItemStack inputStack = inputChest.getItem(i);
				if (!inputStack.isEmpty()) {
					boolean transferred = false;

					List<Container> outputChestsSorted = outputChests.keySet().stream().filter(container -> {
						int count = container.countItem(inputStack.getItem());
						return count > 0 && count < container.getContainerSize() * inputStack.getMaxStackSize();
					}).sorted(Comparator.comparingInt(container -> Integer.MAX_VALUE - container.countItem(inputStack.getItem()))).toList(); //Sort outputs by how many copies of the item they have

					for (Container outputChest : outputChestsSorted) {
						int firstEmptyStack = -1;
						for (int j = 0; j < outputChest.getContainerSize(); j++) {
							ItemStack outputStack = outputChest.getItem(j);
							if (outputStack.isEmpty() && firstEmptyStack == -1) {
								firstEmptyStack = j; //We reference the index of the first empty slot, in case there is no stacks that aren't at max size
							} else if (ItemStack.isSameItemSameTags(inputStack, outputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
								outputStack.grow(1);
								inputStack.shrink(1);
								firstEmptyStack = -1;
								transferred = true;
								break;
							}
						}
						if (firstEmptyStack != -1) { //If there weren't any non-full stacks, we transfer to an empty space instead
							ItemStack newStack = inputStack.copy();
							newStack.setCount(1);
							outputChest.setItem(firstEmptyStack, newStack);
							inputStack.shrink(1);
							transferred = true;
						}
						if (transferred) { //This is just particle math, we send a particle packet to every player in range
							Vec3 xyz = Vec3.upFromBottomCenterOf(outputChests.get(outputChest), 1.9D);
							Vec3 diff = Vec3.upFromBottomCenterOf(inputChests.get(inputChest), 1.9D).subtract(xyz);

							for (ServerPlayer serverplayer : ((ServerLevel)world).players()) {
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
	}
}
