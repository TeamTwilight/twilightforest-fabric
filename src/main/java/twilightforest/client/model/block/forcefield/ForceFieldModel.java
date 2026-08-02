package twilightforest.client.model.block.forcefield;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.data.ModelData;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.ForceFieldBlock;
import twilightforest.client.model.block.IDynamicBakedModel;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForceFieldModel implements IDynamicBakedModel, FabricBakedModel {
	private static final FaceBakery FACE_BAKERY = new FaceBakery();
	private static final RenderMaterial EMISSIVE_TRANSLUCENT = RendererAccess.INSTANCE.getRenderer().materialFinder()
			.blendMode(0, BlendMode.TRANSLUCENT)
			.emissive(0, true)
			.disableAo(0, true)
			.find();

	private final Map<BlockElement, ForceFieldModelLoader.Condition> parts;
	private final Function<Material, TextureAtlasSprite> spriteFunction;
	private final IGeometryBakingContext context;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;

	public ForceFieldModel(Map<BlockElement, ForceFieldModelLoader.Condition> parts, Function<Material, TextureAtlasSprite> spriteFunction, IGeometryBakingContext context, ItemOverrides overrides) {
		this.parts = parts;
		this.spriteFunction = spriteFunction;
		this.context = context;
		this.particle = spriteFunction.apply(context.getMaterial("particle"));
		this.overrides = overrides;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
		// This model uses emitBlockQuads for rendering because it requires level/pos context.
		return List.of();
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		Map<ExtraDirection, List<Direction>> map = new HashMap<>();
		for (ExtraDirection extraDirection : getExtraDirections(state, level, pos)) {
			List<Direction> directionList = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				ExtraDirection mirrored = extraDirection.mirrored(dir.getAxis());
				if (mirrored != extraDirection) {
					BlockState other = level.getBlockState(pos.relative(dir));
					if (other.getBlock() instanceof ForceFieldBlock) {
						if (getExtraDirections(other, level, pos.relative(dir)).contains(mirrored)) directionList.add(dir);
					}
				}
			}
			map.put(extraDirection, directionList);
		}
		ForceFieldData data = new ForceFieldData(map);

		QuadEmitter emitter = context.getEmitter();

		for (Direction side : Direction.values()) {
			for (Map.Entry<BlockElement, ForceFieldModelLoader.Condition> entry : this.parts.entrySet()) {
				BlockElementFace blockelementface = entry.getKey().faces.get(side);
				if (blockelementface != null) {
					boolean cull = blockelementface.cullForDirection() != null;
					if (ForceFieldModel.skipRender(data.directions(), entry.getValue().direction(), entry.getValue().b(), entry.getValue().parents(), side)) continue;

					TextureAtlasSprite sprite = this.spriteFunction.apply(this.context.getMaterial(blockelementface.texture()));
					BakedQuad quad = FACE_BAKERY.bakeQuad(
						entry.getKey().from,
						entry.getKey().to,
						blockelementface,
						sprite,
						side,
						BlockModelRotation.X0_Y0,
						null,
						false
					);
					this.emitBakedQuad(emitter, quad);
				}
			}
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		// Force field items use their own plain flat-panel item models
		// (src/main/resources/assets/twilightforest/models/item/*_force_field.json),
		// which do NOT load this model. This baked model only ever renders the in-world
		// block, so there is nothing to emit for the item here.
	}

	private void emitBakedQuad(QuadEmitter emitter, BakedQuad quad) {
		emitter.fromVanilla(quad, EMISSIVE_TRANSLUCENT, quad.getDirection());
		emitter.emit();
	}

	public static boolean skipRender(Map<ExtraDirection, List<Direction>> directions, @Nullable ExtraDirection direction, boolean supposedToBe, List<ExtraDirection> parents, Direction side) {
		if (direction == null) return false;
		for (ExtraDirection parent : parents) if (!directions.containsKey(parent)) return true;
		boolean hasKey = directions.containsKey(direction);
		if (hasKey != supposedToBe) return true;
		if (hasKey) return directions.get(direction).contains(side);
		return false;
	}

	public static List<ExtraDirection> getExtraDirections(BlockState state, BlockGetter level, BlockPos pos) {
		List<ExtraDirection> directions = new ArrayList<>();

		boolean down = state.getValue(ForceFieldBlock.DOWN);
		boolean up = state.getValue(ForceFieldBlock.UP);
		boolean north = state.getValue(ForceFieldBlock.NORTH);
		boolean south = state.getValue(ForceFieldBlock.SOUTH);
		boolean west = state.getValue(ForceFieldBlock.WEST);
		boolean east = state.getValue(ForceFieldBlock.EAST);

		if (down) {
			directions.add(ExtraDirection.DOWN);
			if (north && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.NORTH)) directions.add(ExtraDirection.DOWN_NORTH);
			if (south && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.SOUTH)) directions.add(ExtraDirection.DOWN_SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.WEST)) directions.add(ExtraDirection.DOWN_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.EAST)) directions.add(ExtraDirection.DOWN_EAST);
		}
		if (up) {
			directions.add(ExtraDirection.UP);
			if (north && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.NORTH)) directions.add(ExtraDirection.UP_NORTH);
			if (south && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.SOUTH)) directions.add(ExtraDirection.UP_SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.WEST)) directions.add(ExtraDirection.UP_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.EAST)) directions.add(ExtraDirection.UP_EAST);
		}
		if (north) {
			directions.add(ExtraDirection.NORTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.NORTH, Direction.WEST)) directions.add(ExtraDirection.NORTH_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.NORTH, Direction.EAST)) directions.add(ExtraDirection.NORTH_EAST);
		}
		if (south) {
			directions.add(ExtraDirection.SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.SOUTH, Direction.WEST)) directions.add(ExtraDirection.SOUTH_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.SOUTH, Direction.EAST)) directions.add(ExtraDirection.SOUTH_EAST);
		}
		if (west) directions.add(ExtraDirection.WEST);
		if (east) directions.add(ExtraDirection.EAST);

		return directions;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.context.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.context.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.context.useBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.overrides;
	}

	@NotNull
	@Override
	@SuppressWarnings("deprecation")
	public ItemTransforms getTransforms() {
		return this.context.getTransforms();
	}

	public enum ExtraDirection implements StringRepresentable {
		DOWN("down", 0, 1, 0),
		UP("up", 1, 0, 1),
		NORTH("north", 2, 2, 3),
		SOUTH("south", 3, 3, 2),
		WEST("west", 5, 4, 4),
		EAST("east", 4, 5, 5),

		DOWN_NORTH("down_north", 6, 10, 7),
		DOWN_SOUTH("down_south", 7, 11, 6),
		DOWN_WEST("down_west", 9, 12, 8),
		DOWN_EAST("down_east", 8, 13, 9),

		UP_NORTH("up_north", 10, 6, 11),
		UP_SOUTH("up_south", 11, 7, 10),
		UP_WEST("up_west", 13, 8, 12),
		UP_EAST("up_east", 12, 9, 13),

		NORTH_WEST("north_west", 15, 14, 16),
		NORTH_EAST("north_east", 14, 15, 17),
		SOUTH_WEST("south_west", 17, 16, 14),
		SOUTH_EAST("south_east", 16, 17, 15);

		@SuppressWarnings("deprecation")
		public static final EnumCodec<ExtraDirection> CODEC = StringRepresentable.fromEnum(ExtraDirection::values);
		private final String name;
		private final int xAxisMirror;
		private final int yAxisMirror;
		private final int zAxisMirror;

		ExtraDirection(String name, int xAxisMirror, int yAxisMirror, int zAxisMirror) {
			this.name = name;
			this.xAxisMirror = xAxisMirror;
			this.yAxisMirror = yAxisMirror;
			this.zAxisMirror = zAxisMirror;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public ExtraDirection mirrored(Direction.Axis axis) {
			return switch (axis) {
				case X -> ExtraDirection.values()[this.xAxisMirror];
				case Y -> ExtraDirection.values()[this.yAxisMirror];
				case Z -> ExtraDirection.values()[this.zAxisMirror];
			};
		}

		@Nullable
		public static ExtraDirection byName(@Nullable String name) {
			return CODEC.byName(name);
		}
	}

	public record ForceFieldData(Map<ExtraDirection, List<Direction>> directions) {
	}
}