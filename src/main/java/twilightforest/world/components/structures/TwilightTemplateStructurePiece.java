package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public abstract class TwilightTemplateStructurePiece extends TemplateStructurePiece implements TwilightFeature {
    protected StructureManager structureManager;

    public TwilightTemplateStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, ServerLevel serverLevel, StructurePlaceSettings rl2SettingsFunction) {
        super(structurePieceType, compoundTag, serverLevel, rl -> rl2SettingsFunction);
        this.rotation = this.getRotation();
        this.mirror = this.getMirror();

        this.structureManager = serverLevel.getStructureManager();
    }

    public TwilightTemplateStructurePiece(StructurePieceType type, int genDepth, StructureManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition) {
        super(type, genDepth, structureManager, templateLocation, templateLocation.toString(), placeSettings, startPosition);
        this.rotation = this.getRotation();
        this.mirror = this.getMirror();

        this.structureManager = structureManager;
    }

    @Override
    protected void addAdditionalSaveData(ServerLevel level, CompoundTag structureTag) {
        super.addAdditionalSaveData(level, structureTag);

        structureTag.putInt("rotation", this.placeSettings.getRotation().ordinal());
        structureTag.putInt("mirror", this.placeSettings.getMirror().ordinal());
    }
}
