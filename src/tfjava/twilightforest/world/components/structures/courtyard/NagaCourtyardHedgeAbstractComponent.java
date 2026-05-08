package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.structures.TFStructureComponentTemplate;
import twilightforest.util.BoundingBoxUtils;

public abstract class NagaCourtyardHedgeAbstractComponent extends TFStructureComponentTemplate {

    private final ResourceLocation HEDGE;
    private final ResourceLocation HEDGE_BIG;

    private final BlockPos rotatedPosition;
    private final StructurePlaceSettings placeSettings;
    private StructureTemplate templateBig;
    private StructureTemplate template;

    public NagaCourtyardHedgeAbstractComponent(StructurePieceSerializationContext ctx, StructurePieceType piece, CompoundTag nbt, ResourceLocation hedge, ResourceLocation hedgeBig) {
        super(piece, nbt);
        this.HEDGE = hedge;
        this.HEDGE_BIG = hedgeBig;
        this.rotatedPosition = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        this.placeSettings = new StructurePlaceSettings().setRotation(Rotation.values()[nbt.getInt("rotation") % Rotation.values().length]);
        this.loadTemplates(ctx.structureTemplateManager());
    }

    @SuppressWarnings("WeakerAccess")
    public NagaCourtyardHedgeAbstractComponent(StructureTemplateManager manager, StructurePieceType type, int i, int x, int y, int z, Rotation rotation, ResourceLocation hedge, ResourceLocation hedgeBig) {
        super(type, i, BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 1, 1, 1, null, false));
        this.HEDGE = hedge;
        this.HEDGE_BIG = hedgeBig;
        this.rotatedPosition = new BlockPos(x, y, z);
        this.placeSettings = new StructurePlaceSettings().setRotation(rotation);
        this.loadTemplates(manager);
    }

    protected void loadTemplates(StructureTemplateManager templateManager) {
        template = templateManager.getOrCreate(HEDGE);
        templateBig = templateManager.getOrCreate(HEDGE_BIG);
        this.boundingBox = template.getBoundingBox(placeSettings, rotatedPosition);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("x", rotatedPosition.getX());
        tag.putInt("y", rotatedPosition.getY());
        tag.putInt("z", rotatedPosition.getZ());
        tag.putInt("rotation", placeSettings.getRotation().ordinal());
    }

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeSettings.setBoundingBox(structureBoundingBox).clearProcessors();
        if (template == null) // FIXME: this should never be null in the first place
			LAZY_TEMPLATE_LOADER.run();
        template.placeInWorld(world, rotatedPosition, rotatedPosition, placeSettings.clearProcessors().addProcessor(NagastoneVariants.INSTANCE), random, 18);
        templateBig.placeInWorld(world, rotatedPosition, rotatedPosition, placeSettings.addProcessor(BlockIgnoreProcessor.AIR).addProcessor(new BlockRotProcessor(CourtyardMain.HEDGE_FLOOF)), random, 18);
	}
}

