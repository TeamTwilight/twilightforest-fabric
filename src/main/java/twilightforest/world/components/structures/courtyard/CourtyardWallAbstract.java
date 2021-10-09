package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.world.components.processors.MossyCobbleTemplateProcessor;
import twilightforest.world.components.structures.TFStructureComponentTemplate;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

@Deprecated
public class CourtyardWallAbstract extends TFStructureComponentTemplate {

    private final ResourceLocation WALL;
    private final ResourceLocation WALL_DECAYED;

    private StructureTemplate decayTemplate;

    public CourtyardWallAbstract(ServerLevel level, StructurePieceType piece, CompoundTag nbt, ResourceLocation wall, ResourceLocation wall_decayed) {
        super(level, piece, nbt);
        this.WALL = wall;
        this.WALL_DECAYED = wall_decayed;
    }

    public CourtyardWallAbstract(StructurePieceType type, TFFeature feature, int i, int x, int y, int z, Rotation rotation, ResourceLocation wall, ResourceLocation wall_decayed) {
        super(type, feature, i, x, y, z, rotation);
        this.WALL = wall;
        this.WALL_DECAYED = wall_decayed;
    }

    @Override
    protected void loadTemplates(StructureManager templateManager) {
        TEMPLATE = templateManager.getOrCreate(WALL);
        decayTemplate = templateManager.getOrCreate(WALL_DECAYED);
    }

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random random, BoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeSettings.setBoundingBox(structureBoundingBox).clearProcessors();
		TEMPLATE.placeInWorld(world, rotatedPosition, rotatedPosition, placeSettings.addProcessor(CourtyardMain.WALL_PROCESSOR).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(BlockIgnoreProcessor.AIR), random, 18);
		decayTemplate.placeInWorld(world, rotatedPosition, rotatedPosition, placeSettings.clearProcessors().addProcessor(MossyCobbleTemplateProcessor.INSTANCE).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR), random, 18);
		return true;
	}
}
