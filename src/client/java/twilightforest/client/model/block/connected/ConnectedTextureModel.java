package twilightforest.client.model.block.connected;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class ConnectedTextureModel implements net.minecraft.client.resources.model.BakedModel, FabricBakedModel {
	private final EnumSet<Direction> enabledFaces;
	private final boolean renderOnDisabledFaces;
	@Nullable
	private final List<BakedQuad>[] baseQuads;
	private final BakedQuad[][][] quads;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;
	private final List<Block> validConnectors;

	public ConnectedTextureModel(EnumSet<Direction> enabledFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, @Nullable List<BakedQuad>[] baseQuads, BakedQuad[][][] quads, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms) {
		this.enabledFaces = enabledFaces;
		this.renderOnDisabledFaces = renderOnDisabledFaces;
		this.validConnectors = connectableBlocks;
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
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().find();
		CastleDoorData data = this.buildData(level, pos);
		for (Direction side : Direction.values()) {
			for (BakedQuad quad : this.getQuads(side, data)) {
				context.getEmitter().fromVanilla(quad, material, side).emit();
			}
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().find();
		CastleDoorData data = new CastleDoorData();
		for (Direction side : Direction.values()) {
			for (BakedQuad quad : this.getQuads(side, data)) {
				context.getEmitter().fromVanilla(quad, material, side).emit();
			}
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
		if (side == null) {
			return List.of();
		}
		return this.getQuads(side, new CastleDoorData());
	}

	private List<BakedQuad> getQuads(Direction side, CastleDoorData data) {
		int faceIndex = side.get3DDataValue();
		ArrayList<BakedQuad> result = new ArrayList<>(4 + (this.baseQuads != null ? 4 : 0));
		if (this.baseQuads != null) {
			result.addAll(this.baseQuads[faceIndex]);
		}

		if (this.enabledFaces.contains(side) || this.renderOnDisabledFaces) {
			for (int quad = 0; quad < 4; ++quad) {
				ConnectionLogic connectionType = this.enabledFaces.contains(side) ? data.logic[faceIndex][quad] : ConnectionLogic.NONE;
				result.add(this.quads[faceIndex][quad][connectionType.ordinal()]);
			}
		}

		return result;
	}

	private CastleDoorData buildData(BlockAndTintGetter getter, BlockPos pos) {
		CastleDoorData data = new CastleDoorData();

		for (Direction face : Direction.values()) {
			Direction[] directions = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
			boolean[] sideStates = new boolean[4];

			for (int faceIndex = 0; faceIndex < directions.length; faceIndex++) {
				sideStates[faceIndex] = this.shouldConnectSide(getter, pos, face, directions[faceIndex]);
			}

			int faceIndex = face.get3DDataValue();
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
		return this.validConnectors.stream().anyMatch(neighborState::is) && Block.shouldRenderFace(neighborState, getter, pos, face, pos.relative(face));
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		return this.validConnectors.stream().anyMatch(neighborState::is) && Block.shouldRenderFace(neighborState, getter, pos, face, pos.relative(face));
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

	private static final class CastleDoorData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private CastleDoorData() {
			for (int face = 0; face < this.logic.length; face++) {
				for (int quad = 0; quad < this.logic[face].length; quad++) {
					this.logic[face][quad] = ConnectionLogic.NONE;
				}
			}
		}
	}
}
