package twilightforest.mixin;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;

import java.util.function.Supplier;

/**
 * Fixes CompositeModel.Baked so that emitBlockQuads / emitItemQuads work on Fabric.
 */
@Mixin(targets = "io.github.fabricators_of_create.porting_lib.models.CompositeModel$Baked", remap = false)
public class CompositeModel$BakedMixin {

	@Unique
	private static final RenderMaterial CUTOUT_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.CUTOUT)
		.find();

	@Unique
	private static final RenderMaterial EMISSIVE_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
		.blendMode(0, BlendMode.CUTOUT)
		.emissive(0, true)
		.find();

	@Shadow
	@Final
	private ImmutableMap<String, BakedModel> children;

	/**
	 * @author Mitama
	 * @reason The original {@code emitBlockQuads} delegates to child models' {@code emitBlockQuads},
	 * which is a no-op for vanilla {@link BakedModel} instances. This mixin replaces the delegation
	 * with calls to {@code getQuads()} and emits the quads directly via the Fabric Renderer API.
	 * Also applies emissive materials to quads whose textures need full-brightness rendering
	 * (e.g. {@code tower_device_level_*} and {@code castleblock_magic_*}).
	 */
	@Overwrite
	public void emitBlockQuads(
		BlockAndTintGetter blockView,
		BlockState state,
		BlockPos pos,
		Supplier<RandomSource> randomSupplier,
		RenderContext context
	) {
		RandomSource random = randomSupplier.get();
		var emitter = context.getEmitter();
		for (BakedModel child : this.children.values()) {
			for (Direction side : Direction.values()) {
				for (BakedQuad quad : child.getQuads(state, side, random)) {
					emit(emitter, quad);
				}
			}
			for (BakedQuad quad : child.getQuads(state, null, random)) {
				emit(emitter, quad);
			}
		}
	}

	/**
	 * @author Mitama
	 * @reason Similar to above, but for items.
	 */
	@Overwrite
	public void emitItemQuads(
		ItemStack stack,
		Supplier<RandomSource> randomSupplier,
		RenderContext context
	) {
		RandomSource random = randomSupplier.get();
		var emitter = context.getEmitter();
		for (BakedModel child : this.children.values()) {
			for (Direction side : Direction.values()) {
				for (BakedQuad quad : child.getQuads(null, side, random)) {
					emit(emitter, quad);
				}
			}
			for (BakedQuad quad : child.getQuads(null, null, random)) {
				emit(emitter, quad);
			}
		}
	}

	@Unique
	private static void emit(QuadEmitter emitter, BakedQuad quad) {
		String path = quad.getSprite().contents().name().getPath();
		boolean emissive = path.contains("tower_device_level_") || path.contains("castleblock_magic_");
		emitter.fromVanilla(quad, emissive ? EMISSIVE_MATERIAL : CUTOUT_MATERIAL, null);
		emitter.emit();
	}
}
