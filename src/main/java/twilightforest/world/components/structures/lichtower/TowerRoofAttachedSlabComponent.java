package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;


public class TowerRoofAttachedSlabComponent extends TowerRoofSlabComponent {

	public TowerRoofAttachedSlabComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTRAS.get(), nbt);
	}

	public TowerRoofAttachedSlabComponent(TFLandmark feature, int i, TowerWingComponent wing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFLTRAS.get(), feature, i, wing, x, y, z);
	}

	/**
	 * Makes a flat, pyramid-shaped roof that is connected to the parent tower
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		makeConnectedCap(world, Blocks.BIRCH_SLAB.defaultBlockState(), Blocks.BIRCH_PLANKS.defaultBlockState(), sbb);
	}
}
