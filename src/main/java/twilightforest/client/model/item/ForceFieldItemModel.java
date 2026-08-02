package twilightforest.client.model.item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryBakingContext;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A translucent 2D item model for force field items.
 * Uses a BakedQuad emitted via {@code fromVanilla} with a translucent material,
 * matching the proven approach in {@code ForceFieldModel}.
 * Tint index 0 triggers the registered ColorHandler for per-color tinting.
 */
public class ForceFieldItemModel implements IUnbakedGeometry<ForceFieldItemModel> {

	private static final FaceBakery FACE_BAKERY = new FaceBakery();

	private static final RenderMaterial TRANSLUCENT_MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder()
			.blendMode(0, BlendMode.TRANSLUCENT)
			.emissive(0, true)
			.disableDiffuse(0, true)
			.disableAo(0, true)
			.find();

	private ForceFieldItemModel() {
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		Material texMat = context.hasMaterial("layer0") ? context.getMaterial("layer0") : context.getMaterial("particle");
		TextureAtlasSprite sprite = spriteGetter.apply(texMat);
		TextureAtlasSprite particle = context.hasMaterial("particle") ? spriteGetter.apply(context.getMaterial("particle")) : sprite;

		// Build BakedQuads for both faces at bake time (texture coords are fixed)
		BakedQuad southQuad = buildQuad(sprite, Direction.SOUTH);
		BakedQuad northQuad = buildQuad(sprite, Direction.NORTH);

		return new Baked(sprite, particle, southQuad, northQuad, context.getTransforms(), overrides);
	}

	private static BakedQuad buildQuad(TextureAtlasSprite sprite, Direction face) {
		// Create a full-face BlockElement for the 16x16 item plane, with tint index 0
		BlockElementFace blockFace = new BlockElementFace(
				null,                        // no cullface
				0,                           // tintIndex -> triggers ColorHandler
				"layer0",                    // texture reference (not used directly, sprite is passed explicitly)
				new BlockFaceUV(new float[]{0f, 0f, 16f, 16f}, 0)
		);

		// Build a simple flat element spanning the full 16x16 plane at z=7.5 (center of item)
		Vector3f from, to;
		if (face == Direction.SOUTH) {
			from = new Vector3f(0f, 0f, 7.5f);
			to = new Vector3f(16f, 16f, 7.5f);
		} else {
			from = new Vector3f(0f, 0f, 8.5f);
			to = new Vector3f(16f, 16f, 8.5f);
		}

		return FACE_BAKERY.bakeQuad(from, to, blockFace, sprite, face, BlockModelRotation.X0_Y0, null, false);
	}

	public static final class Loader implements IGeometryLoader<ForceFieldItemModel> {
		public static final Loader INSTANCE = new Loader();

		private Loader() {
		}

		@Override
		public ForceFieldItemModel read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
			return new ForceFieldItemModel();
		}
	}

	public static class Baked implements BakedModel, FabricBakedModel {

		private final TextureAtlasSprite sprite;
		private final TextureAtlasSprite particle;
		private final BakedQuad southQuad;
		private final BakedQuad northQuad;
		private final ItemTransforms transforms;
		private final ItemOverrides overrides;

		public Baked(TextureAtlasSprite sprite, TextureAtlasSprite particle, BakedQuad southQuad, BakedQuad northQuad, ItemTransforms transforms, ItemOverrides overrides) {
			this.sprite = sprite;
			this.particle = particle;
			this.southQuad = southQuad;
			this.northQuad = northQuad;
			this.transforms = transforms;
			this.overrides = overrides;
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
			return List.of();
		}

		@Override
		public boolean useAmbientOcclusion() {
			return false;
		}

		@Override
		public boolean isGui3d() {
			return false;
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
		public ItemTransforms getTransforms() {
			return this.transforms;
		}

		@Override
		public ItemOverrides getOverrides() {
			return this.overrides;
		}

		@Override
		public boolean isVanillaAdapter() {
			return false;
		}

		@Override
		public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
			QuadEmitter emitter = context.getEmitter();

			// Use the exact same pattern as ForceFieldModel.emitBakedQuad()
			emitter.fromVanilla(this.southQuad, TRANSLUCENT_MATERIAL, Direction.SOUTH);
			emitter.emit();

			emitter.fromVanilla(this.northQuad, TRANSLUCENT_MATERIAL, Direction.NORTH);
			emitter.emit();
		}
	}
}
