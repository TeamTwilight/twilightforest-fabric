package twilightforest.client.model.block;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric equivalent of NeoForge's {@code IDynamicBakedModel}.
 * <p>
 * Provides a clean {@link #getQuads(BlockState, Direction, RandomSource, ModelData, RenderType)}
 * method that receives {@link ModelData} for dynamic model rendering, while automatically
 * integrating with Fabric API's {@code emitBlockQuads} rendering pipeline.
 * <p>
 * To ensure Fabric API's renderer calls {@link #emitBlockQuads} / {@link #emitItemQuads}
 * instead of the vanilla {@link BakedModel#getQuads} path, concrete implementations
 * MUST also implement {@link net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel}.
 * <p>
 * Models implementing this interface should override {@link #getQuads(BlockState, Direction, RandomSource, ModelData, RenderType)}
 * instead of {@link #emitBlockQuads(BlockAndTintGetter, BlockState, BlockPos, Supplier, RenderContext)}.
 */
public interface IDynamicBakedModel extends BakedModel {

	/**
	 * Get the quads for this model, with dynamic {@link ModelData} and {@link RenderType}.
	 * <p>
	 * This is the main method that models should override. It is called per-side during rendering.
	 *
	 * @param state      the block state, or null for item rendering
	 * @param side       the face direction, or null for non-culled quads
	 * @param rand       random source
	 * @param data       model data from the block entity or level context
	 * @param renderType the render type, or null for all
	 * @return list of baked quads
	 */
	List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType);

	/**
	 * Delegates the vanilla {@link BakedModel#getQuads(BlockState, Direction, RandomSource)} to
	 * {@link #getQuads(BlockState, Direction, RandomSource, ModelData, RenderType)} with empty data.
	 */
	@Override
	default List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		return getQuads(state, side, rand, ModelData.EMPTY, null);
	}

	/**
	 * Fabric API: tells the renderer to use {@link #emitBlockQuads} instead of {@link #getQuads}.
	 */
	@Override
	default boolean isVanillaAdapter() {
		return false;
	}

	/**
	 * Fabric API: emits block quads by calling {@link #getQuads} for each side.
	 * Models that need direct access to the level/position can override this method.
	 */
	@Override
	default void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		ModelData data = getModelData(level, pos, state, ModelData.EMPTY);
		QuadEmitter emitter = context.getEmitter();
		RandomSource random = randomSupplier.get();

		for (Direction side : Direction.values()) {
			List<BakedQuad> quads = getQuads(state, side, random, data, null);
			for (BakedQuad quad : quads) {
				emitter.fromVanilla(quad.getVertices(), 0);
				emitter.spriteBake(quad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
				emitter.colorIndex(quad.getTintIndex());
				emitter.cullFace(quad.getDirection());
				emitter.emit();
			}
		}

		List<BakedQuad> unculled = getQuads(state, null, random, data, null);
		for (BakedQuad quad : unculled) {
			emitter.fromVanilla(quad.getVertices(), 0);
			emitter.spriteBake(quad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
			emitter.colorIndex(quad.getTintIndex());
			emitter.cullFace(null);
			emitter.emit();
		}
	}

	/**
	 * Fabric API: emits item quads by calling {@link #getQuads} for each side with null state.
	 * Models that need custom item rendering can override this method.
	 */
	@Override
	default void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		QuadEmitter emitter = context.getEmitter();
		RandomSource random = randomSupplier.get();

		for (Direction side : Direction.values()) {
			List<BakedQuad> quads = getQuads(null, side, random, ModelData.EMPTY, null);
			for (BakedQuad quad : quads) {
				emitter.fromVanilla(quad.getVertices(), 0);
				emitter.spriteBake(quad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
				emitter.colorIndex(quad.getTintIndex());
				emitter.cullFace(quad.getDirection());
				emitter.emit();
			}
		}

		List<BakedQuad> unculled = getQuads(null, null, random, ModelData.EMPTY, null);
		for (BakedQuad quad : unculled) {
			emitter.fromVanilla(quad.getVertices(), 0);
			emitter.spriteBake(quad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
			emitter.colorIndex(quad.getTintIndex());
			emitter.cullFace(null);
			emitter.emit();
		}
	}

	/**
	 * Hook for models to provide custom {@link ModelData} based on the level context.
	 * Override this to pass data from block entities or level state to the model.
	 *
	 * @param level     the level (block and tint getter)
	 * @param pos       the block position
	 * @param state     the block state
	 * @param modelData existing model data
	 * @return enriched model data
	 */
	default ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
		return modelData;
	}
}