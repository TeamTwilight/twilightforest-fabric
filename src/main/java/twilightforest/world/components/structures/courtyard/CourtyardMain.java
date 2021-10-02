package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.block.TFBlocks;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class CourtyardMain extends StructureMazeGenerator {
	static int ROW_OF_CELLS = 8;
	static int RADIUS = (int) ((((ROW_OF_CELLS - 2) / 2.0F) * 12.0F) + 8);
	static int DIAMETER = 2 * RADIUS + 1; //TODO: Unused

	static final float HEDGE_FLOOF = 0.5f;

	static final float WALL_DECAY = 0.1f;
	static final float WALL_INTEGRITY = 0.95f;

	static final CourtyardTerraceTemplateProcessor TERRACE_PROCESSOR = new CourtyardTerraceTemplateProcessor(0.0F);
	static final CourtyardWallTemplateProcessor WALL_PROCESSOR = new CourtyardWallTemplateProcessor(0.0F);
	static final BlockRotProcessor WALL_INTEGRITY_PROCESSOR = new BlockRotProcessor(CourtyardMain.WALL_INTEGRITY);
	static final BlockRotProcessor WALL_DECAY_PROCESSOR = new BlockRotProcessor(CourtyardMain.WALL_DECAY);

	public CourtyardMain(ServerLevel level, CompoundTag nbt) {
		super(level.getStructureManager(), NagaCourtyardPieces.TFNCMn, nbt);
	}

	public CourtyardMain(TFFeature feature, Random rand, int i, int x, int y, int z, StructureManager structureManager) {
		super(NagaCourtyardPieces.TFNCMn, feature, rand, i, ROW_OF_CELLS, ROW_OF_CELLS, x, y, z, structureManager);

		this.setOrientation(Direction.NORTH);

		this.sizeConstraints = feature.getComponentToAddBoundingBox(x, y, z, -RADIUS, -1, -RADIUS, RADIUS * 2, 10, RADIUS * 2, this.getOrientation());
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// naga spawner seems important
		placeBlock(world, TFBlocks.boss_spawner_naga.defaultBlockState(), RADIUS, 3, RADIUS, sbb);

		return true;
	}

	@Override
	public NoiseEffect getNoiseEffect() {
		return NoiseEffect.NONE;
	}
}
