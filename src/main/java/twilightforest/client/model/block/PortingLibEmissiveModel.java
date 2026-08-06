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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * Wraps baked block models that used Porting Lib's {@code porting_lib_data}
 * (block_light / sky_light) to render certain texture layers at full brightness.
 * <p>
 * In 1.20.1, Porting Lib's renderer module read {@code porting_lib_data} from
 * model JSON face data and adjusted the per-quad light level. That module was
 * removed in 1.21.1, so emissive rendering must be applied at the model level
 * via the Fabric Renderer API.
 * <p>
 * Any quad whose sprite texture path contains {@code tower_device_level_},
 * {@code castleblock_magic_}, or {@code uncrafting_glow} is emitted with an
 * emissive material, matching the NeoForge {@code neoforge_data} behavior.
 */
public class PortingLibEmissiveModel extends ForwardingBakedModel {

	private static final RenderMaterial CUTOUT_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.CUTOUT)
		.find();
	private static final RenderMaterial EMISSIVE_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.CUTOUT)
		.emissive(0, true)
		.find();
	private static final RenderMaterial TRANSLUCENT_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.TRANSLUCENT)
		.find();
	private static final RenderMaterial EMISSIVE_TRANSLUCENT_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.TRANSLUCENT)
		.emissive(0, true)
		.find();

	private final RenderMaterial normalMaterial;
	private final RenderMaterial emissiveMaterial;

	public PortingLibEmissiveModel(BakedModel wrapped) {
		this(wrapped, false);
	}

	public PortingLibEmissiveModel(BakedModel wrapped, boolean translucent) {
		this.wrapped = wrapped;
		this.normalMaterial = translucent ? TRANSLUCENT_MATERIAL : CUTOUT_MATERIAL;
		this.emissiveMaterial = translucent ? EMISSIVE_TRANSLUCENT_MATERIAL : EMISSIVE_MATERIAL;
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
				emit(emitter, quad);
			}
		}
		for (BakedQuad quad : this.wrapped.getQuads(state, null, random)) {
			emit(emitter, quad);
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RandomSource random = randomSupplier.get();
		QuadEmitter emitter = context.getEmitter();
		for (Direction side : Direction.values()) {
			for (BakedQuad quad : this.wrapped.getQuads(null, side, random)) {
				emit(emitter, quad);
			}
		}
		for (BakedQuad quad : this.wrapped.getQuads(null, null, random)) {
			emit(emitter, quad);
		}
	}

	private void emit(QuadEmitter emitter, BakedQuad quad) {
		String path = quad.getSprite().contents().name().getPath();
		boolean emissive = path.contains("tower_device_level_") || path.contains("castleblock_magic_") || path.contains("uncrafting_glow");
		emitter.fromVanilla(quad, emissive ? this.emissiveMaterial : this.normalMaterial, null);
		emitter.emit();
	}
}