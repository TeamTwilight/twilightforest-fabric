package twilightforest.world.components.structures.mushroomtower;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import twilightforest.world.components.structures.TFStructureDecorator;

public class MushroomTowerDecorator extends TFStructureDecorator {

	public MushroomTowerDecorator() {
		this.blockState = Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false);
		this.accentState = Blocks.RED_MUSHROOM_BLOCK.defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = Blocks.SPRUCE_STAIRS.defaultBlockState();
		this.pillarState = Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false);
		this.floorState = Blocks.OAK_PLANKS.defaultBlockState();
	}
}
