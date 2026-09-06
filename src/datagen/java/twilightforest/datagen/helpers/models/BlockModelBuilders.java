package twilightforest.datagen.helpers.models;

import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFCommon;
import twilightforest.block.*;
import twilightforest.client.model.item.AnimatedItemModel;
import twilightforest.client.renderer.special.SkullCandleSpecialRenderer;
import twilightforest.client.renderer.special.TrophySpecialRenderer;
import twilightforest.datagen.assets.models.TFModelTemplates;
import twilightforest.datagen.assets.models.TFTextureMapping;
import twilightforest.datagen.assets.models.TFTextureSlot;
import twilightforest.enums.BossVariant;
import twilightforest.enums.NagastoneVariant;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BlockModelBuilders extends WoodBlockBuilders {

	public BlockModelBuilders(Consumer<BlockModelDefinitionGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate) {
		this.generateTrophy(floor, wall, backplate, "template_trophy");
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate, String existingTrophy) {
		MultiVariant template = plainVariant(ModelLocationUtils.decorateBlockModelLocation("skull"));
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		Function<ItemDisplayContext, ItemModel.Unbaked> itemTrophy = ctx -> ItemModelUtils.specialModel(ModelLocationUtils.decorateItemModelLocation("twilightforest:" + existingTrophy), new TrophySpecialRenderer.Unbaked(floor.getVariant(), ctx));

		List<SelectItemModel.SwitchCase<ItemDisplayContext>> cases = new ArrayList<>();
		for (ItemDisplayContext context : ItemDisplayContext.values()) {
			if (context == ItemDisplayContext.GUI) {
				cases.add(new SelectItemModel.SwitchCase<>(List.of(context), ItemModelUtils.composite(backplate, itemTrophy.apply(context))));
			} else {
				cases.add(new SelectItemModel.SwitchCase<>(List.of(context), itemTrophy.apply(context)));
			}
		}

		this.itemModelOutput.accept(floor.asItem(), new AnimatedItemModel.Unbaked(ItemModelUtils.select(new DisplayContext(), cases)), new ClientItem.Properties(true, true, 1.0F));
	}

	public void generateSkullCandle(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall) {
		MultiVariant template = plainVariant(ModelLocationUtils.decorateBlockModelLocation("skull"));
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.specialModel(TFCommon.prefix("item/template_skull_candle"), new SkullCandleSpecialRenderer.Unbaked(floor.getType())));
	}

	public void spawner(Block block, String texture) {
		TextureMapping texturemapping = TextureMapping.cube(new Material(TFCommon.prefix(texture)));
		this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(ModelTemplates.CUBE_ALL_INNER_FACES.create(block, texturemapping, this.modelOutput))));
		this.generateBlockItem(block);
	}

	public void generateGiantBlockItem(Block giantBlock, TextureMapping mapping) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(TFModelTemplates.GIANT_BLOCK_BASE.createWithSuffix(giantBlock, "_item", mapping, this.modelOutput));
		ItemModel.Unbaked gui = ItemModelUtils.plainModel(TFModelTemplates.GIANT_BLOCK_GUI.createWithSuffix(giantBlock, "_gui", mapping, this.modelOutput));
		this.itemModelOutput.accept(giantBlock.asItem(), ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void generateGiantBlockItem(Block giantBlock, TextureMapping mapping, int tint) {
		ItemModel.Unbaked base = ItemModelUtils.tintedModel(TFModelTemplates.GIANT_BLOCK_BASE.createWithSuffix(giantBlock, "_item", mapping, this.modelOutput), ItemModelUtils.constantTint(tint));
		ItemModel.Unbaked gui = ItemModelUtils.tintedModel(TFModelTemplates.GIANT_BLOCK_GUI.createWithSuffix(giantBlock, "_gui", mapping, this.modelOutput), ItemModelUtils.constantTint(tint));
		this.itemModelOutput.accept(giantBlock.asItem(), ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void nagaStone() {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE);

		TextureMapping solidMapping = TextureMapping.cube(TFBlocks.NAGASTONE)
			.put(TextureSlot.SIDE, new Material(TFCommon.prefix("block/nagastone_long_side")))
			.put(TextureSlot.BOTTOM, new Material(TFCommon.prefix("block/nagastone_bottom_long")))
			.put(TextureSlot.TOP, new Material(TFCommon.prefix("block/nagastone_turn_top")));

		Identifier solid = TFModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(TFBlocks.NAGASTONE, "_solid", solidMapping, this.modelOutput);
		// todo 1.21.x cleanup: generate these models as well
		Identifier down = TFCommon.prefix("block/naga_segment/down");
		Identifier up = TFCommon.prefix("block/naga_segment/up");
		Identifier horizontal = TFCommon.prefix("block/naga_segment/horizontal");
		Identifier vertical = TFCommon.prefix("block/naga_segment/vertical");

		this.itemModelOutput.accept(TFBlocks.NAGASTONE.asItem(), ItemModelUtils.plainModel(solid));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.NAGASTONE).with(
			PropertyDispatch.initial(NagastoneBlock.VARIANT)
				.select(NagastoneVariant.NORTH_DOWN, plainVariant(down).with(Y_ROT_270))
				.select(NagastoneVariant.SOUTH_DOWN, plainVariant(down).with(Y_ROT_90))
				.select(NagastoneVariant.WEST_DOWN, plainVariant(down).with(Y_ROT_180))
				.select(NagastoneVariant.EAST_DOWN, plainVariant(down))

				.select(NagastoneVariant.NORTH_UP, plainVariant(up).with(Y_ROT_270))
				.select(NagastoneVariant.SOUTH_UP, plainVariant(up).with(Y_ROT_90))
				.select(NagastoneVariant.WEST_UP, plainVariant(up).with(Y_ROT_180))
				.select(NagastoneVariant.EAST_UP, plainVariant(up))

				.select(NagastoneVariant.AXIS_X, plainVariant(horizontal))
				.select(NagastoneVariant.AXIS_Y, plainVariant(vertical))
				.select(NagastoneVariant.AXIS_Z, plainVariant(horizontal).with(Y_ROT_90))
				.select(NagastoneVariant.SOLID, plainVariant(solid))
		));

		TextureMapping faceMapping = TextureMapping.cube(TFBlocks.NAGASTONE_HEAD)
			.put(TextureSlot.UP, new Material(TFCommon.prefix("block/nagastone_top_tip")))
			.put(TextureSlot.DOWN, new Material(TFCommon.prefix("block/nagastone_bottom_tip")))
			.put(TextureSlot.SOUTH, new Material(TFCommon.prefix("block/nagastone_face_left")))
			.put(TextureSlot.NORTH, new Material(TFCommon.prefix("block/nagastone_face_right")))
			.put(TextureSlot.WEST, new Material(TFCommon.prefix("block/nagastone_face_front")))
			.put(TextureSlot.EAST, new Material(TFCommon.prefix("block/nagastone_cross_section")))
			.put(TextureSlot.PARTICLE, new Material(TFCommon.prefix("block/nagastone_face_front")));
		Identifier model = TFModelTemplates.CUBE.create(TFBlocks.NAGASTONE_HEAD, faceMapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.NAGASTONE_HEAD, plainVariant(model)).with(ROTATION_HORIZONTAL_FACING));
		this.itemModelOutput.accept(TFBlocks.NAGASTONE_HEAD.asItem(), ItemModelUtils.plainModel(model));

		nagastonePillar(TFBlocks.NAGASTONE_PILLAR, "");
		nagastonePillar(TFBlocks.MOSSY_NAGASTONE_PILLAR, "_mossy");
		nagastonePillar(TFBlocks.CRACKED_NAGASTONE_PILLAR, "_weathered");
		etchedNagastone(TFBlocks.ETCHED_NAGASTONE, "");
		etchedNagastone(TFBlocks.MOSSY_ETCHED_NAGASTONE, "_mossy");
		etchedNagastone(TFBlocks.CRACKED_ETCHED_NAGASTONE, "_weathered");

		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_LEFT, ModelLocationUtils.decorateBlockModelLocation("block/etched_nagastone_left"), TFCommon.prefix("block/stone_tiles"), TFCommon.prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT, TFCommon.prefix("block/etched_nagastone_right"), TFCommon.prefix("block/stone_tiles"), TFCommon.prefix("block/nagastone_bare"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFCommon.prefix("block/etched_nagastone_left_mossy"), TFCommon.prefix("block/stone_tiles_mossy"), TFCommon.prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFCommon.prefix("block/etched_nagastone_right_mossy"), TFCommon.prefix("block/stone_tiles_mossy"), TFCommon.prefix("block/nagastone_bare_mossy"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFCommon.prefix("block/etched_nagastone_left_weathered"), TFCommon.prefix("block/stone_tiles_weathered"), TFCommon.prefix("block/nagastone_bare_weathered"));
		bisectedStairsBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFCommon.prefix("block/etched_nagastone_right_weathered"), TFCommon.prefix("block/stone_tiles_weathered"), TFCommon.prefix("block/nagastone_bare_weathered"));
	}

	private void nagastonePillar(Block block, String suffix) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE)
			.put(TextureSlot.END, new Material(TFCommon.prefix("block/nagastone_pillar_end" + suffix)))
			.put(TextureSlot.SIDE, new Material(TFCommon.prefix("block/nagastone_pillar_side" + suffix)));
		Identifier model = TFModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);

		TextureMapping altMapping = TextureMapping.cube(TFBlocks.NAGASTONE)
			.put(TextureSlot.END, new Material(TFCommon.prefix("block/nagastone_pillar_end" + suffix)))
			.put(TextureSlot.SIDE, new Material(TFCommon.prefix("block/nagastone_pillar_side" + suffix + "_alt")));
		Identifier reversed = TFModelTemplates.CUBE_COLUMN.createWithSuffix(block, "_alt", altMapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(RotatedPillarBlock.AXIS, DirectionalRotatedPillarBlock.REVERSED)
				.select(Direction.Axis.X, true, plainVariant(reversed).with(X_ROT_270).with(Y_ROT_270))
				.select(Direction.Axis.Y, true, plainVariant(reversed))
				.select(Direction.Axis.Z, true, plainVariant(reversed).with(X_ROT_270))

				.select(Direction.Axis.X, false, plainVariant(model).with(X_ROT_270).with(Y_ROT_270))
				.select(Direction.Axis.Y, false, plainVariant(model))
				.select(Direction.Axis.Z, false, plainVariant(model).with(X_ROT_270))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
	}

	private void etchedNagastone(Block block, String suffix) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.NAGASTONE)
			.put(TextureSlot.END, new Material(TFCommon.prefix("block/stone_tiles" + suffix)))
			.put(TextureSlot.SIDE, new Material(TFCommon.prefix("block/etched_nagastone_up" + suffix)))
			.put(TextureSlot.PARTICLE, new Material(TFCommon.prefix("block/stone_tiles" + suffix)));
		Identifier model = ModelTemplates.CUBE_COLUMN.create(block, mapping, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(DirectionalBlock.FACING)
				.select(Direction.UP, plainVariant(model))
				.select(Direction.DOWN, plainVariant(model).with(X_ROT_180))
				.select(Direction.SOUTH, plainVariant(model).with(X_ROT_270))
				.select(Direction.NORTH, plainVariant(model).with(X_ROT_90))
				.select(Direction.WEST, plainVariant(model).with(Y_ROT_90).with(X_ROT_270))
				.select(Direction.EAST, plainVariant(model).with(Y_ROT_90).with(X_ROT_90))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
	}

	protected void bisectedStairsBlock(Block block, Material side, Material end, Material middle) {
		TextureMapping mapping = TextureMapping.cube(block)
			.put(TextureSlot.END, end)
			.put(TextureSlot.SIDE, side)
			.put(TFTextureSlot.MIDDLE, middle)
			.put(TextureSlot.PARTICLE, middle);

		MultiVariant inner = plainVariant(TFModelTemplates.BISECTED_STAIRS_INNER.createWithSuffix(block, "_inner", mapping, this.modelOutput));
		Identifier straight = TFModelTemplates.BISECTED_STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		MultiVariant outer = plainVariant(TFModelTemplates.BISECTED_STAIRS_OUTER.createWithSuffix(block, "_outer", mapping, this.modelOutput));
		this.blockStateOutput.accept(createStairs(block, inner, plainVariant(straight), outer));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(straight));
	}

	protected void bisectedStairsBlock(Block block, Identifier side, Identifier end, Identifier middle) {
		this.bisectedStairsBlock(block, new Material(side), new Material(end), new Material(middle));
	}

	public void stonePillar() {
		Identifier base = TFCommon.prefix("block/pillar/pillar_base");
		Identifier up = TFCommon.prefix("block/pillar/pillar_up");
		Identifier down = TFCommon.prefix("block/pillar/pillar_down");
		Identifier top = TFCommon.prefix("block/pillar/pillar_top");
		Identifier bottom = TFCommon.prefix("block/pillar/pillar_bottom");

		this.itemModelOutput.accept(TFBlocks.TWISTED_STONE_PILLAR.asItem(), ItemModelUtils.plainModel(TFCommon.prefix("block/pillar/pillar_inventory")));

		this.blockStateOutput.accept(
			MultiPartGenerator.multiPart(TFBlocks.TWISTED_STONE_PILLAR)
				// X
				.with(
					condition(WallPillarBlock.AXIS, Direction.Axis.X),
					plainVariant(base).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X), condition(PipeBlock.EAST, false)),
					plainVariant(top).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X), condition(PipeBlock.WEST, false)),
					plainVariant(bottom).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z), condition(PipeBlock.EAST, true)),
					plainVariant(up).with(X_ROT_90).with(Y_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y, Direction.Axis.Z), condition(PipeBlock.WEST, true)),
					plainVariant(down).with(X_ROT_90).with(Y_ROT_90)
				)

				// Y
				.with(
					condition(WallPillarBlock.AXIS, Direction.Axis.Y),
					plainVariant(base)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y), condition(PipeBlock.UP, false)),
					plainVariant(top)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Y), condition(PipeBlock.DOWN, false)),
					plainVariant(bottom)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Z), condition(PipeBlock.UP, true)),
					plainVariant(up)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Z), condition(PipeBlock.DOWN, true)),
					plainVariant(down)
				)

				// Z
				.with(
					condition(WallPillarBlock.AXIS, Direction.Axis.Z),
					plainVariant(base).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Z), condition(PipeBlock.NORTH, false)),
					plainVariant(top).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.Z), condition(PipeBlock.SOUTH, false)),
					plainVariant(bottom).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Y), condition(PipeBlock.NORTH, true)),
					plainVariant(up).with(X_ROT_90)
				)
				.with(
					and(condition(WallPillarBlock.AXIS, Direction.Axis.X, Direction.Axis.Y), condition(PipeBlock.SOUTH, true)),
					plainVariant(down).with(X_ROT_90)
				)
		);
	}

	public void wroughtIronFence() {
		Block block = TFBlocks.WROUGHT_IRON_FENCE;
		Identifier post = ModelLocationUtils.getModelLocation(block, "_post");
		Identifier capped = ModelLocationUtils.getModelLocation(block, "_post_capped");
		Identifier full = ModelLocationUtils.getModelLocation(block, "_full");
		Identifier top = ModelLocationUtils.getModelLocation(block, "_top");
		Identifier middle = ModelLocationUtils.getModelLocation(block, "_middle");
		Identifier bottom = ModelLocationUtils.getModelLocation(block, "_bottom");

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
			.with(condition(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.POST), plainVariant(post))
			.with(condition(WroughtIronFenceBlock.POST, WroughtIronFenceBlock.PostState.CAPPED), plainVariant(capped))

			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full))
			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top))
			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle))
			.with(condition(WroughtIronFenceBlock.NORTH_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom))

			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full).with(Y_ROT_90))
			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top).with(Y_ROT_90))
			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle).with(Y_ROT_90))
			.with(condition(WroughtIronFenceBlock.EAST_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom).with(Y_ROT_90))

			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full).with(Y_ROT_180))
			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top).with(Y_ROT_180))
			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle).with(Y_ROT_180))
			.with(condition(WroughtIronFenceBlock.SOUTH_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom).with(Y_ROT_180))

			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.FULL), plainVariant(full).with(Y_ROT_270))
			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.TOP), plainVariant(top).with(Y_ROT_270))
			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.MIDDLE), plainVariant(middle).with(Y_ROT_270))
			.with(condition(WroughtIronFenceBlock.WEST_FENCE, WroughtIronFenceBlock.FenceSide.BOTTOM), plainVariant(bottom).with(Y_ROT_270))
		);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(block.asItem())));
	}

	public void terrorcotta() {
		this.rotationallySpecialColumn(TFBlocks.TERRORCOTTA_ARCS);

		PropertyDispatch.C1<MultiVariant, Direction> directionDispach = PropertyDispatch.initial(GlazedTerracottaBlock.FACING);
		boolean firstCurve = true;
		for (Direction direction : new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST}) {
			Identifier location = this.makeTerrorcottaCurvesModel("terrorcotta_curves", direction.get2DDataValue());
			directionDispach = directionDispach.select(direction, plainVariant(location));
			if (firstCurve) {
				this.itemModelOutput.accept(TFBlocks.TERRORCOTTA_CURVES.asItem(), ItemModelUtils.plainModel(location));
				firstCurve = false;
			}
		}
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.TERRORCOTTA_CURVES).with(directionDispach));

		Identifier rotated = this.makeTerrorcottaLinesModel("terrorcotta_lines", true);
		Identifier unRotated = this.makeTerrorcottaLinesModel("terrorcotta_lines", false);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(TFBlocks.TERRORCOTTA_LINES).with(
			PropertyDispatch.initial(BinaryRotatedBlock.ROTATED)
				.select(true, plainVariant(rotated))
				.select(false, plainVariant(unRotated))
		));

		this.itemModelOutput.accept(TFBlocks.TERRORCOTTA_LINES.asItem(), ItemModelUtils.plainModel(rotated));
	}

	private Identifier makeTerrorcottaCurvesModel(String type, int rotation) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.TERRORCOTTA_CURVES)
			.put(TextureSlot.UP, new Material(TFCommon.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.UP))))
			.put(TextureSlot.DOWN, new Material(TFCommon.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.DOWN))))
			.put(TextureSlot.SOUTH, new Material(TFCommon.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.SOUTH))))
			.put(TextureSlot.NORTH, new Material(TFCommon.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.NORTH))))
			.put(TextureSlot.WEST, new Material(TFCommon.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.WEST))))
			.put(TextureSlot.EAST, new Material(TFCommon.prefix("block/" + type + curvesSuffixForFacing(rotation, Direction.EAST))))
			.put(TextureSlot.PARTICLE, new Material(TFCommon.prefix("block/" + type + "_a")));

		return TFModelTemplates.CUBE.create(TFCommon.prefix("block/" + type + "_" + (rotation * 90)), mapping, this.modelOutput);
	}

	@NotNull
	private static String curvesSuffixForFacing(int blockRotation, Direction blockFace) {
		int rotationForFace = switch (blockFace) {
			case UP -> 2 - blockRotation;
			case DOWN -> 3 + blockRotation;
			case SOUTH -> switch (blockRotation) {
				case 3 -> 0;
				case 2 -> 3;
				case 1 -> 1;
				default -> 2;
			};
			case WEST -> switch (blockRotation) {
				case 3 -> 1;
				case 2 -> 3;
				case 1 -> 0;
				default -> 2;
			};
			case NORTH -> switch (blockRotation) {
				case 3 -> 3;
				case 2 -> 0;
				case 1 -> 2;
				default -> 1;
			};
			case EAST -> switch (blockRotation) {
				case 3 -> 2;
				case 2 -> 0;
				case 1 -> 3;
				default -> 1;
			};
		};

		return switch (Math.floorMod(rotationForFace, 4)) {
			case 3 -> "_d";
			case 2 -> "_c";
			case 1 -> "_b";
			default -> "_a";
		};
	}

	private Identifier makeTerrorcottaLinesModel(String type, boolean rotated) {
		TextureMapping mapping = TextureMapping.cube(TFBlocks.TERRORCOTTA_CURVES)
			.put(TextureSlot.UP, new Material(TFCommon.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.UP))))
			.put(TextureSlot.DOWN, new Material(TFCommon.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.DOWN))))
			.put(TextureSlot.SOUTH, new Material(TFCommon.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.SOUTH))))
			.put(TextureSlot.NORTH, new Material(TFCommon.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.NORTH))))
			.put(TextureSlot.WEST, new Material(TFCommon.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.WEST))))
			.put(TextureSlot.EAST, new Material(TFCommon.prefix("block/" + type + linesSuffixForFacing(rotated, Direction.EAST))))
			.put(TextureSlot.PARTICLE, new Material(TFCommon.prefix("block/" + type + "_a")));

		return TFModelTemplates.CUBE.create(TFCommon.prefix("block/" + type + "_" + (rotated ? 90 : 0)), mapping, this.modelOutput);
	}

	@NotNull
	private static String linesSuffixForFacing(boolean blockRotation, Direction blockFace) {
		Vec3i normal = blockFace.getUnitVec3i();
		int axisDirection = normal.getX() + normal.getY() + normal.getZ();
		// Biblically accurate XOR
		return axisDirection > 0 == ((blockFace.getAxis() == Direction.Axis.Z) != blockRotation) ? "_a" : "_b";
	}

	public void rotationallySpecialColumn(Block block) {
		Material sideA = TextureMapping.getBlockTexture(block, "_side_a");
		Material sideB = TextureMapping.getBlockTexture(block, "_side_b");
		Material end = TextureMapping.getBlockTexture(block, "_end");

		Identifier xModel = TFModelTemplates.CUBE_COLUMN_ROTATIONALLY_SPECIAL_X.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TFTextureSlot.SIDE_A, sideA).put(TFTextureSlot.SIDE_B, sideB), this.modelOutput);
		Identifier yModel = TFModelTemplates.CUBE_COLUMN.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TextureSlot.SIDE, sideA), this.modelOutput);
		Identifier zModel = TFModelTemplates.CUBE_COLUMN_ROTATIONALLY_SPECIAL_Z.create(block, TextureMapping.cube(block).put(TextureSlot.END, end).put(TFTextureSlot.SIDE_A, sideA).put(TFTextureSlot.SIDE_B, sideB), this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(RotatedPillarBlock.AXIS)
				.select(Direction.Axis.X, plainVariant(xModel))
				.select(Direction.Axis.Y, plainVariant(yModel))
				.select(Direction.Axis.Z, plainVariant(zModel))
		));
		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(yModel));
	}

	public void directionalCrossModel(Block block, PlantType type) {
		Identifier identifier = type.getCross().create(block, TextureMapping.cross(block), this.modelOutput);

		this.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(this.createFlatItemModelWithBlockTexture(block.asItem(), block)));
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
			PropertyDispatch.initial(DirectionalBlock.FACING)
				.select(Direction.UP, plainVariant(identifier))
				.select(Direction.DOWN, plainVariant(identifier).with(X_ROT_180))
				.select(Direction.SOUTH, plainVariant(identifier).with(X_ROT_270))
				.select(Direction.NORTH, plainVariant(identifier).with(X_ROT_90))
				.select(Direction.WEST, plainVariant(identifier).with(Y_ROT_90).with(X_ROT_270))
				.select(Direction.EAST, plainVariant(identifier).with(Y_ROT_90).with(X_ROT_90))
		));
	}

	public void generatePaneBlock(Block glassBlock, Block paneBlock) {
		TextureMapping mapping = new TextureMapping().put(TextureSlot.PANE, TextureMapping.getBlockTexture(glassBlock)).put(TextureSlot.EDGE, TextureMapping.getBlockTexture(glassBlock));
		Identifier post = ModelTemplates.STAINED_GLASS_PANE_POST.create(paneBlock, mapping, this.modelOutput);
		Identifier side = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(paneBlock, mapping, this.modelOutput);
		Identifier sideAlt = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(paneBlock, mapping, this.modelOutput);
		Identifier noSide = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(paneBlock, mapping, this.modelOutput);
		Identifier noSideAlt = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(paneBlock, mapping, this.modelOutput);
		Item item = paneBlock.asItem();
		this.registerSimpleItemModel(item, this.createFlatItemModelWithBlockTexture(item, glassBlock));
		this.blockStateOutput
			.accept(
				MultiPartGenerator.multiPart(paneBlock)
					.with(plainVariant(post))
					.with(condition(BlockStateProperties.NORTH, true), plainVariant(side))
					.with(
						condition(BlockStateProperties.EAST, true),
						plainVariant(side).with(Y_ROT_90)
					)
					.with(condition(BlockStateProperties.SOUTH, true), plainVariant(sideAlt))
					.with(
						condition(BlockStateProperties.WEST, true),
						plainVariant(sideAlt).with(Y_ROT_90)
					)
					.with(condition(BlockStateProperties.NORTH, false), plainVariant(noSide))
					.with(condition(BlockStateProperties.EAST, false), plainVariant(noSideAlt))
					.with(
						condition(BlockStateProperties.SOUTH, false),
						plainVariant(noSideAlt).with(Y_ROT_90)
					)
					.with(
						condition(BlockStateProperties.WEST, false),
						plainVariant(noSide).with(Y_ROT_270)
					)
			);
	}

	public void generateRuneBlock(Block runeBlock, int tint) {
		Variant[] variants = new Variant[8];
		for (int i = 0; i < 8; i++) {
			variants[i] = plainModel(TFModelTemplates.CASTLE_RUNE_TEMPLATE.createWithSuffix(runeBlock, "_" + i, TextureMapping.cube(TFBlocks.CASTLE_BRICK).put(TFTextureSlot.RUNE, new Material(TFCommon.prefix("block/castleblock_magic_" + i))), this.modelOutput));
		}
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(runeBlock, variants(variants)));
		this.itemModelOutput.accept(runeBlock.asItem(), ItemModelUtils.tintedModel(ModelLocationUtils.getModelLocation(runeBlock).withSuffix("_0"), ItemModelUtils.constantTint(tint)));
	}

	public void trophyPedestal() {
		BossVariant[][] variantList = new BossVariant[][]{ //face order: north, south, east, west
			new BossVariant[]{BossVariant.NAGA, BossVariant.LICH, BossVariant.UR_GHAST, BossVariant.HYDRA},
			new BossVariant[]{BossVariant.SNOW_QUEEN, BossVariant.NAGA, BossVariant.HYDRA, BossVariant.LICH},
			new BossVariant[]{BossVariant.UR_GHAST, BossVariant.SNOW_QUEEN, BossVariant.LICH, BossVariant.NAGA},
			new BossVariant[]{BossVariant.HYDRA, BossVariant.UR_GHAST, BossVariant.NAGA, BossVariant.SNOW_QUEEN},
			new BossVariant[]{BossVariant.LICH, BossVariant.HYDRA, BossVariant.SNOW_QUEEN, BossVariant.UR_GHAST}
		};
		Block pedestal = TFBlocks.TROPHY_PEDESTAL;
		List<Variant> variants = new ArrayList<>();
		List<Variant> activeVariants = new ArrayList<>();
		for (int i = 0; i < variantList.length; i++) {
			Identifier model = TFModelTemplates.TROPHY_PEDESTAL.createWithSuffix(pedestal, i == 0 ? "" : ("_" + i), TFTextureMapping.trophyPedestal(pedestal, false, variantList[i][0], variantList[i][1], variantList[i][2], variantList[i][3]), this.modelOutput);
			Identifier activeModel = TFModelTemplates.TROPHY_PEDESTAL_ACTIVE.createWithSuffix(pedestal, i == 0 ? "_active" : ("_active_" + i), TFTextureMapping.trophyPedestal(pedestal, true, variantList[i][0], variantList[i][1], variantList[i][2], variantList[i][3]), this.modelOutput);

			for (Quadrant rot : Quadrant.values()) {
				variants.add(plainModel(model).with(VariantMutator.Y_ROT.withValue(rot)));
				activeVariants.add(plainModel(activeModel).with(VariantMutator.Y_ROT.withValue(rot)));
			}
		}
		this.blockStateOutput.accept(MultiVariantGenerator.dispatch(pedestal).with(PropertyDispatch.initial(TrophyPedestalBlock.ACTIVE).select(true, variants(activeVariants.toArray(new Variant[0]))).select(false, variants(variants.toArray(new Variant[0])))));
		this.generateBlockItem(pedestal);
	}

	public void ironLadder() {
		MultiPartGenerator model = MultiPartGenerator.multiPart(TFBlocks.IRON_LADDER);
		Identifier left = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER, "_left");
		Identifier leftConnect = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER, "_left_connection");
		Identifier right = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER, "_right");
		Identifier rightConnect = ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER, "_right_connection");
		forEachHorizontalDirection((d, mutator) -> {
			model.with(and(
				condition(IronLadderBlock.LEFT, false),
				condition(LadderBlock.FACING, d)), plainVariant(left).with(mutator));
			model.with(and(
				condition(IronLadderBlock.LEFT, true),
				condition(LadderBlock.FACING, d)), plainVariant(leftConnect).with(mutator));
			model.with(and(
				condition(IronLadderBlock.RIGHT, false),
				condition(LadderBlock.FACING, d)), plainVariant(right).with(mutator));
			model.with(and(
				condition(IronLadderBlock.RIGHT, true),
				condition(LadderBlock.FACING, d)), plainVariant(rightConnect).with(mutator));
		});
		this.blockStateOutput.accept(model);
		this.itemModelOutput.accept(TFBlocks.IRON_LADDER.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.IRON_LADDER.asItem())));
	}

	public void createMultifaceBlock(Block mushroomBlock, Identifier inside, boolean invertConditions) {
		Identifier outside = ModelTemplates.SINGLE_FACE.create(mushroomBlock, TextureMapping.defaultTexture(mushroomBlock), this.modelOutput);
		this.blockStateOutput.accept(MultiPartGenerator.multiPart(mushroomBlock)
			.with(condition(BlockStateProperties.NORTH, !invertConditions), plainVariant(outside))
			.with(condition(BlockStateProperties.EAST, !invertConditions), plainVariant(outside).with(Y_ROT_90).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.SOUTH, !invertConditions), plainVariant(outside).with(Y_ROT_180).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.WEST, !invertConditions), plainVariant(outside).with(Y_ROT_270).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.UP, !invertConditions), plainVariant(outside).with(X_ROT_270).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.DOWN, !invertConditions), plainVariant(outside).with(X_ROT_90).with(VariantMutator.UV_LOCK.withValue(true)))
			.with(condition(BlockStateProperties.NORTH, invertConditions), plainVariant(inside))
			.with(condition(BlockStateProperties.EAST, invertConditions), plainVariant(inside).with(Y_ROT_90).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.SOUTH, invertConditions), plainVariant(inside).with(Y_ROT_180).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.WEST, invertConditions), plainVariant(inside).with(Y_ROT_270).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.UP, invertConditions), plainVariant(inside).with(X_ROT_270).with(VariantMutator.UV_LOCK.withValue(false)))
			.with(condition(BlockStateProperties.DOWN, invertConditions), plainVariant(inside).with(X_ROT_90).with(VariantMutator.UV_LOCK.withValue(false)))
		);
		this.registerSimpleItemModel(mushroomBlock, TexturedModel.CUBE.createWithSuffix(mushroomBlock, "_inventory", this.modelOutput));
	}

	public void createTFChest(Block chestBlock, Block particleBlock, Identifier texture) {
		this.createParticleOnlyBlock(chestBlock, particleBlock);
		Item item = chestBlock.asItem();
		this.itemModelOutput.accept(item, ItemModelUtils.specialModel(ModelTemplates.CHEST_INVENTORY.create(item, TextureMapping.particle(particleBlock), this.modelOutput), new ChestSpecialRenderer.Unbaked(texture)));
	}
}
