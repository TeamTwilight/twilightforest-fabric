package twilightforest.world.components.structures.finalcastle;

import net.minecraft.world.level.block.Blocks;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;

public class StructureTFDecoratorCastle extends TFStructureDecorator {

	public StructureTFDecoratorCastle() {
		this.blockState = TFBlocks.castle_brick.defaultBlockState();
		this.accentState = Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState();
		this.roofState = TFBlocks.castle_brick_roof.defaultBlockState();
		this.pillarState = TFBlocks.castle_pillar_bold.defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = TFBlocks.castle_stairs_brick.defaultBlockState();
		this.randomBlocks = new CastleBlockProcessor();
	}

}
