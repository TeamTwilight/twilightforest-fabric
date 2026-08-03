package twilightforest.client.model.block;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * Wraps the baked torchberry plant block model and re-emits its quads through the
 * Fabric Renderer API so that the berries layer ({@code torchberry_plant_glow})
 * renders with full-brightness emissive lighting, matching the official look.
 * <p>
 * In 1.20.1 this emissivity was provided by Porting Lib's {@code porting_lib_data}
 * (block_light/sky_light), but that renderer module no longer exists on 1.21.1, so
 * the glow must be applied at the model level.
 */
public class TorchberryPlantModel extends ForwardingBakedModel {

	private static final RenderMaterial CUTOUT_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.CUTOUT)
		.find();
	private static final RenderMaterial EMISSIVE_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.CUTOUT)
		.emissive(0, true)
		.find();

	private final ResourceLocation glowTexture;

	public TorchberryPlantModel(BakedModel wrapped, ResourceLocation glowTexture) {
		this.wrapped = wrapped;
		this.glowTexture = glowTexture;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RandomSource random = randomSupplier.get();
		QuadEmitter emitter = context.getEmitter();
		for (Direction side : Direction.values()) {
			for (BakedQuad quad : this.wrapped.getQuads(state, side, random)) {
				this.emit(emitter, quad);
			}
		}
		for (BakedQuad quad : this.wrapped.getQuads(state, null, random)) {
			this.emit(emitter, quad);
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RandomSource random = randomSupplier.get();
		QuadEmitter emitter = context.getEmitter();
		for (Direction side : Direction.values()) {
			for (BakedQuad quad : this.wrapped.getQuads(null, side, random)) {
				this.emit(emitter, quad);
			}
		}
		for (BakedQuad quad : this.wrapped.getQuads(null, null, random)) {
			this.emit(emitter, quad);
		}
	}

	private void emit(QuadEmitter emitter, BakedQuad quad) {
		boolean glow = this.glowTexture.equals(quad.getSprite().contents().name());
		// Cross-plant quads have no cull face; never cull them against neighboring blocks.
		emitter.fromVanilla(quad, glow ? EMISSIVE_MATERIAL : CUTOUT_MATERIAL, null);
		emitter.emit();
	}
}