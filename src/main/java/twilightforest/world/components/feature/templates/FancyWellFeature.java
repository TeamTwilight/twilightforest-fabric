package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.feature.config.SwizzleConfig;
import twilightforest.world.components.processors.SmartGrassProcessor;

public class FancyWellFeature extends TemplateFeature<SwizzleConfig> {
    private static final ResourceLocation WELL_TOP = TwilightForestMod.prefix("feature/well/fancy_well_top");
    private static final ResourceLocation WELL_BOTTOM = TwilightForestMod.prefix("feature/well/fancy_well_bottom");

    public FancyWellFeature(Codec<SwizzleConfig> config) {
        super(config);
    }

    @Nullable
    @Override
    protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
        return templateManager.getOrCreate(WELL_TOP);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, RandomSource random, SwizzleConfig config) {
        config.buildAddProcessors(settings, random);
    }

    @Override
    protected void postPlacement(WorldGenLevel world, RandomSource random, StructureTemplateManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos, SwizzleConfig config) {
        StructureTemplate template = templateManager.getOrCreate(WELL_BOTTOM);
        if (template == null) {
            return;
        }

        placementPos = placementPos.below(template.getSize().getY());
        placementSettings.addProcessor(SmartGrassProcessor.INSTANCE);
        template.placeInWorld(world, placementPos, placementPos, placementSettings, random, Block.UPDATE_CLIENTS);

        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK)) {
            if (info.nbt() != null && StructureMode.valueOf(info.nbt().getString("mode")) == StructureMode.DATA) {
                this.processMarkers(info, world, rotation, mirror, random);
            }
        }
    }

    @Override
    protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, RandomSource random) {
        String metadata = info.nbt().getString("metadata");
        if (!metadata.startsWith("loot")) {
            return;
        }

        world.removeBlock(info.pos(), false);
        TFLootTables.generateLootContainer(world, info.pos(), Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.UP), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS, TFLootTables.FANCY_WELL);
    }
}
