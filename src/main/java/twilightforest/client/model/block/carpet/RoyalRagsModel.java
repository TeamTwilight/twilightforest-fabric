package twilightforest.client.model.block.carpet;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.RenderType;
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
import io.github.fabricators_of_create.porting_lib.render_types.RenderTypeGroup;
import io.github.fabricators_of_create.porting_lib.models.data.ModelData;
import io.github.fabricators_of_create.porting_lib.models.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.block.IDynamicBakedModel;
import twilightforest.client.model.block.connected.ConnectionLogic;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class RoyalRagsModel implements IDynamicBakedModel, FabricBakedModel {
	@Nullable
	private final List<BakedQuad>[] baseQuads;
	private final BakedQuad[][][] quads;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;
	private final RenderTypeGroup renderTypes;
	private final Block[] validConnectors = {TFBlocks.CORONATION_CARPET.value()};
	private static final ModelProperty<LoftyCarpetData> DATA = new ModelProperty<>();

	public RoyalRagsModel(@Nullable List<BakedQuad>[] baseQuads, BakedQuad[][][] quads, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms, RenderTypeGroup group) {
		this.baseQuads = baseQuads;
		this.quads = quads;
		this.particle = particle;
		this.overrides = overrides;
		this.transforms = transforms;
		this.renderTypes = group;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
		// This model uses emitBlockQuads for rendering because it requires level/pos context.
		return List.of();
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		LoftyCarpetData data = this.computeData(level, pos, state);
		QuadEmitter emitter = context.getEmitter();

		for (Direction face : Direction.values()) {
			if (face.getAxis().isHorizontal()) {
				if (this.baseQuads != null) {
					for (BakedQuad quad : this.baseQuads[face.get2DDataValue()]) {
						this.emitBakedQuad(emitter, quad);
					}
				}
			} else {
				int faceIndex = face.get3DDataValue();
				for (int quad = 0; quad < 4; ++quad) {
					ConnectionLogic connectionType = data.logic[faceIndex][quad];
					this.emitBakedQuad(emitter, this.quads[faceIndex][quad][connectionType.ordinal()]);
				}
			}
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		QuadEmitter emitter = context.getEmitter();
		for (Direction face : Direction.values()) {
			if (face.getAxis().isHorizontal()) {
				if (this.baseQuads != null) {
					for (BakedQuad quad : this.baseQuads[face.get2DDataValue()]) {
						this.emitBakedQuad(emitter, quad);
					}
				}
			} else {
				int faceIndex = face.get3DDataValue();
				for (int quad = 0; quad < 4; ++quad) {
					this.emitBakedQuad(emitter, this.quads[faceIndex][quad][ConnectionLogic.NONE.ordinal()]);
				}
			}
		}
	}

	private void emitBakedQuad(QuadEmitter emitter, BakedQuad quad) {
		emitter.fromVanilla(quad.getVertices(), 0);
		emitter.spriteBake(quad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
		emitter.colorIndex(quad.getTintIndex());
		emitter.cullFace(quad.getDirection());
		emitter.material(this.renderTypes.material());
		emitter.emit();
	}

	private LoftyCarpetData computeData(BlockAndTintGetter getter, BlockPos pos, BlockState state) {
		LoftyCarpetData data = new LoftyCarpetData();

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
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is);
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is);
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

	private static final class LoftyCarpetData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private LoftyCarpetData() {
		}
	}
}