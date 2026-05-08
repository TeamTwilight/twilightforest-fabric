package twilightforest.client.model.block.aurorablock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric port of upstream {@code twilightforest.client.model.block.aurorablock.NoiseVaryingModel}.
 *
 * <p>Wraps N child {@link BakedModel}s (each a normal cube_all model with a different texture).
 * Vanilla {@link BakedModel#getQuads} has no BlockPos parameter — we therefore implement
 * {@link FabricBakedModel#emitBlockQuads} from fabric-renderer-api-v1, which DOES receive
 * BlockPos, and use {@link SimplexNoiseHelper#calcVariant} to pick the variant for that
 * exact position. Result is identical to upstream NeoForge ModelData-based variant pick.</p>
 */
@Environment(EnvType.CLIENT)
public class NoiseVaryingBakedModel implements BakedModel, FabricBakedModel {
	private final BakedModel[] variants;
	private final BakedModel primary;

	public NoiseVaryingBakedModel(BakedModel[] variants) {
		this.variants = variants;
		this.primary = variants[0];
	}

	// ===== FabricBakedModel — receives BlockPos so we can pick by simplex noise =====

	@Override
	public boolean isVanillaAdapter() {
		// false → tells FRAPI to call emitBlockQuads / emitItemQuads instead of getQuads.
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		int variantIndex = SimplexNoiseHelper.calcVariant(pos, this.variants.length);
		BakedModel chosen = this.variants[variantIndex];
		// Delegate to the chosen variant's emit path. If the chosen variant is itself a Fabric
		// model, defer to it; otherwise fall back to the vanilla-quad rendering path.
		if (chosen instanceof FabricBakedModel fb && !fb.isVanillaAdapter()) {
			fb.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		} else {
			context.bakedModelConsumer().accept(chosen, state);
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		// For item form (held/in inventory) we use the primary variant; there's no BlockPos.
		if (this.primary instanceof FabricBakedModel fb && !fb.isVanillaAdapter()) {
			fb.emitItemQuads(stack, randomSupplier, context);
		} else {
			context.bakedModelConsumer().accept(this.primary);
		}
	}

	// ===== BakedModel — vanilla-side fallback (used when FRAPI not bypassed) =====

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		return this.primary.getQuads(state, side, rand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.primary.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.primary.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.primary.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.primary.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.primary.getParticleIcon();
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.primary.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.primary.getOverrides();
	}
}
