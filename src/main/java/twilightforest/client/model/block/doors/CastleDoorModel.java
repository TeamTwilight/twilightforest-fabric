package twilightforest.client.model.block.doors;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class CastleDoorModel implements BakedModel, FabricBakedModel {
	private static final RenderMaterial MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).emissive(0, true).find();
	@Nullable
	private final Mesh[] baseQuads;
	private final BakedQuad[][][] quads;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;
	//if we ever expand this model to be more flexible, I think we'll need a list of blocks that can connect together defined in the json instead of hardcoding this (tags may be nice for this)
	private final Block[] validConnectors = {TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.YELLOW_CASTLE_DOOR.get(), TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get()};

	public CastleDoorModel(@Nullable Mesh[] baseQuads, BakedQuad[][][] quads, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms) {
		this.baseQuads = baseQuads;
		this.quads = quads;
		this.particle = particle;
		this.overrides = overrides;
		this.transforms = transforms;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		QuadEmitter emitter = context.getEmitter();

		for (Direction side : Direction.values()) {
			int faceIndex = side.get3DDataValue();
			CastleDoorData data = getModelData(blockView, pos);
			context.meshConsumer().accept(this.baseQuads[faceIndex]);

			for (int quad = 0; quad < 4; ++quad) {
				//if our model data is null (I really hope it isn't) we can skip connected textures since we dont have the info we need
				//i'd rather do this than crash the game or skip rendering the block entirely
				ConnectionLogic connectionType = data.logic[faceIndex][quad];
				emitter.fromVanilla(this.quads[faceIndex][quad][connectionType.ordinal()], MATERIAL, side);
				emitter.emit();
			}
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		QuadEmitter emitter = context.getEmitter();

		for (Direction side : Direction.values()) {
			int faceIndex = side.get3DDataValue();
			context.meshConsumer().accept(this.baseQuads[faceIndex]);

			for (int quad = 0; quad < 4; ++quad) {
				ConnectionLogic connectionType = ConnectionLogic.NONE;
				emitter.fromVanilla(this.quads[faceIndex][quad][connectionType.ordinal()], MATERIAL, side);
				emitter.emit();
			}
		}
	}

	@NotNull
	public CastleDoorData getModelData(@NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
		CastleDoorData data = new CastleDoorData();

		for (Direction face : Direction.values()) {
			Direction[] directions = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
			boolean[] sideStates = new boolean[4];

			int faceIndex;
			for (faceIndex = 0; faceIndex < directions.length; faceIndex++) {
				sideStates[faceIndex] = this.shouldConnectSide(getter, pos, face, directions[faceIndex]);
			}

			faceIndex = face.get3DDataValue();

			for (int dir = 0; dir < directions.length; dir++) {
				int cornerOffset = (dir + 1) % directions.length;
				boolean side1 = sideStates[dir];
				boolean side2 = sideStates[cornerOffset];
				boolean corner = side1 && side2 && this.isCornerBlockPresent(getter, pos, face, directions[dir], directions[cornerOffset]);
				data.logic[faceIndex][dir] = dir % 2 == 0 ? ConnectionLogic.of(side1, side2, corner) : ConnectionLogic.of(side2, side1, corner);
			}
		}

		return data;
	}

	private boolean shouldConnectSide(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side) {
		BlockState neighborState = getter.getBlockState(pos.relative(side));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(neighborState, getter, pos, face, pos.relative(face));
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(neighborState, getter, pos, face, pos.relative(face));
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
		return List.of();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@NotNull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

	@NotNull
	@Override
	public ItemOverrides getOverrides() {
		return this.overrides;
	}

	@NotNull
	@Override
	public ItemTransforms getTransforms() {
		return this.transforms;
	}

	//we need a class to make model data. Fine, here you go
	public static final class CastleDoorData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private CastleDoorData() {
		}
	}
}