package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.loot.TFLootTables;
import twilightforest.util.entities.EntityUtil;
import twilightforest.world.components.feature.config.SwizzleConfig;
import twilightforest.world.components.processors.CobbleVariants;
import twilightforest.world.components.processors.StoneBricksVariants;

public class DruidHutFeature extends TemplateFeature<SwizzleConfig> {
    public DruidHutFeature(Codec<SwizzleConfig> config) {
        super(config);
    }

    @Override
    protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
        return templateManager.getOrCreate(Util.getRandom(HutType.values(), random).resourceLocation);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, RandomSource random, SwizzleConfig config) {
        config.buildAddProcessors(settings, random);
    }

    @Override
    protected void postPlacement(WorldGenLevel world, RandomSource random, StructureTemplateManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos, SwizzleConfig config) {
        if (random.nextBoolean()) {
            BasementType[] basementTypes = BasementType.values();
            StructureTemplate template = templateManager.getOrCreate(basementTypes[random.nextInt(basementTypes.length)].getBasement(random.nextBoolean()));
            if (template == null) {
                return;
            }

            placementPos = placementPos.below(12).relative(rotation.rotate(mirror.mirror(Direction.NORTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);
            placementSettings.clearProcessors();
            config.buildAddProcessors(placementSettings, random);
            placementSettings.addProcessor(CobbleVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE);
            template.placeInWorld(world, placementPos, placementPos, placementSettings, random, Block.UPDATE_CLIENTS);

            for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK)) {
                if (info.nbt() != null && StructureMode.valueOf(info.nbt().getString("mode")) == StructureMode.DATA) {
                    this.processMarkers(info, world, rotation, mirror, random);
                }
            }
        }
    }

    @Override
    protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, RandomSource random) {
        String metadata = info.nbt().getString("metadata");
        BlockPos blockPos = info.pos();

        if ("spawner".equals(metadata)) {
            if (world.removeBlock(blockPos, false) && world.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS)) {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity instanceof SpawnerBlockEntity spawner) {
                    spawner.setEntityId(TFEntities.SKELETON_DRUID.get(), random);
                }
            }
        } else if (metadata.startsWith("loot")) {
            world.removeBlock(blockPos, false);
            BlockState chest = metadata.endsWith("T") ? Blocks.TRAPPED_CHEST.defaultBlockState() : Blocks.CHEST.defaultBlockState();

            chest = switch (metadata.substring(5, 6)) {
                case "L" -> chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.RIGHT : ChestType.LEFT);
                case "R" -> chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.LEFT : ChestType.RIGHT);
                default -> chest.setValue(ChestBlock.TYPE, ChestType.SINGLE);
            };

            chest = switch (metadata.substring(4, 5)) {
                case "W" -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
                case "E" -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
                case "S" -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
                default -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
            };

            TFLootTables.generateLootContainer(world, blockPos, chest, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS, metadata.endsWith("J") ? TFLootTables.HUT_JUNK : TFLootTables.BASEMENT);
        } else if (metadata.startsWith("barrel")) {
            world.removeBlock(blockPos, false);
            BlockState barrel = Blocks.BARREL.defaultBlockState();

            barrel = switch (metadata.substring(6, 7)) {
                case "D" -> barrel.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.DOWN)));
                case "W" -> barrel.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
                case "E" -> barrel.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
                case "N" -> barrel.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
                case "S" -> barrel.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
                default -> barrel.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.UP)));
            };

            TFLootTables.generateLootContainer(world, blockPos, barrel, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS, TFLootTables.HUT_JUNK);
        } else if (metadata.startsWith("painting")) {
            world.removeBlock(blockPos, false);
            Direction direction = rotation.rotate(mirror.mirror(switch (metadata.substring(8, 9)) {
                case "W" -> Direction.WEST;
                case "E" -> Direction.EAST;
                case "S" -> Direction.SOUTH;
                default -> Direction.NORTH;
            }));
            String width = metadata.substring(9, 10);
            int paintingWidth = width.matches("\\d+") ? Integer.parseInt(width) : 1;
            boolean flipped = mirror != Mirror.NONE;
            BlockPos hangPos = flipped ? blockPos.relative(direction.getClockWise()) : blockPos;
            EntityUtil.tryHangPainting(world, hangPos, direction, EntityUtil.getPaintingOfSize(world, random, paintingWidth));
        }
    }

    private enum HutType {
        REGULAR(TwilightForestMod.prefix("feature/druid_hut/druid_hut")),
        SIDEWAYS(TwilightForestMod.prefix("feature/druid_hut/druid_sideways")),
        DOUBLE_DECK(TwilightForestMod.prefix("feature/druid_hut/druid_doubledeck"));

        private final ResourceLocation resourceLocation;

        HutType(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }
    }

    private enum BasementType {
        STUDY(TwilightForestMod.prefix("feature/druid_hut/basement_study"), TwilightForestMod.prefix("feature/druid_hut/basement_study_trap")),
        SHELVES(TwilightForestMod.prefix("feature/druid_hut/basement_shelves"), TwilightForestMod.prefix("feature/druid_hut/basement_shelves_trap")),
        GALLERY(TwilightForestMod.prefix("feature/druid_hut/basement_gallery"), TwilightForestMod.prefix("feature/druid_hut/basement_gallery_trap"));

        private final ResourceLocation resourceLocation;
        private final ResourceLocation resourceLocationTrap;
        BasementType(ResourceLocation resourceLocation, ResourceLocation resourceLocationTrap) {
            this.resourceLocation = resourceLocation;
            this.resourceLocationTrap = resourceLocationTrap;
        }

        private ResourceLocation getBasement(boolean trapped) {
            return trapped ? this.resourceLocationTrap : this.resourceLocation;
        }
    }
}
