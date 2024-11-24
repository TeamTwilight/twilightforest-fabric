package twilightforest.client.model;

import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;

import io.github.fabricators_of_create.porting_lib.models.BakedMeshModel;
import io.github.fabricators_of_create.porting_lib.models.PortingLibModelLoadingRegistry;
import io.github.fabricators_of_create.porting_lib.models.UnbakedGeometryHelper;
import io.github.fabricators_of_create.porting_lib.models.builders.ItemLayerModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class TFItemLayerModel implements IUnbakedGeometry<TFItemLayerModel> {
	@Nullable
	private ImmutableList<Material> textures;
	private final Int2ObjectMap<RenderMaterial> layerData;

	private TFItemLayerModel(@Nullable ImmutableList<Material> textures, Int2ObjectMap<RenderMaterial> layerData) {
		this.textures = textures;
		this.layerData = layerData;
	}

	@Override
	public BakedModel bake(BlockModel context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation, boolean isGui3d) {
		if (textures == null) {
			ImmutableList.Builder<Material> builder = ImmutableList.builder();
			if (context.hasTexture("particle"))
				builder.add(context.getMaterial("particle"));
			for (int i = 0; context.hasTexture("layer" + i); i++)
			{
				builder.add(context.getMaterial("layer" + i));
			}
			textures = builder.build();
		}

		TextureAtlasSprite particle = spriteGetter.apply(
				context.hasTexture("particle") ? context.getMaterial("particle") : textures.get(0)
		);

		MeshBuilder meshBuilder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
		for (int i = 0; i < textures.size(); i++)
		{
			QuadEmitter emitter = meshBuilder.getEmitter();
			TextureAtlasSprite sprite = spriteGetter.apply(textures.get(i));
			var unbaked = ModelBakery.ITEM_MODEL_GENERATOR.processFrames(i, "layer" + i, sprite.contents());
			var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState, modelLocation);
			for (BakedQuad quad : quads) {
				emitter.fromVanilla(quad, this.layerData.get(i), quad.getDirection());
				emitter.emit();
			}
		}

		return new BakedMeshModel(context, particle, meshBuilder.build());
	}

	public static final class Loader implements IGeometryLoader<TFItemLayerModel>  {
		public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "item_layers");
		public static final Loader INSTANCE = new Loader();

		@Override
		public TFItemLayerModel read(JsonObject jsonObject, JsonDeserializationContext parent) throws JsonParseException {
			if (!RendererAccess.INSTANCE.hasRenderer())
				throw new JsonParseException("The Fabric Rendering API is not available. If you have Sodium, install Indium!");
			var emissiveLayers = new Int2ObjectArrayMap<RenderMaterial>();
			if(jsonObject.has("render_materials")) {
				JsonObject forgeData = jsonObject.get("render_materials").getAsJsonObject();
				readLayerData(forgeData, "layers", emissiveLayers);
			}
			return new TFItemLayerModel(null, emissiveLayers);
		}

		public void readLayerData(JsonObject jsonObject, String name, Int2ObjectMap<RenderMaterial> layerData) {
			if (!jsonObject.has(name)) {
				return;
			}
			var fullbrightLayers = jsonObject.getAsJsonObject(name);
			for (var entry : fullbrightLayers.entrySet()) {
				int layer = Integer.parseInt(entry.getKey());
				var data = PortingLibModelLoadingRegistry.GSON.fromJson(entry.getValue(), RenderMaterial.class);
				layerData.put(layer, data);
			}
		}
	}
}
