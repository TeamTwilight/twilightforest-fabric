package twilightforest.client;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import io.github.fabricators_of_create.porting_lib.client.NamedRenderTypeManager;
import io.github.fabricators_of_create.porting_lib.mixin.client.accessor.BlockModelAccessor;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class RenderLayerRegistration {
	public static void init() {
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelId, context) -> {
			if (!modelId.getNamespace().equals(TwilightForestMod.ID))
				return null;
			Block block = Registry.BLOCK.get(new ResourceLocation(modelId.getNamespace(), modelId.getPath()));
			Optional<Resource> optionalResource = resourceManager.getResource(new ResourceLocation(modelId.getNamespace(), "blockstates/" + modelId.getPath() + ".json"));
			optionalResource.ifPresent(resource -> {
				try {
					JsonObject jsonObject = Streams.parse(new JsonReader(new InputStreamReader(resource.open(), Charsets.UTF_8))).getAsJsonObject();

					if (jsonObject.has("variants")) {
						loadVariants(block, resourceManager, jsonObject.getAsJsonObject("variants"));
					} else if (jsonObject.has("multipart")) {
						loadMultiparts(block, resourceManager, jsonObject.getAsJsonArray("multipart"));
					}

					BlockModelAccessor.port_lib$GSON().fromJson(jsonObject, BlockModel.class);

				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

			return null;
		});
	}

	private static void loadVariants(Block block, ResourceManager resourceManager, JsonObject variants) {
		variants.keySet().forEach(s -> {
			JsonObject stateModel = null;
			if (variants.get(s).isJsonArray())
				stateModel = GsonHelper.getAsJsonArray(variants, s).get(0).getAsJsonObject();
			else
				stateModel = GsonHelper.getAsJsonObject(variants, s);
			resourceManager.getResource(new ResourceLocation(stateModel.getAsJsonObject().get("model").getAsString().replace("block/", "models/block/") + ".json")).ifPresent(variant -> {
				try {
					JsonObject modelJson = Streams.parse(new JsonReader(new InputStreamReader(variant.open(), Charsets.UTF_8))).getAsJsonObject();
					if (modelJson.has("render_type")) {
						BlockRenderLayerMap.INSTANCE.putBlock(block, NamedRenderTypeManager.get(new ResourceLocation(GsonHelper.getAsString(modelJson, "render_type"))).block());
						return;
					}
					// Brute force are way if we haven't found a render type
					String json = modelJson.toString();
					if (!json.contains("\"render_type\""))
						return;
					json = json.substring(json.indexOf("\"render_type\""));
					json = json.replace("\"render_type\":\"", "");
					StringBuilder builder = new StringBuilder();
					for (int i = 0; i < json.length(); i++) {
						if (json.charAt(i) == '"') {
							BlockRenderLayerMap.INSTANCE.putBlock(block, NamedRenderTypeManager.get(new ResourceLocation(builder.toString())).block());
							return;
						}
						builder.append(json.charAt(i));
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		});
	}

	private static void loadMultiparts(Block block, ResourceManager resourceManager, JsonArray multiparts) {
		multiparts.forEach(element -> {
			if (element.getAsJsonObject().get( "apply").isJsonArray())
				return;
			resourceManager.getResource(new ResourceLocation(GsonHelper.getAsJsonObject(element.getAsJsonObject(), "apply").getAsJsonObject().get("model").getAsString().replace("block/", "models/block/") + ".json")).ifPresent(variant -> {
				try {
					JsonObject modelJson = Streams.parse(new JsonReader(new InputStreamReader(variant.open(), Charsets.UTF_8))).getAsJsonObject();
					if (modelJson.has("render_type")) {
						BlockRenderLayerMap.INSTANCE.putBlock(block, NamedRenderTypeManager.get(new ResourceLocation(GsonHelper.getAsString(modelJson, "render_type"))).block());
						return;
					}
					// Brute force are way if we haven't found a render type
					String json = modelJson.toString();
					if (!json.contains("\"render_type\""))
						return;
					json = json.substring(json.indexOf("\"render_type\""));
					json = json.replace("\"render_type\":\"", "");
					StringBuilder builder = new StringBuilder();
					for (int i = 0; i < json.length(); i++) {
						if (json.charAt(i) == '"') {
							BlockRenderLayerMap.INSTANCE.putBlock(block, NamedRenderTypeManager.get(new ResourceLocation(builder.toString())).block());
							return;
						}
						builder.append(json.charAt(i));
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
			BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_CASTLE_DOOR.get(), RenderType.cutout());
		});
	}
}
