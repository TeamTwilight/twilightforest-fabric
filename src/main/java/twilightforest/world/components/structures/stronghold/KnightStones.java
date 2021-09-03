package twilightforest.world.components.structures.stronghold;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class KnightStones extends StructurePiece.BlockSelector {

	@Override
	public void next(Random random, int x, int y, int z, boolean edge) {
		if (!edge) {
			this.next = Blocks.AIR.defaultBlockState();
		} else {
			float f = random.nextFloat();

			if (f < 0.2F) {
				this.next = TFBlocks.underbrick_cracked.defaultBlockState();
			} else if (f < 0.5F) {
				this.next = TFBlocks.underbrick_mossy.defaultBlockState();
			} else {
				this.next = TFBlocks.underbrick.defaultBlockState();
			}
		}
	}

}
