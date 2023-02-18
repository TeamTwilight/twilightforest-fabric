package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class TowerBeardAttachedComponent extends TowerBeardComponent {

	public TowerBeardAttachedComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTBA.get(), nbt);
	}

	public TowerBeardAttachedComponent(int i, TowerWingComponent wing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFLTBA.get(), i, wing, x, y, z);

		// just hang out at the very bottom of the tower
		this.boundingBox = new BoundingBox(wing.getBoundingBox().minX(), wing.getBoundingBox().minY() - this.height - 1, wing.getBoundingBox().minZ(), wing.getBoundingBox().maxX(), wing.getBoundingBox().maxY() - 1, wing.getBoundingBox().maxZ());
	}

	/**
	 * Makes a pyramid-shaped beard
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		makeAttachedBeard(world, rand, sbb);
	}

	private void makeAttachedBeard(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		for (int y = 0; y <= height; y++) {
			int min = y + 1;
			int max = size - y;

			generateBox(world, sbb, 0, height - y, min, max, height - y, max, false, rand, TFStructureComponentOld.getStrongholdStones());
		}
	}
}
