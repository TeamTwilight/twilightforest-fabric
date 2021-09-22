package twilightforest.enums;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.BanisterBlock;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeaturePlacers;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum StructureWoodVariant implements StringRepresentable {

	OAK(Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_PRESSURE_PLATE, TFBlocks.oak_banister),
	SPRUCE(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PRESSURE_PLATE, TFBlocks.spruce_banister),
	BIRCH(Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PRESSURE_PLATE, TFBlocks.birch_banister),
	JUNGLE(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PRESSURE_PLATE, TFBlocks.jungle_banister),
	ACACIA(Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PRESSURE_PLATE, TFBlocks.acacia_banister),
	DARK_OAK(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PRESSURE_PLATE, TFBlocks.dark_oak_banister),

	TWILIGHT_OAK(TFBlocks.twilight_oak_planks, TFBlocks.twilight_oak_stairs, TFBlocks.twilight_oak_slab, TFBlocks.twilight_oak_button, TFBlocks.twilight_oak_fence, TFBlocks.twilight_oak_gate, TFBlocks.twilight_oak_plate, TFBlocks.twilight_oak_banister),
	CANOPY(TFBlocks.canopy_planks, TFBlocks.canopy_stairs, TFBlocks.canopy_slab, TFBlocks.canopy_button, TFBlocks.canopy_fence, TFBlocks.canopy_gate, TFBlocks.canopy_plate, TFBlocks.canopy_banister),
	MANGROVE(TFBlocks.mangrove_planks, TFBlocks.mangrove_stairs, TFBlocks.mangrove_slab, TFBlocks.mangrove_button, TFBlocks.mangrove_fence, TFBlocks.mangrove_gate, TFBlocks.mangrove_plate, TFBlocks.mangrove_banister),
	DARK(TFBlocks.dark_planks, TFBlocks.dark_stairs, TFBlocks.dark_slab, TFBlocks.dark_button, TFBlocks.dark_fence, TFBlocks.dark_gate, TFBlocks.dark_plate, TFBlocks.darkwood_banister),
	TIME(TFBlocks.time_planks, TFBlocks.time_stairs, TFBlocks.time_slab, TFBlocks.time_button, TFBlocks.time_fence, TFBlocks.time_gate, TFBlocks.time_plate, TFBlocks.time_banister),
	TRANS(TFBlocks.trans_planks, TFBlocks.trans_stairs, TFBlocks.trans_slab, TFBlocks.trans_button, TFBlocks.trans_fence, TFBlocks.trans_gate, TFBlocks.trans_plate, TFBlocks.trans_banister),
	MINE(TFBlocks.mine_planks, TFBlocks.mine_stairs, TFBlocks.mine_slab, TFBlocks.mine_button, TFBlocks.mine_fence, TFBlocks.mine_gate, TFBlocks.mine_plate, TFBlocks.mine_banister),
	SORT(TFBlocks.sort_planks, TFBlocks.sort_stairs, TFBlocks.sort_slab, TFBlocks.sort_button, TFBlocks.sort_fence, TFBlocks.sort_gate, TFBlocks.sort_plate, TFBlocks.sort_banister);

	private final List<Block> blocks;

	StructureWoodVariant(Supplier<Block> planks, Supplier<StairBlock> stairs, Supplier<Block> slab, Supplier<Block> button, Supplier<Block> fence, Supplier<Block> gate, Supplier<Block> plate, Supplier<BanisterBlock> banister) {
		this(planks.get(), stairs.get(), slab.get(), button.get(), fence.get(), gate.get(), plate.get(), banister.get());
	}

	StructureWoodVariant(Block planks, Block stairs, Block slab, Block button, Block fence, Block gate, Block plate, BanisterBlock banister) {
		this.planks = planks;
		this.stairs = stairs;
		this.slab = slab;
		this.button = button;
		this.fence = fence;
		this.gate = gate;
		this.plate = plate;
		this.banister = banister;
		this.blocks = Arrays.asList(this.planks, this.stairs, this.slab, this.button, this.fence, this.gate, this.plate, this.banister);
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private final Block planks;
	private final Block stairs;
	private final Block slab;
	private final Block button;
	private final Block fence;
	private final Block gate;
	private final Block plate;
	private final BanisterBlock banister;

	@Nullable
	public static StructureWoodVariant getVariantFromBlock(Block block) {
		for (StructureWoodVariant var : values()) {
			if (var.blocks.contains(block)) {
				return var;
			}
		}

		// None of these wood types
		return null;
	}

	public static boolean isPlanks(Block block) {
		StructureWoodVariant var = getVariantFromBlock(block);
		if (var == null) return false;

		return var.planks == block;
	}

	public static BlockState modifyBlockWithType(BlockState stateIn, StructureWoodVariant target) {
		// get all data from stateIn
		Block block = stateIn.getBlock();
		WoodShapes shape = getWoodShapeFromBlock(block);

		ResourceLocation blockRegName = Registry.BLOCK.getKey(block);

		if (blockRegName == null) return stateIn;

		if (shape == WoodShapes.INVALID || !("minecraft".equals(blockRegName.getNamespace()) || TwilightForestMod.ID.equals(blockRegName.getNamespace()))) {
			return stateIn; // Block not recognized
		}

		switch (shape) {
			case BLOCK:
				return target.planks.defaultBlockState();
			case STAIRS:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.stairs);
			case SLAB:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.slab);
			case FENCE:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.fence);
			case GATE:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.gate);
			case BUTTON:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.button);
			case PLATE:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.plate);
			case BANISTER:
				return FeaturePlacers.transferAllStateKeys(stateIn, target.banister);
			default:
				return stateIn; // Block not recognized
		}
	}

	public static WoodShapes getWoodShapeFromBlock(Block b) {
		if (isPlanks(b)) return WoodShapes.BLOCK;
		if (b instanceof StairBlock) return WoodShapes.STAIRS;
		if (b instanceof SlabBlock) return WoodShapes.SLAB;
		if (b instanceof ButtonBlock) return WoodShapes.BUTTON;
		if (b instanceof FenceBlock) return WoodShapes.FENCE;
		if (b instanceof FenceGateBlock) return WoodShapes.GATE;
		if (b instanceof PressurePlateBlock) return WoodShapes.PLATE;
		if (b instanceof BanisterBlock) return WoodShapes.BANISTER;

		return WoodShapes.INVALID;
	}

	public enum WoodShapes {
		BLOCK,
		STAIRS,
		SLAB,
		BUTTON,
		FENCE,
		GATE,
		PLATE,
		BANISTER,
		INVALID
	}
}
