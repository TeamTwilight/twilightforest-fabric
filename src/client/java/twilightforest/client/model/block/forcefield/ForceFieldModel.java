package twilightforest.client.model.block.forcefield;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
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
import org.jetbrains.annotations.Nullable;
import twilightforest.block.ForceFieldBlock;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForceFieldModel implements BakedModel, FabricBakedModel {
	private static final FaceBakery FACE_BAKERY = new FaceBakery();

	private final Map<BlockElement, ForceFieldModelLoader.Condition> parts;
	private final Map<String, Material> materials;
	private final Function<Material, TextureAtlasSprite> spriteGetter;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;

	public ForceFieldModel(Map<BlockElement, ForceFieldModelLoader.Condition> parts, Map<String, Material> materials, Function<Material, TextureAtlasSprite> spriteGetter, ItemOverrides overrides, ItemTransforms transforms) {
		this.parts = parts;
		this.materials = materials;
		this.spriteGetter = spriteGetter;
		this.particle = spriteGetter.apply(materials.getOrDefault("particle", materials.get("pane")));
		this.overrides = overrides;
		this.transforms = transforms;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().find();
		ForceFieldData data = buildData(state, level, pos);
		for (Direction side : Direction.values()) {
			this.emitQuads(context, material, side, data, false, null);
			this.emitQuads(context, material, side, data, true, side);
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().find();
		ForceFieldData data = itemData();
		for (Direction side : Direction.values()) {
			this.emitQuads(context, material, side, data, false, null);
			this.emitQuads(context, material, side, data, true, side);
		}
	}

	private void emitQuads(RenderContext context, RenderMaterial material, Direction side, ForceFieldData data, boolean cull, @Nullable Direction cullFace) {
		for (BakedQuad quad : this.getQuads(new ArrayList<>(), side, data, cull)) {
			context.getEmitter().fromVanilla(quad, material, cullFace).emit();
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
		List<BakedQuad> quads = new ArrayList<>();
		ForceFieldData data = itemData();
		if (side == null) {
			for (Direction direction : Direction.values()) {
				this.getQuads(quads, direction, data, false);
			}
		} else {
			this.getQuads(quads, side, data, true);
		}
		return quads;
	}

	public List<BakedQuad> getQuads(List<BakedQuad> quads, Direction side, ForceFieldData data, boolean cull) {
		for (Map.Entry<BlockElement, ForceFieldModelLoader.Condition> entry : this.parts.entrySet()) {
			BlockElementFace face = entry.getKey().faces.get(side);
			if (face != null && (face.cullForDirection() != null) == cull) {
				ForceFieldModelLoader.Condition condition = entry.getValue();
				if (skipRender(data.directions(), condition.direction(), condition.b(), condition.parents(), side)) {
					continue;
				}
				quads.add(FACE_BAKERY.bakeQuad(
						entry.getKey().from,
						entry.getKey().to,
						face,
						this.sprite(face.texture()),
						side,
						BlockModelRotation.X0_Y0,
						entry.getKey().rotation,
						entry.getKey().shade));
			}
		}
		return quads;
	}

	private TextureAtlasSprite sprite(String textureName) {
		String key = textureName.startsWith("#") ? textureName.substring(1) : textureName;
		Material material = this.materials.getOrDefault(key, this.materials.get("pane"));
		return this.spriteGetter.apply(material);
	}

	protected static boolean skipRender(Map<ExtraDirection, List<Direction>> directions, @Nullable ExtraDirection direction, boolean supposedToBe, List<ExtraDirection> parents, Direction side) {
		if (direction == null) {
			return false;
		}
		for (ExtraDirection parent : parents) {
			if (!directions.containsKey(parent)) {
				return true;
			}
		}
		boolean hasKey = directions.containsKey(direction);
		if (hasKey != supposedToBe) {
			return true;
		}
		return hasKey && directions.get(direction).contains(side);
	}

	private static ForceFieldData buildData(BlockState state, BlockGetter level, BlockPos pos) {
		Map<ExtraDirection, List<Direction>> map = new EnumMap<>(ExtraDirection.class);
		for (ExtraDirection extraDirection : getExtraDirections(state, level, pos)) {
			List<Direction> directionList = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				ExtraDirection mirrored = extraDirection.mirrored(dir.getAxis());
				if (mirrored != extraDirection) {
					BlockPos neighborPos = pos.relative(dir);
					BlockState other = level.getBlockState(neighborPos);
					if (other.getBlock() instanceof ForceFieldBlock && getExtraDirections(other, level, neighborPos).contains(mirrored)) {
						directionList.add(dir);
					}
				}
			}
			map.put(extraDirection, directionList);
		}
		return new ForceFieldData(map);
	}

	private static ForceFieldData itemData() {
		Map<ExtraDirection, List<Direction>> map = new EnumMap<>(ExtraDirection.class);
		for (ExtraDirection direction : ExtraDirection.values()) {
			map.put(direction, List.of());
		}
		return new ForceFieldData(map);
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
		return false;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
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

	@Override
	public ItemTransforms getTransforms() {
		return this.transforms;
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
