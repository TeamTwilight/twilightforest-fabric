package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import twilightforest.TFSounds;
import twilightforest.enums.MagicWoodVariant;
import twilightforest.item.OreMagnetItem;
import twilightforest.network.ChangeBiomePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpecialMagicLogBlock extends RotatedPillarBlock {

	private final MagicWoodVariant magicWoodVariant;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	protected SpecialMagicLogBlock(BlockBehaviour.Properties props, MagicWoodVariant variant) {
		super(props.strength(2.0F).sound(SoundType.WOOD).lightLevel((state) -> 15));

		magicWoodVariant = variant;
		registerDefaultState(stateDefinition.any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		super.createBlockStateDefinition(container);
		container.add(ACTIVE);
	}

	//No longer an override, but keep here for sanity
	public int tickRate() {
		return 20;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		world.getBlockTicks().scheduleTick(pos, this, this.tickRate());
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (world.isClientSide || !state.getValue(ACTIVE)) return;

		switch (this.magicWoodVariant) {
			case TIME:
				world.playSound(null, pos, TFSounds.TIME_CORE, SoundSource.BLOCKS, 0.1F, 0.5F);
				doTreeOfTimeEffect(world, pos, rand);
				break;
			case TRANS:
				world.playSound(null, pos, TFSounds.TRANSFORMATION_CORE, SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 2F);
				doTreeOfTransformationEffect(world, pos, rand);
				break;
			case MINE:
				doMinersTreeEffect(world, pos, rand);
				break;
			case SORT:
				doSortingTreeEffect(world, pos, rand);
				break;
		}

		world.getBlockTicks().scheduleTick(pos, this, this.tickRate());
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!state.getValue(ACTIVE)) {
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			world.getBlockTicks().scheduleTick(pos, this, this.tickRate());
			return InteractionResult.SUCCESS;
		} else if (state.getValue(ACTIVE)) {
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	/**
	 * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
	 */
	private void doTreeOfTimeEffect(Level world, BlockPos pos, Random rand) {

		int numticks = 8 * 3 * this.tickRate();

		for (int i = 0; i < numticks; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16);

			BlockState state = world.getBlockState(dPos);

			if (state.isRandomlyTicking()) {
				state.randomTick((ServerLevel) world, dPos, rand);
			}

			//FIXME figure out how to get this working again
//			BlockEntity te = world.getBlockEntity(dPos);
//			if (te instanceof TickableBlockEntity && !te.isRemoved()) {
//				((TickableBlockEntity) te).tick();
//			}
		}
	}

	/**
	 * The tree of transformation transforms the biome in the area near it into the enchanted forest biome.
	 * TODO: also change entities
	 */
	private void doTreeOfTransformationEffect(Level world, BlockPos pos, Random rand) {
		final int WIDTH_BITS = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
		final int HEIGHT_BITS = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
		final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;
		final int VERTICAL_MASK = (1 << HEIGHT_BITS) - 1;
		Biome targetBiome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.ENCHANTED_FOREST);

		for (int i = 0; i < 16; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16, 0, 16);
			if (dPos.distSqr(pos) > 256.0)
				continue;

			Biome biomeAt = world.getBiome(dPos);
			if (biomeAt == targetBiome)
				continue;

			LevelChunk chunkAt = world.getChunk(dPos.getX() >> 4, dPos.getZ() >> 4);
			int x = (dPos.getX() >> 2) & HORIZONTAL_MASK;
			int z = (dPos.getZ() >> 2) & HORIZONTAL_MASK;
			if (chunkAt.getBiomes().biomes[z << WIDTH_BITS | x] == targetBiome)
				continue;
			for (int dy = 0; dy < 255; dy += 4) {
				int y = Mth.clamp(dy >> 2, 0, VERTICAL_MASK);
				chunkAt.getBiomes().biomes[y << WIDTH_BITS + WIDTH_BITS | z << WIDTH_BITS | x] = targetBiome;
			}

			if (world instanceof ServerLevel) {
				sendChangedBiome(chunkAt, dPos, targetBiome);
			}
			break;
		}
	}

	/**
	 * Send a tiny update packet to the client to inform it of the changed biome
	 */
	private void sendChangedBiome(LevelChunk chunk, BlockPos pos, Biome biome) {
		ChangeBiomePacket message = new ChangeBiomePacket(pos, BuiltinRegistries.BIOME.getKey(biome));
		TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
	}

	/**
	 * The miner's tree generates the ore magnet effect randomly every second
	 */
	private void doMinersTreeEffect(Level world, BlockPos pos, Random rand) {
		BlockPos dPos = WorldUtil.randomOffset(rand, pos, 32);
		int moved = OreMagnetItem.doMagnet(world, pos, dPos);

		if (moved > 0) {
			world.playSound(null, pos, TFSounds.MAGNET_GRAB, SoundSource.BLOCKS, 0.1F, 1.0F);
		}
	}

	/**
	 * The sorting tree finds two chests nearby and then attempts to sort a random item.
	 */
	private void doSortingTreeEffect(Level world, BlockPos pos, Random rand) {

		// find all the chests nearby
		List<Container> chests = new ArrayList<>();
		int itemCount = 0;

		for (BlockPos iterPos : WorldUtil.getAllAround(pos, 16)) {

			Container chestInventory = null, teInventory = null;

			Block block = world.getBlockState(iterPos).getBlock();
			if (block instanceof ChestBlock) {
				chestInventory = ChestBlock.getContainer((ChestBlock) block, block.defaultBlockState(), world, iterPos, true);
			}

			BlockEntity te = world.getBlockEntity(iterPos);
			if (te instanceof Container && !te.isRemoved()) {
				teInventory = (Container) te;
			}

			// make sure we haven't counted this chest
			if (chestInventory != null && teInventory != null && !checkIfChestsContains(chests, teInventory)) {

				boolean empty = true;
				// count items
				for (int i = 0; i < chestInventory.getContainerSize(); i++) {
					if (!chestInventory.getItem(i).isEmpty()) {
						empty = false;
						itemCount++;
					}
				}

				// only add non-empty chests
				if (!empty) {
					chests.add(chestInventory);
				}
			}
		}

		// find a random item in one of the chests
		ItemStack beingSorted = ItemStack.EMPTY;
		int sortedChestNum = -1;
		int sortedSlotNum = -1;

		if (itemCount == 0) return;

		int itemNumber = rand.nextInt(itemCount);
		int currentNumber = 0;

		for (int i = 0; i < chests.size(); i++) {
			Container chest = chests.get(i);
			for (int slotNum = 0; slotNum < chest.getContainerSize(); slotNum++) {
				ItemStack currentItem = chest.getItem(slotNum);

				if (!currentItem.isEmpty()) {
					if (currentNumber++ == itemNumber) {
						beingSorted = currentItem;
						sortedChestNum = i;
						sortedSlotNum = slotNum;
					}
				}
			}
		}

		if (beingSorted.isEmpty()) return;

		int matchChestNum = -1;
		int matchCount = 0;

		// decide where to put it, if anywhere
		for (int chestNum = 0; chestNum < chests.size(); chestNum++) {
			Container chest = chests.get(chestNum);
			int currentChestMatches = 0;

			for (int slotNum = 0; slotNum < chest.getContainerSize(); slotNum++) {

				ItemStack currentItem = chest.getItem(slotNum);
				if (!currentItem.isEmpty() && isSortingMatch(beingSorted, currentItem)) {
					currentChestMatches += currentItem.getCount();
				}
			}

			if (currentChestMatches > matchCount) {
				matchCount = currentChestMatches;
				matchChestNum = chestNum;
			}
		}

		// soooo, did we find a better match?
		if (matchChestNum >= 0 && matchChestNum != sortedChestNum) {
			Container moveChest = chests.get(matchChestNum);
			Container oldChest = chests.get(sortedChestNum);

			// is there an empty inventory slot in the new chest?
			int moveSlot = getEmptySlotIn(moveChest);

			if (moveSlot >= 0) {
				// remove old item
				oldChest.setItem(sortedSlotNum, ItemStack.EMPTY);

				// add new item
				moveChest.setItem(moveSlot, beingSorted);
			}
		}

		// if the stack is not full, combine items from other stacks
		if (beingSorted.getCount() < beingSorted.getMaxStackSize()) {
			for (Container chest : chests) {
				for (int slotNum = 0; slotNum < chest.getContainerSize(); slotNum++) {
					ItemStack currentItem = chest.getItem(slotNum);

					if (!currentItem.isEmpty() && currentItem != beingSorted && beingSorted.sameItem(currentItem)) {
						if (currentItem.getTag() != null && beingSorted.getTag() != null) {
							if (beingSorted.getTag().equals(currentItem.getTag())) {
								if (currentItem.getCount() <= (beingSorted.getMaxStackSize() - beingSorted.getCount())) {
									chest.setItem(slotNum, ItemStack.EMPTY);
									beingSorted.grow(currentItem.getCount());
									currentItem.setCount(0);
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean isSortingMatch(ItemStack beingSorted, ItemStack currentItem) {
		return beingSorted.getItem().getItemCategory() == currentItem.getItem().getItemCategory();
	}

	/**
	 * Is the chest we're testing part of our chest list already?
	 */
	private boolean checkIfChestsContains(List<Container> chests, Container testChest) {
		for (Container chest : chests) {
			if (chest == testChest) {
				return true;
			}

			if (chest instanceof CompoundContainer && ((CompoundContainer) chest).contains(testChest)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return an empty slot number in the chest, or -1 if the chest is full
	 */
	private int getEmptySlotIn(Container chest) {
		for (int i = 0; i < chest.getContainerSize(); i++) {
			if (chest.getItem(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}
}
