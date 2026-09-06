package twilightforest.datagen.assets.models;

import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import twilightforest.TFCommon;
import twilightforest.block.*;
import twilightforest.client.model.item.AnimatedItemModel;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.helpers.models.BlockModelBuilders;
import twilightforest.init.TFBlocks;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockModelGenerator extends BlockModelBuilders {
	public BlockModelGenerator(Consumer<BlockModelDefinitionGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public void run() {
		this.generateWoodBlocks();

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(TFBlocks.TWILIGHT_PORTAL)
			.with(plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.TWILIGHT_PORTAL)))
			.with(condition().term(TFPortalBlock.DISALLOW_RETURN, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.TWILIGHT_PORTAL, "_barrier"))));

		this.spawner(TFBlocks.NAGA_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.LICH_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.MINOSHROOM_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.HYDRA_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.UR_GHAST_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.ALPHA_YETI_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.FINAL_BOSS_BOSS_SPAWNER, "block/boss_spawner");
		this.spawner(TFBlocks.SINISTER_SPAWNER, "block/sinister_spawner");

		this.thorns(TFBlocks.BROWN_THORNS, TFBlocks.POTTED_THORN);
		this.thorns(TFBlocks.GREEN_THORNS, TFBlocks.POTTED_GREEN_THORN);
		this.thorns(TFBlocks.BURNT_THORNS, TFBlocks.POTTED_DEAD_THORN);
		this.directionalCrossModel(TFBlocks.THORN_ROSE, PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.THORN_LEAVES, TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.SPRUCE_LEAVES), ModelTemplates.LEAVES), -10380959);
		this.wrapBlockItem(TFBlocks.DEADROCK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_DEADROCK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WEATHERED_DEADROCK, this::createTrivialCube);

		this.createCrossBlock(TFBlocks.FIDDLEHEAD, PlantType.TINTED);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.POTTED_FIDDLEHEAD, plainVariant(ModelTemplates.TINTED_FLOWER_POT_CROSS.create(TFBlocks.POTTED_FIDDLEHEAD, TextureMapping.singleSlot(TextureSlot.PLANT, new Material(TFCommon.prefix("block/potted_fiddlehead"))), this.modelOutput))));
		this.createItemWithGrassTint(TFBlocks.FIDDLEHEAD);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MAYAPPLE, plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.MAYAPPLE))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.POTTED_MAYAPPLE, plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.POTTED_MAYAPPLE))));
		this.registerSimpleFlatItemModel(TFBlocks.MAYAPPLE);
        this.registerSimpleItemModel(TFBlocks.CLOVER_PATCH.asItem(), ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(TFBlocks.CLOVER_PATCH.asItem()), TextureMapping.layer0(new Material(TFCommon.prefix("block/patch/clover"))), this.modelOutput));
		this.registerSimpleItemModel(TFBlocks.MOSS_PATCH.asItem(), ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(TFBlocks.MOSS_PATCH.asItem()), TextureMapping.layer0(new Material(TFCommon.prefix("block/patch/moss"))), this.modelOutput));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.TORCHBERRY_PLANT).with(createBooleanModelDispatch(TorchberryPlantBlock.HAS_BERRIES,
			plainVariant(ModelTemplates.CROSS_EMISSIVE.createWithSuffix(TFBlocks.TORCHBERRY_PLANT, "_berries", TextureMapping.crossEmissive(TFBlocks.TORCHBERRY_PLANT), this.modelOutput)),
			plainVariant(ModelTemplates.CROSS.create(TFBlocks.TORCHBERRY_PLANT, TextureMapping.cross(TFBlocks.TORCHBERRY_PLANT), this.modelOutput)))));
		this.registerSimpleFlatItemModel(TFBlocks.TORCHBERRY_PLANT);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MUSHGLOOM, plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.MUSHGLOOM))));
		this.registerTwoLayerFlatItemModel(TFBlocks.MUSHGLOOM, "_head");
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.POTTED_MUSHGLOOM,plainVariant( ModelTemplates.FLOWER_POT_CROSS.create(TFBlocks.POTTED_MUSHGLOOM, TextureMapping.singleSlot(TextureSlot.PLANT, new Material(TFCommon.prefix("block/potted_mushgloom"))), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.HEDGE, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, new MultiVariant(WeightedList.of(
			new Weighted<>(plainModel(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), this.modelOutput)), 10),
			new Weighted<>(plainModel(ModelTemplates.CUBE_ALL.createWithSuffix(block, "_rose", TextureMapping.cube(TextureMapping.getBlockTexture(block, "_rose")), this.modelOutput)), 1))))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.ROOT_STRAND, plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROOT_STRAND))));
		this.registerSimpleFlatItemModel(TFBlocks.ROOT_STRAND);
		this.registerSimpleTintedItemModel(TFBlocks.FALLEN_LEAVES, this.createFlatItemModelWithBlockTexture(TFBlocks.FALLEN_LEAVES.asItem(), Blocks.OAK_LEAVES), ItemModelUtils.constantTint(-12012264));

		this.nagaStone();

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.SPIRAL_BRICKS).with(PropertyDispatch.initial(SpiralBrickBlock.AXIS_FACING, SpiralBrickBlock.DIAGONAL).generate((axis, diagonals) ->
			plainVariant(TFCommon.prefix("block/spiral_bricks/" + axis.getName() + "_spiral_" + diagonals.getSerializedName())))));
		this.itemModelOutput.accept(TFBlocks.SPIRAL_BRICKS.asItem(), ItemModelUtils.plainModel(TFCommon.prefix("block/spiral_bricks/z_spiral_bottom_right")));

		this.wrapBlockItem(TFBlocks.TWISTED_STONE, block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.wrapBlockItem(TFBlocks.BOLD_STONE_PILLAR, block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.stonePillar();
		this.wroughtIronFence();
		this.terrorcotta();
		MultiVariant floorOminous = this.createFloorFireModels(TFBlocks.OMINOUS_FIRE);
		MultiVariant sideOminous = this.createSideFireModels(TFBlocks.OMINOUS_FIRE);
		this.blockStateOutput.accept(MultiPartGenerator.multiPart(TFBlocks.OMINOUS_FIRE)
			.with(floorOminous)
			.with(sideOminous)
			.with(sideOminous.with(Y_ROT_90))
			.with(sideOminous.with(Y_ROT_180))
			.with(sideOminous.with(Y_ROT_270))
		);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_CANDLE, Blocks.CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_BROWN_CANDLE, Blocks.BROWN_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_WHITE_CANDLE, Blocks.WHITE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE, Blocks.LIGHT_GRAY_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_GRAY_CANDLE, Blocks.GRAY_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_BLACK_CANDLE, Blocks.BLACK_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_RED_CANDLE, Blocks.RED_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_ORANGE_CANDLE, Blocks.ORANGE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_YELLOW_CANDLE, Blocks.YELLOW_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_GREEN_CANDLE, Blocks.GREEN_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_LIME_CANDLE, Blocks.LIME_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_BLUE_CANDLE, Blocks.BLUE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_CYAN_CANDLE, Blocks.CYAN_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE, Blocks.LIGHT_BLUE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_PURPLE_CANDLE, Blocks.PURPLE_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_MAGENTA_CANDLE, Blocks.MAGENTA_CANDLE);
		this.createParticleOnlyBlock(TFBlocks.OMINOUS_PINK_CANDLE, Blocks.PINK_CANDLE);

		this.createCrossBlockWithDefaultItem(TFBlocks.HUGE_WATER_LILY, PlantType.NOT_TINTED);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.RED_THREAD, plainVariant(ModelTemplates.PARTICLE_ONLY.create(TFBlocks.RED_THREAD, TextureMapping.particle(new Material(TFCommon.prefix("block/blank"))), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.MAZESTONE, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MAZESTONE_BRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_MAZESTONE, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_MAZESTONE, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.DECORATIVE_MAZESTONE, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.MAZESTONE)), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.CUT_MAZESTONE, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.MAZESTONE)), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.MAZESTONE_MOSAIC, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(TFBlocks.MAZESTONE_BRICK), TextureMapping.getBlockTexture(block)), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.MAZESTONE_BORDER, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(TFBlocks.MAZESTONE_BRICK), TextureMapping.getBlockTexture(block)), this.modelOutput)))));
		this.wrapTintedBlockItem(TFBlocks.SMOKER, new GrassColorSource(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.TINTED_CUBE_BOTTOM_TOP.create(block, TextureMapping.cubeTop(TFBlocks.FIRE_JET).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BLACK_CONCRETE_POWDER)), this.modelOutput)))));
		this.wrapTintedBlockItem(TFBlocks.FIRE_JET, new GrassColorSource(), block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.TINTED_CUBE_BOTTOM_TOP.create(block, TextureMapping.cubeTop(block).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BLACK_CONCRETE_POWDER)), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.UNDERBRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_UNDERBRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_UNDERBRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.UNDERBRICK_FLOOR, this::createTrivialCube);
		this.trophyPedestal();
		this.wrapBlockItem(TFBlocks.STRONGHOLD_SHIELD, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_TOP.create(block, new TextureMapping().put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_inside")), this.modelOutput))).with(PropertyDispatch.modify(BlockStateProperties.FACING)
			.select(Direction.UP, NOP)
			.select(Direction.DOWN, X_ROT_180)
			.select(Direction.NORTH, X_ROT_90)
			.select(Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
			.select(Direction.WEST, X_ROT_90.then(Y_ROT_270))
			.select(Direction.EAST, X_ROT_90.then(Y_ROT_90)))));

		this.wrapBlockItem(TFBlocks.TOWERWOOD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_TOWERWOOD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_TOWERWOOD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.INFESTED_TOWERWOOD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.ENCASED_TOWERWOOD, this::createTrivialCube);

		this.wrapBlockItem(TFBlocks.ENCASED_SMOKER, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			createBooleanModelDispatch(EncasedSmokerBlock.ACTIVE,
				plainVariant(TFModelTemplates.THREE_LAYER_DEVICE_ACTIVE.createWithSuffix(block, "_on", TFTextureMapping.threeLayerDeviceOn(block, TFBlocks.GHAST_TRAP), this.modelOutput)),
				plainVariant(TFModelTemplates.THREE_LAYER_DEVICE.create(block, TFTextureMapping.threeLayerDevice(block, TFBlocks.GHAST_TRAP, ""), this.modelOutput))))));
		MultiVariant jetOn = plainVariant(TFModelTemplates.THREE_LAYER_DEVICE_ACTIVE.createWithSuffix(TFBlocks.ENCASED_FIRE_JET, "_on", TFTextureMapping.threeLayerDeviceOn(TFBlocks.ENCASED_FIRE_JET, TFBlocks.GHAST_TRAP), this.modelOutput));
		MultiVariant jetOff = plainVariant(TFModelTemplates.THREE_LAYER_DEVICE.create(TFBlocks.ENCASED_FIRE_JET, TFTextureMapping.threeLayerDevice(TFBlocks.ENCASED_FIRE_JET, TFBlocks.GHAST_TRAP, ""), this.modelOutput));
		this.wrapBlockItem(TFBlocks.ENCASED_FIRE_JET, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(EncasedFireJetBlock.STATE).generate(variant -> variant.isVariantOn() ? jetOn : jetOff))));
		this.wrapBlockItem(TFBlocks.GHAST_TRAP, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(GhastTrapBlock.ACTIVE,
			plainVariant(TFModelTemplates.THREE_LAYER_DEVICE_ACTIVE.createWithSuffix(block, "_on", TFTextureMapping.threeLayerDeviceOn(block, block), this.modelOutput)),
				plainVariant(TFModelTemplates.THREE_LAYER_DEVICE.create(block, TFTextureMapping.threeLayerDevice(block, block, ""), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.ANTIBUILDER, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(TFModelTemplates.THREE_LAYER_BLOCK.create(block, TFTextureMapping.threeLayerBlock(block, ""), this.modelOutput)))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.ANTIBUILT_BLOCK, plainVariant(TFModelTemplates.TWO_LAYER_BLOCK_DARKER.create(TFBlocks.ANTIBUILT_BLOCK, TFTextureMapping.twoLayerBlock(TFBlocks.ANTIBUILT_BLOCK, ""), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.CARMINITE_BUILDER, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BuilderBlock.STATE).generate(state -> {
			Identifier model = switch (state) {
				case BUILDER_INACTIVE -> TFModelTemplates.THREE_LAYER_BLOCK.createWithSuffix(block, "", TFTextureMapping.threeLayerBlock(block, ""), this.modelOutput);
				case BUILDER_ACTIVE -> TFModelTemplates.THREE_LAYER_BLOCK.createWithSuffix(block, "_on", TFTextureMapping.threeLayerBlock(block, "_on"), this.modelOutput);
				case BUILDER_TIMEOUT -> TFModelTemplates.THREE_LAYER_BLOCK.createWithSuffix(block, "_timeout", TFTextureMapping.threeLayerBlock(block, "_timeout"), this.modelOutput);
			};
			return plainVariant(model);
		}))));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.BUILT_BLOCK)
			.with(PropertyDispatch.initial(TranslucentBuiltBlock.ACTIVE).generate(active -> plainVariant(active ?
				TFModelTemplates.FULLBRIGHT_BLOCK.createWithSuffix(TFBlocks.BUILT_BLOCK, "_on", TextureMapping.cube(TextureMapping.getBlockTexture(TFBlocks.BUILT_BLOCK, "_on")), this.modelOutput) :
				TFModelTemplates.FULLBRIGHT_BLOCK.create(TFBlocks.BUILT_BLOCK, TextureMapping.cube(TFBlocks.BUILT_BLOCK), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.CARMINITE_REACTOR, block -> this.createTrivialBlock(block, TexturedModel.createDefault(block1 -> TFTextureMapping.threeLayerBlock(block, ""), TFModelTemplates.THREE_LAYER_BLOCK)));
		this.wrapBlockItem(TFBlocks.LOCKED_VANISHING_BLOCK, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(VanishingBlock.ACTIVE).generate(active -> plainVariant(active ?
				TFModelTemplates.THREE_LAYER_BLOCK.createWithSuffix(block, "_on", TFTextureMapping.threeLayerBlock(block, "_on"), this.modelOutput) :
				TFModelTemplates.THREE_LAYER_BLOCK.create(block, TFTextureMapping.threeLayerBlock(block, ""), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.VANISHING_BLOCK, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(VanishingBlock.ACTIVE).generate(active -> plainVariant(active ?
				TFModelTemplates.THREE_LAYER_BLOCK.createWithSuffix(block, "_on", TFTextureMapping.threeLayerBlock(block, "_on"), this.modelOutput) :
				TFModelTemplates.THREE_LAYER_BLOCK.create(block, TFTextureMapping.threeLayerBlock(block, ""), this.modelOutput))))));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.UNBREAKABLE_VANISHING_BLOCK)
			.with(PropertyDispatch.initial(VanishingBlock.ACTIVE).generate(active -> plainVariant(active ?
				ModelLocationUtils.getModelLocation(TFBlocks.VANISHING_BLOCK, "_on") :
				ModelLocationUtils.getModelLocation(TFBlocks.VANISHING_BLOCK)))));
		this.wrapBlockItem(TFBlocks.REAPPEARING_BLOCK, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(ReappearingBlock.ACTIVE, ReappearingBlock.VANISHED).generate((active, vanished) -> {
				String variant = (vanished ? "_invis" : "") + (active ? "_on" : "");
				return plainVariant(vanished ?
					TFModelTemplates.SMALL_CUBE.createWithSuffix(block, variant, TextureMapping.cube(TextureMapping.getBlockTexture(block, variant)), this.modelOutput) :
					TFModelTemplates.THREE_LAYER_BLOCK.createWithSuffix(block, variant, TFTextureMapping.threeLayerBlock(block, variant), this.modelOutput));
			}))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FAKE_GOLD, plainVariant(ModelLocationUtils.getModelLocation(Blocks.GOLD_BLOCK))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.FAKE_DIAMOND, plainVariant(ModelLocationUtils.getModelLocation(Blocks.DIAMOND_BLOCK))));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.EXPERIMENT_115).with(PropertyDispatch.initial(Experiment115Block.BITES_TAKEN, Experiment115Block.REGENERATE).generate((bites, regen) -> {
			String suffix = String.format("_%d_8", 8 - bites);
			Identifier model;
			if (regen) {
				model = TFModelTemplates.create("twilightforest:experiment_115" + suffix, suffix + "_regenerating", TFTextureSlot.TOP_2).create(TFBlocks.EXPERIMENT_115, new TextureMapping().put(TFTextureSlot.TOP_2, new Material(TFCommon.prefix("block/experiment115_sprinkle"))), this.modelOutput);
			} else {
				model = ModelLocationUtils.getModelLocation(TFBlocks.EXPERIMENT_115, suffix);
			}
			return plainVariant(model);
		})));

		this.wrapBlockItem(TFBlocks.HUGE_STALK, block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.createParticleOnlyBlock(TFBlocks.BEANSTALK_GROWER, TFBlocks.HUGE_STALK);
		this.wrapBlockItem(TFBlocks.BEANSTALK_LEAVES, block -> plainVariant(ModelLocationUtils.getModelLocation(Blocks.AZALEA_LEAVES)));
		Identifier mushgloomInside = ModelTemplates.SINGLE_FACE.create(TFCommon.prefix("huge_mushgloom_inside"), TextureMapping.cube(new Material(TFCommon.prefix("block/huge_mushgloom_inside"))), this.modelOutput);
		this.createMultifaceBlock(TFBlocks.HUGE_MUSHGLOOM, mushgloomInside, false);
		this.createMultifaceBlock(TFBlocks.HUGE_MUSHGLOOM_STEM, mushgloomInside, false);
		Identifier trollsteinnInside = ModelTemplates.SINGLE_FACE.create(TFCommon.prefix("trollsteinn_inside"), TextureMapping.cube(new Material(TFCommon.prefix("block/trollsteinn_light"))), this.modelOutput);
		this.createMultifaceBlock(TFBlocks.TROLLSTEINN, trollsteinnInside, true);
		this.createCrossBlockWithDefaultItem(TFBlocks.TROLLVIDR, PlantType.NOT_TINTED);
		this.createCrossBlockWithDefaultItem(TFBlocks.UNRIPE_TROLLBER, PlantType.NOT_TINTED);
		this.createCrossBlockWithDefaultItem(TFBlocks.TROLLBER, PlantType.EMISSIVE_NOT_TINTED);
		this.wrapBlockItem(TFBlocks.FLUFFY_CLOUD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WISPY_CLOUD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.RAINY_CLOUD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.SNOWY_CLOUD, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.UBEROUS_SOIL, block -> this.createTrivialBlock(block, TexturedModel.createDefault(block1 -> new TextureMapping().put(TextureSlot.DIRT, TextureMapping.getBlockTexture(block)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(block)), ModelTemplates.FARMLAND)));

		this.wrapBlockItem(TFBlocks.CASTLE_BRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WORN_CASTLE_BRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_CASTLE_BRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MOSSY_CASTLE_BRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.THICK_CASTLE_BRICK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CASTLE_ROOF_TILE, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.wrapBlockItem(TFBlocks.ENCASED_CASTLE_BRICK_TILE, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(TextureMapping.getBlockTexture(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, "_top")), this.modelOutput)))));
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR, block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE, this::createTrivialCube);
		this.generateStairs(TFBlocks.CASTLE_BRICK_STAIRS, TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE));
		this.generateStairs(TFBlocks.WORN_CASTLE_BRICK_STAIRS, TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE));
		this.generateStairs(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE));
		this.generateStairs(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS, TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE));
		this.bisectedStairsBlock(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS, TextureMapping.getBlockTexture(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, "_h"), TextureMapping.getBlockTexture(TFBlocks.CASTLE_BRICK), TextureMapping.getBlockTexture(TFBlocks.CASTLE_ROOF_TILE));
		this.generateStairs(TFBlocks.BOLD_CASTLE_BRICK_STAIRS, TextureMapping.cube(TFBlocks.BOLD_CASTLE_BRICK_TILE));
		this.generateRuneBlock(TFBlocks.PINK_CASTLE_RUNE_BRICK, 16711935);
		this.generateRuneBlock(TFBlocks.YELLOW_CASTLE_RUNE_BRICK, 16776960);
		this.generateRuneBlock(TFBlocks.BLUE_CASTLE_RUNE_BRICK, 65535);
		this.generateRuneBlock(TFBlocks.VIOLET_CASTLE_RUNE_BRICK, 4915330);

		this.generateSpecialModel(TFBlocks.KEEPSAKE_CASKET, Blocks.NETHERITE_BLOCK, block -> ItemModelUtils.specialModel(TFCommon.prefix("item/keepsake_casket"), new KeepsakeCasketSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.SKULL_CHEST, Blocks.LIGHT_GRAY_CONCRETE_POWDER, block -> ItemModelUtils.specialModel(TFCommon.prefix("item/skull_chest"), new SkullChestSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.CICADA, Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TFCommon.prefix("item/cicada"), new CicadaSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.FIREFLY, Blocks.SLIME_BLOCK, _ -> new AnimatedItemModel.Unbaked(ItemModelUtils.specialModel(TFCommon.prefix("item/firefly"), new FireflySpecialRenderer.Unbaked())));
		this.generateSpecialModel(TFBlocks.MOONWORM, Blocks.SLIME_BLOCK, _ -> new AnimatedItemModel.Unbaked(ItemModelUtils.specialModel(TFCommon.prefix("item/moonworm"), new MoonwormSpecialRenderer.Unbaked())));

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.CANDELABRA)
			.with(PropertyDispatch.initial(CandelabraBlock.ON_WALL)
				.select(true, plainVariant(TFCommon.prefix("block/wall_candelabra")))
				.select(false, plainVariant(TFCommon.prefix("block/candelabra"))))
			.with(PropertyDispatch.modify(CandelabraBlock.FACING)
				.select(Direction.NORTH, Y_ROT_180)
				.select(Direction.EAST, Y_ROT_270)
				.select(Direction.WEST, Y_ROT_90)
				.select(Direction.SOUTH, NOP)));
		this.itemModelOutput.accept(TFBlocks.CANDELABRA.asItem(), ItemModelUtils.composite(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.CANDELABRA)), ItemModelUtils.specialModel(TFCommon.prefix("block/candelabra"), new CandelabraSpecialRenderer.Unbaked())));

		this.generateSkullCandle(TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE);
		this.generateSkullCandle(TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE);
		this.generateSkullCandle(TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE);
		this.generateSkullCandle(TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE);
		this.generateSkullCandle(TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE);
		this.generateSkullCandle(TFBlocks.PIGLIN_SKULL_CANDLE, TFBlocks.PIGLIN_WALL_SKULL_CANDLE);

		var major = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TFCommon.prefix("item/major_boss_trophy"), TextureMapping.layer0(new Material(TFCommon.prefix("item/trophy"))), this.modelOutput));
		var minor = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TFCommon.prefix("item/minor_boss_trophy"), TextureMapping.layer0(new Material(TFCommon.prefix("item/trophy_minor"))), this.modelOutput));
		var quest = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TFCommon.prefix("item/quest_trophy"), TextureMapping.layer0(new Material(TFCommon.prefix("item/trophy_quest"))), this.modelOutput));

		this.generateTrophy(TFBlocks.NAGA_TROPHY, TFBlocks.NAGA_WALL_TROPHY, major);
		this.generateTrophy(TFBlocks.LICH_TROPHY, TFBlocks.LICH_WALL_TROPHY, major);
		this.generateTrophy(TFBlocks.MINOSHROOM_TROPHY, TFBlocks.MINOSHROOM_WALL_TROPHY, minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.HYDRA_TROPHY, TFBlocks.HYDRA_WALL_TROPHY, major, "hydra_trophy");
		this.generateTrophy(TFBlocks.KNIGHT_PHANTOM_TROPHY, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY, minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.UR_GHAST_TROPHY, TFBlocks.UR_GHAST_WALL_TROPHY, major, "ur_ghast_trophy");
		this.generateTrophy(TFBlocks.ALPHA_YETI_TROPHY, TFBlocks.ALPHA_YETI_WALL_TROPHY, minor, "alpha_yeti_trophy");
		this.generateTrophy(TFBlocks.SNOW_QUEEN_TROPHY, TFBlocks.SNOW_QUEEN_WALL_TROPHY, major);
		this.generateTrophy(TFBlocks.QUEST_RAM_TROPHY, TFBlocks.QUEST_RAM_WALL_TROPHY, quest, "smaller_gui_trophy");

		this.ironLadder();

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(TFBlocks.ROPE)
			.with(condition().term(RopeBlock.X, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE, "_x")))
			.with(condition().term(RopeBlock.Y, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE, "_y")))
			.with(condition().term(RopeBlock.Z, true), plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE, "_z")))
			.with(new CombinedCondition(CombinedCondition.Operation.OR, List.of(
					and(condition().term(RopeBlock.X, true), condition().term(RopeBlock.Y, true)),
					and(condition().term(RopeBlock.Y, true), condition().term(RopeBlock.Z, true)),
					and(condition().term(RopeBlock.Z, true), condition().term(RopeBlock.X, true)))),
				plainVariant(ModelLocationUtils.getModelLocation(TFBlocks.ROPE, "_knot"))));
		this.itemModelOutput.accept(TFBlocks.ROPE.asItem(), ItemModelUtils.plainModel(this.createFlatItemModelWithBlockTexture(TFBlocks.ROPE.asItem(), TFBlocks.ROPE)));

		this.wrapBlockItem(TFBlocks.UNCRAFTING_TABLE, block -> this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(UncraftingTableBlock.POWERED, plainVariant(TFModelTemplates.TWO_LAYER_COLUMN_NO_BOTTOM.createWithSuffix(block, "_activated", TFTextureMapping.uncraftingTableOn(block), this.modelOutput)), plainVariant(TFModelTemplates.CUBE_BOTTOM_2_LAYER_TOP.create(block, TFTextureMapping.uncraftingTable(block), this.modelOutput))))));
		this.wrapBlockItem(TFBlocks.STEELEAF_BLOCK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.IRONWOOD_BLOCK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.KNIGHTMETAL_BLOCK, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(block)))));
		this.wrapBlockItem(TFBlocks.FIERY_BLOCK, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(block)))));
		this.wrapBlockItem(TFBlocks.CARMINITE_BLOCK, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelLocationUtils.getModelLocation(block)))));

		this.createParticleOnlyBlock(TFBlocks.BRAZIER, TFBlocks.CANOPY_PLANKS);
		this.wrapBlockItem(TFBlocks.SLIDER, block -> this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(block, plainVariant(ModelLocationUtils.getModelLocation(block)), plainVariant(ModelLocationUtils.getModelLocation(block, "_horiz")))));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.CINDER_FURNACE, plainVariant(ModelLocationUtils.getModelLocation(Blocks.FURNACE))));
		this.registerSimpleItemModel(TFBlocks.CINDER_FURNACE, Identifier.withDefaultNamespace("block/furnace"));
		this.woodProvider(TFBlocks.CINDER_LOG).logWithHorizontal(TFBlocks.CINDER_LOG).wood(TFBlocks.CINDER_WOOD);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE, plainVariant(TFCommon.prefix("block/miniature/portal"))));
		this.registerSimpleTintedItemModel(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE, TFCommon.prefix("block/miniature/portal"), new GrassColorSource());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE, plainVariant(TFCommon.prefix("block/miniature/naga_courtyard"))));
		this.registerSimpleTintedItemModel(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE, TFCommon.prefix("block/miniature/naga_courtyard"), new GrassColorSource());
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE, plainVariant(TFCommon.prefix("block/miniature/lich_tower"))));
		this.registerSimpleItemModel(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE, TFCommon.prefix("block/miniature/lich_tower"));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE, plainVariant(TFCommon.prefix("block/miniature/labyrinth"))));
		this.registerSimpleItemModel(TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE, TFCommon.prefix("block/miniature/labyrinth"));
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE, plainVariant(TFCommon.prefix("block/miniature/dark_tower"))));
		this.registerSimpleItemModel(TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE, TFCommon.prefix("block/miniature/dark_tower"));
	}

	private void generateWoodBlocks() {
		this.wrapBlockItem(TFBlocks.ROOT_BLOCK, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.LIVEROOT_BLOCK, this::createTrivialCube);

		this.woodProvider(TFBlocks.TWILIGHT_OAK_LOG).logWithHorizontal(TFBlocks.TWILIGHT_OAK_LOG).wood(TFBlocks.TWILIGHT_OAK_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG).logWithHorizontal(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG).wood(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD);
		this.generateHollowLog(TFBlocks.TWILIGHT_OAK_LOG, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.TWILIGHT_OAK_SAPLING, TFBlocks.POTTED_TWILIGHT_OAK_SAPLING, PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.TWILIGHT_OAK_LEAVES, TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.OAK_LEAVES), ModelTemplates.LEAVES), -12012264);
		this.wrapBlockItem(TFBlocks.TWILIGHT_OAK_PLANKS, this::createTrivialCube);
		TextureMapping twilightOak = TextureMapping.cube(TFBlocks.TWILIGHT_OAK_PLANKS);
		this.generateStairs(TFBlocks.TWILIGHT_OAK_STAIRS, twilightOak);
		this.generateSlab(TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_PLANKS, twilightOak);
		this.generateButton(TFBlocks.TWILIGHT_OAK_BUTTON, twilightOak);
		this.generateFence(TFBlocks.TWILIGHT_OAK_FENCE, twilightOak);
		this.generateFenceGate(TFBlocks.TWILIGHT_OAK_GATE, twilightOak);
		this.generatePressurePlate(TFBlocks.TWILIGHT_OAK_PLATE, twilightOak);
		this.generateTrapdoor(TFBlocks.TWILIGHT_OAK_TRAPDOOR, true);
		this.generateDoor(TFBlocks.TWILIGHT_OAK_DOOR, false);
		this.generateSign(TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_WALL_SIGN, twilightOak);
		this.generateHangingSign(TFBlocks.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);
		this.generateBanister(TFBlocks.TWILIGHT_OAK_BANISTER, twilightOak);
		this.generateDryingRack(TFBlocks.TWILIGHT_OAK_DRYING_RACK, twilightOak);

		this.woodProvider(TFBlocks.CANOPY_LOG).logWithHorizontal(TFBlocks.CANOPY_LOG).wood(TFBlocks.CANOPY_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_CANOPY_LOG).logWithHorizontal(TFBlocks.STRIPPED_CANOPY_LOG).wood(TFBlocks.STRIPPED_CANOPY_WOOD);
		this.generateHollowLog(TFBlocks.CANOPY_LOG, TFBlocks.STRIPPED_CANOPY_LOG, TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.CANOPY_SAPLING, TFBlocks.POTTED_CANOPY_SAPLING, PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.CANOPY_LEAVES, TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.SPRUCE_LEAVES), ModelTemplates.LEAVES), -10380959);
		this.wrapBlockItem(TFBlocks.CANOPY_PLANKS, this::createTrivialCube);
		TextureMapping canopy = TextureMapping.cube(TFBlocks.CANOPY_PLANKS);
		this.generateStairs(TFBlocks.CANOPY_STAIRS, canopy);
		this.generateSlab(TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_PLANKS, canopy);
		this.generateButton(TFBlocks.CANOPY_BUTTON, canopy);
		this.generateFence(TFBlocks.CANOPY_FENCE, canopy);
		this.generateFenceGate(TFBlocks.CANOPY_GATE, canopy);
		this.generatePressurePlate(TFBlocks.CANOPY_PLATE, canopy);
		this.generateTrapdoor(TFBlocks.CANOPY_TRAPDOOR, true);
		this.generateDoor(TFBlocks.CANOPY_DOOR, false);
		this.generateSign(TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_WALL_SIGN, canopy);
		this.generateHangingSign(TFBlocks.CANOPY_HANGING_SIGN, TFBlocks.CANOPY_WALL_HANGING_SIGN, TFBlocks.STRIPPED_CANOPY_LOG);
		this.generateBanister(TFBlocks.CANOPY_BANISTER, canopy);
		this.generateDryingRack(TFBlocks.CANOPY_DRYING_RACK, canopy);
		this.wrapBlockItem(TFBlocks.CANOPY_BOOKSHELF, block -> this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(TFBlocks.CANOPY_PLANKS)), this.modelOutput)))));
		this.generateChiseledBookshelf(TFBlocks.CHISELED_CANOPY_BOOKSHELF);
		this.wrapBlockItem(TFBlocks.CANOPY_WINDOW, this::createTrivialCube);
		this.generatePaneBlock(TFBlocks.CANOPY_WINDOW, TFBlocks.CANOPY_WINDOW_PANE);

		this.woodProvider(TFBlocks.MANGROVE_LOG).logWithHorizontal(TFBlocks.MANGROVE_LOG).wood(TFBlocks.MANGROVE_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_MANGROVE_LOG).logWithHorizontal(TFBlocks.STRIPPED_MANGROVE_LOG).wood(TFBlocks.STRIPPED_MANGROVE_WOOD);
		this.generateHollowLog(TFBlocks.MANGROVE_LOG, TFBlocks.STRIPPED_MANGROVE_LOG, TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.MANGROVE_SAPLING, TFBlocks.POTTED_MANGROVE_SAPLING, PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.MANGROVE_LEAVES, TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.BIRCH_LEAVES), ModelTemplates.LEAVES), -8345771);
		this.wrapBlockItem(TFBlocks.MANGROVE_ROOT, this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.MANGROVE_PLANKS, this::createTrivialCube);
		TextureMapping mangrove = TextureMapping.cube(TFBlocks.MANGROVE_PLANKS);
		this.generateStairs(TFBlocks.MANGROVE_STAIRS, mangrove);
		this.generateSlab(TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_PLANKS, mangrove);
		this.generateButton(TFBlocks.MANGROVE_BUTTON, mangrove);
		this.generateFence(TFBlocks.MANGROVE_FENCE, mangrove);
		this.generateFenceGate(TFBlocks.MANGROVE_GATE, mangrove);
		this.generatePressurePlate(TFBlocks.MANGROVE_PLATE, mangrove);
		this.generateTrapdoor(TFBlocks.MANGROVE_TRAPDOOR, true);
		this.generateDoor(TFBlocks.MANGROVE_DOOR, false);
		this.generateSign(TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_WALL_SIGN, mangrove);
		this.generateHangingSign(TFBlocks.MANGROVE_HANGING_SIGN, TFBlocks.MANGROVE_WALL_HANGING_SIGN, TFBlocks.STRIPPED_MANGROVE_LOG);
		this.generateBanister(TFBlocks.MANGROVE_BANISTER, mangrove);
		this.generateDryingRack(TFBlocks.MANGROVE_DRYING_RACK, mangrove);

		this.woodProvider(TFBlocks.DARK_LOG).logWithHorizontal(TFBlocks.DARK_LOG).wood(TFBlocks.DARK_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_DARK_LOG).logWithHorizontal(TFBlocks.STRIPPED_DARK_LOG).wood(TFBlocks.STRIPPED_DARK_WOOD);
		this.generateHollowLog(TFBlocks.DARK_LOG, TFBlocks.STRIPPED_DARK_LOG, TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.DARKWOOD_SAPLING, TFBlocks.POTTED_DARKWOOD_SAPLING, PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.DARK_LEAVES, TexturedModel.LEAVES, -12012264);
		this.blockStateOutput.accept(createSimpleBlock(TFBlocks.HARDENED_DARK_LEAVES, plainVariant(ModelTemplates.LEAVES.create(TFBlocks.HARDENED_DARK_LEAVES, TextureMapping.cube(TFBlocks.DARK_LEAVES), this.modelOutput))));
		this.wrapBlockItem(TFBlocks.DARK_PLANKS, this::createTrivialCube);
		TextureMapping dark = TextureMapping.cube(TFBlocks.DARK_PLANKS);
		this.generateStairs(TFBlocks.DARK_STAIRS, dark);
		this.generateSlab(TFBlocks.DARK_SLAB, TFBlocks.DARK_PLANKS, dark);
		this.generateButton(TFBlocks.DARK_BUTTON, dark);
		this.generateFence(TFBlocks.DARK_FENCE, dark);
		this.generateFenceGate(TFBlocks.DARK_GATE, dark);
		this.generatePressurePlate(TFBlocks.DARK_PLATE, dark);
		this.generateTrapdoor(TFBlocks.DARK_TRAPDOOR, true);
		this.generateDoor(TFBlocks.DARK_DOOR, false);
		this.generateSign(TFBlocks.DARK_SIGN, TFBlocks.DARK_WALL_SIGN, dark);
		this.generateHangingSign(TFBlocks.DARK_HANGING_SIGN, TFBlocks.DARK_WALL_HANGING_SIGN, TFBlocks.STRIPPED_DARK_LOG);
		this.generateBanister(TFBlocks.DARK_BANISTER, dark);
		this.generateDryingRack(TFBlocks.DARK_DRYING_RACK, dark);

		this.woodProvider(TFBlocks.TIME_LOG).logWithHorizontal(TFBlocks.TIME_LOG).wood(TFBlocks.TIME_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_TIME_LOG).logWithHorizontal(TFBlocks.STRIPPED_TIME_LOG).wood(TFBlocks.STRIPPED_TIME_WOOD);
		this.generateTreeCore(TFBlocks.TIME_LOG, TFBlocks.TIME_LOG_CORE);
		this.generateHollowLog(TFBlocks.TIME_LOG, TFBlocks.STRIPPED_TIME_LOG, TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.TIME_SAPLING, TFBlocks.POTTED_TIME_SAPLING, PlantType.NOT_TINTED);
		this.wrapBlockItem(TFBlocks.TIME_PLANKS, this::createTrivialCube);
		TextureMapping time = TextureMapping.cube(TFBlocks.TIME_PLANKS);
		this.generateStairs(TFBlocks.TIME_STAIRS, time);
		this.generateSlab(TFBlocks.TIME_SLAB, TFBlocks.TIME_PLANKS, time);
		this.generateButton(TFBlocks.TIME_BUTTON, time);
		this.generateFence(TFBlocks.TIME_FENCE, time);
		this.generateFenceGate(TFBlocks.TIME_GATE, time);
		this.generatePressurePlate(TFBlocks.TIME_PLATE, time);
		this.generateTrapdoor(TFBlocks.TIME_TRAPDOOR, true);
		this.generateDoor(TFBlocks.TIME_DOOR, false);
		this.generateSign(TFBlocks.TIME_SIGN, TFBlocks.TIME_WALL_SIGN, time);
		this.generateHangingSign(TFBlocks.TIME_HANGING_SIGN, TFBlocks.TIME_WALL_HANGING_SIGN, TFBlocks.STRIPPED_TIME_LOG);
		this.generateBanister(TFBlocks.TIME_BANISTER, time);
		this.generateDryingRack(TFBlocks.TIME_DRYING_RACK, time);

		this.woodProvider(TFBlocks.TRANSFORMATION_LOG).logWithHorizontal(TFBlocks.TRANSFORMATION_LOG).wood(TFBlocks.TRANSFORMATION_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_TRANSFORMATION_LOG).logWithHorizontal(TFBlocks.STRIPPED_TRANSFORMATION_LOG).wood(TFBlocks.STRIPPED_TRANSFORMATION_WOOD);
		this.generateTreeCore(TFBlocks.TRANSFORMATION_LOG, TFBlocks.TRANSFORMATION_LOG_CORE);
		this.generateHollowLog(TFBlocks.TRANSFORMATION_LOG, TFBlocks.STRIPPED_TRANSFORMATION_LOG, TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.TRANSFORMATION_SAPLING, TFBlocks.POTTED_TRANSFORMATION_SAPLING, PlantType.NOT_TINTED);
		this.wrapBlockItem(TFBlocks.TRANSFORMATION_PLANKS, this::createTrivialCube);
		TextureMapping transformation = TextureMapping.cube(TFBlocks.TRANSFORMATION_PLANKS);
		this.generateStairs(TFBlocks.TRANSFORMATION_STAIRS, transformation);
		this.generateSlab(TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_PLANKS, transformation);
		this.generateButton(TFBlocks.TRANSFORMATION_BUTTON, transformation);
		this.generateFence(TFBlocks.TRANSFORMATION_FENCE, transformation);
		this.generateFenceGate(TFBlocks.TRANSFORMATION_GATE, transformation);
		this.generatePressurePlate(TFBlocks.TRANSFORMATION_PLATE, transformation);
		this.generateTrapdoor(TFBlocks.TRANSFORMATION_TRAPDOOR, true);
		this.generateDoor(TFBlocks.TRANSFORMATION_DOOR, false);
		this.generateSign(TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_WALL_SIGN, transformation);
		this.generateHangingSign(TFBlocks.TRANSFORMATION_HANGING_SIGN, TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		this.generateBanister(TFBlocks.TRANSFORMATION_BANISTER, transformation);
		this.generateDryingRack(TFBlocks.TRANSFORMATION_DRYING_RACK, transformation);

		this.woodProvider(TFBlocks.MINING_LOG).logWithHorizontal(TFBlocks.MINING_LOG).wood(TFBlocks.MINING_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_MINING_LOG).logWithHorizontal(TFBlocks.STRIPPED_MINING_LOG).wood(TFBlocks.STRIPPED_MINING_WOOD);
		this.generateTreeCore(TFBlocks.MINING_LOG, TFBlocks.MINING_LOG_CORE);
		this.generateHollowLog(TFBlocks.MINING_LOG, TFBlocks.STRIPPED_MINING_LOG, TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.MINING_SAPLING, TFBlocks.POTTED_MINING_SAPLING, PlantType.NOT_TINTED);
		this.wrapBlockItem(TFBlocks.MINING_PLANKS, this::createTrivialCube);
		TextureMapping mining = TextureMapping.cube(TFBlocks.MINING_PLANKS);
		this.generateStairs(TFBlocks.MINING_STAIRS, mining);
		this.generateSlab(TFBlocks.MINING_SLAB, TFBlocks.MINING_PLANKS, mining);
		this.generateButton(TFBlocks.MINING_BUTTON, mining);
		this.generateFence(TFBlocks.MINING_FENCE, mining);
		this.generateFenceGate(TFBlocks.MINING_GATE, mining);
		this.generatePressurePlate(TFBlocks.MINING_PLATE, mining);
		this.generateTrapdoor(TFBlocks.MINING_TRAPDOOR, true);
		this.generateDoor(TFBlocks.MINING_DOOR, false);
		this.generateSign(TFBlocks.MINING_SIGN, TFBlocks.MINING_WALL_SIGN, mining);
		this.generateHangingSign(TFBlocks.MINING_HANGING_SIGN, TFBlocks.MINING_WALL_HANGING_SIGN, TFBlocks.STRIPPED_MINING_LOG);
		this.generateBanister(TFBlocks.MINING_BANISTER, mining);
		this.generateDryingRack(TFBlocks.MINING_DRYING_RACK, mining);

		this.woodProvider(TFBlocks.SORTING_LOG).logWithHorizontal(TFBlocks.SORTING_LOG).wood(TFBlocks.SORTING_WOOD);
		this.woodProvider(TFBlocks.STRIPPED_SORTING_LOG).logWithHorizontal(TFBlocks.STRIPPED_SORTING_LOG).wood(TFBlocks.STRIPPED_SORTING_WOOD);
		this.generateTreeCore(TFBlocks.SORTING_LOG, TFBlocks.SORTING_LOG_CORE);
		this.generateHollowLog(TFBlocks.SORTING_LOG, TFBlocks.STRIPPED_SORTING_LOG, TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE);
		this.generateSapling(TFBlocks.SORTING_SAPLING, TFBlocks.POTTED_SORTING_SAPLING, PlantType.NOT_TINTED);
		this.wrapBlockItem(TFBlocks.SORTING_PLANKS, this::createTrivialCube);
		TextureMapping sorting = TextureMapping.cube(TFBlocks.SORTING_PLANKS);
		this.generateStairs(TFBlocks.SORTING_STAIRS, sorting);
		this.generateSlab(TFBlocks.SORTING_SLAB, TFBlocks.SORTING_PLANKS, sorting);
		this.generateButton(TFBlocks.SORTING_BUTTON, sorting);
		this.generateFence(TFBlocks.SORTING_FENCE, sorting);
		this.generateFenceGate(TFBlocks.SORTING_GATE, sorting);
		this.generatePressurePlate(TFBlocks.SORTING_PLATE, sorting);
		this.generateTrapdoor(TFBlocks.SORTING_TRAPDOOR, true);
		this.generateDoor(TFBlocks.SORTING_DOOR, true);
		this.generateSign(TFBlocks.SORTING_SIGN, TFBlocks.SORTING_WALL_SIGN, sorting);
		this.generateHangingSign(TFBlocks.SORTING_HANGING_SIGN, TFBlocks.SORTING_WALL_HANGING_SIGN, TFBlocks.STRIPPED_SORTING_LOG);
		this.generateBanister(TFBlocks.SORTING_BANISTER, sorting);
		this.generateDryingRack(TFBlocks.SORTING_DRYING_RACK, sorting);

		this.generateSapling(TFBlocks.HOLLOW_OAK_SAPLING, TFBlocks.POTTED_HOLLOW_OAK_SAPLING, PlantType.NOT_TINTED);
		this.createTintedLeaves(TFBlocks.RAINBOW_OAK_LEAVES, TexturedModel.createDefault(block -> TextureMapping.cube(Blocks.OAK_LEAVES), ModelTemplates.LEAVES), -12012264);
		this.generateSapling(TFBlocks.RAINBOW_OAK_SAPLING, TFBlocks.POTTED_RAINBOW_OAK_SAPLING, PlantType.NOT_TINTED);

		this.createTFChest(TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.TWILIGHT_OAK_PLANKS, TFCommon.prefix("twilight_oak/normal"));
		this.createTFChest(TFBlocks.CANOPY_CHEST, TFBlocks.CANOPY_PLANKS, TFCommon.prefix("canopy/normal"));
		this.createTFChest(TFBlocks.MANGROVE_CHEST, TFBlocks.MANGROVE_PLANKS, TFCommon.prefix("mangrove/normal"));
		this.createTFChest(TFBlocks.DARK_CHEST, TFBlocks.DARK_PLANKS, TFCommon.prefix("darkwood/normal"));
		this.createTFChest(TFBlocks.TIME_CHEST, TFBlocks.TIME_PLANKS, TFCommon.prefix("time/normal"));
		this.createTFChest(TFBlocks.TRANSFORMATION_CHEST, TFBlocks.TRANSFORMATION_PLANKS, TFCommon.prefix("transformation/normal"));
		this.createTFChest(TFBlocks.MINING_CHEST, TFBlocks.MINING_PLANKS, TFCommon.prefix("mining/normal"));
		this.createTFChest(TFBlocks.SORTING_CHEST, TFBlocks.SORTING_PLANKS, TFCommon.prefix("sorting/normal"));

		this.createTFChest(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.TWILIGHT_OAK_PLANKS, TFCommon.prefix("twilight_oak/trapped"));
		this.createTFChest(TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.CANOPY_PLANKS, TFCommon.prefix("canopy/trapped"));
		this.createTFChest(TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.MANGROVE_PLANKS, TFCommon.prefix("mangrove/trapped"));
		this.createTFChest(TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.DARK_PLANKS, TFCommon.prefix("darkwood/trapped"));
		this.createTFChest(TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TIME_PLANKS, TFCommon.prefix("time/trapped"));
		this.createTFChest(TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_PLANKS, TFCommon.prefix("transformation/trapped"));
		this.createTFChest(TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.MINING_PLANKS, TFCommon.prefix("mining/trapped"));
		this.createTFChest(TFBlocks.SORTING_TRAPPED_CHEST, TFBlocks.SORTING_PLANKS, TFCommon.prefix("sorting/trapped"));

		this.generateHollowLog(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.OAK_BANISTER, TextureMapping.cube(Blocks.OAK_PLANKS));
		this.generateDryingRack(TFBlocks.OAK_DRYING_RACK, TextureMapping.cube(Blocks.OAK_PLANKS));
		this.generateHollowLog(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.SPRUCE_BANISTER, TextureMapping.cube(Blocks.SPRUCE_PLANKS));
		this.generateDryingRack(TFBlocks.SPRUCE_DRYING_RACK, TextureMapping.cube(Blocks.SPRUCE_PLANKS));
		this.generateHollowLog(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.BIRCH_BANISTER, TextureMapping.cube(Blocks.BIRCH_PLANKS));
		this.generateDryingRack(TFBlocks.BIRCH_DRYING_RACK, TextureMapping.cube(Blocks.BIRCH_PLANKS));
		this.generateHollowLog(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.JUNGLE_BANISTER, TextureMapping.cube(Blocks.JUNGLE_PLANKS));
		this.generateDryingRack(TFBlocks.JUNGLE_DRYING_RACK, TextureMapping.cube(Blocks.JUNGLE_PLANKS));
		this.generateHollowLog(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.ACACIA_BANISTER, TextureMapping.cube(Blocks.ACACIA_PLANKS));
		this.generateDryingRack(TFBlocks.ACACIA_DRYING_RACK, TextureMapping.cube(Blocks.ACACIA_PLANKS));
		this.generateHollowLog(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.DARK_OAK_BANISTER, TextureMapping.cube(Blocks.DARK_OAK_PLANKS));
		this.generateDryingRack(TFBlocks.DARK_OAK_DRYING_RACK, TextureMapping.cube(Blocks.DARK_OAK_PLANKS));
		this.generateHollowLog(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE);
		this.generateBanister(TFBlocks.CRIMSON_BANISTER, TextureMapping.cube(Blocks.CRIMSON_PLANKS));
		this.generateDryingRack(TFBlocks.CRIMSON_DRYING_RACK, TextureMapping.cube(Blocks.CRIMSON_PLANKS));
		this.generateHollowLog(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE);
		this.generateBanister(TFBlocks.WARPED_BANISTER, TextureMapping.cube(Blocks.WARPED_PLANKS));
		this.generateDryingRack(TFBlocks.WARPED_DRYING_RACK, TextureMapping.cube(Blocks.WARPED_PLANKS));
		this.generateHollowLog(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.VANGROVE_BANISTER, TextureMapping.cube(Blocks.MANGROVE_PLANKS));
		this.generateDryingRack(TFBlocks.VANGROVE_DRYING_RACK, TextureMapping.cube(Blocks.MANGROVE_PLANKS));
		this.generateHollowLog(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG, TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.CHERRY_BANISTER, TextureMapping.cube(Blocks.CHERRY_PLANKS));
		this.generateDryingRack(TFBlocks.CHERRY_DRYING_RACK, TextureMapping.cube(Blocks.CHERRY_PLANKS));
		this.generateBanister(TFBlocks.BAMBOO_BANISTER, TextureMapping.cube(Blocks.BAMBOO_PLANKS));
		this.generateDryingRack(TFBlocks.BAMBOO_DRYING_RACK, TextureMapping.cube(Blocks.BAMBOO_PLANKS));
		this.generateHollowLog(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG, TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE);
		this.generateBanister(TFBlocks.PALE_OAK_BANISTER, TextureMapping.cube(Blocks.PALE_OAK_PLANKS));
		this.generateDryingRack(TFBlocks.PALE_OAK_DRYING_RACK, TextureMapping.cube(Blocks.PALE_OAK_PLANKS));
	}
}
