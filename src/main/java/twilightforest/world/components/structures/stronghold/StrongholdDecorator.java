package twilightforest.world.components.structures.stronghold;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;

public class StrongholdDecorator extends TFStructureDecorator {

	public StrongholdDecorator() {
		this.blockState = TFBlocks.underbrick.defaultBlockState();
		this.accentState = TFBlocks.underbrick_cracked.defaultBlockState();
		this.fenceState = Blocks.COBBLESTONE_WALL.defaultBlockState();
		this.stairState = Blocks.STONE_BRICK_STAIRS.defaultBlockState();
		this.pillarState = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
		this.platformState = Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP);
		this.randomBlocks = new KnightStones();
	}
}
