package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.Random;

public class WellFeature extends TemplateFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation WELL_TOP = TwilightForestMod.prefix("feature/well/well_top");
    private static final ResourceLocation WELL_BOTTOM = TwilightForestMod.prefix("feature/well/well_bottom");

    public WellFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Nullable
    @Override
    protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
        return templateManager.getOrCreate(WELL_TOP);
    }

    @Override
    protected void postProcess(WorldGenLevel world, Random random, StructureManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos) {
        StructureTemplate template = templateManager.getOrCreate(WELL_BOTTOM);

        if(template == null)
            return;

        placementPos = placementPos.below(11).relative(rotation.rotate(mirror.mirror(Direction.SOUTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);

        template.placeInWorld(world, placementPos, placementPos, placementSettings, random, 20);

        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK)) {
            this.processData(info, world, rotation, mirror);
        }
    }
}
