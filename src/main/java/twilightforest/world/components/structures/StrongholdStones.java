package twilightforest.world.components.structures;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class StrongholdStones extends StructurePiece.BlockSelector {

	@Override
	public void next(RandomSource random, int x, int y, int z, boolean wall) {
		if (!wall) {
			next = Blocks.AIR.defaultBlockState();
		} else {
			float f = random.nextFloat();

			if (f < 0.2F) {
				next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
			} else if (f < 0.5F) {
				next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
			} else if (f < 0.55F) {
				next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
			} else {
				next = Blocks.STONE_BRICKS.defaultBlockState();
			}
		}
	}

}
