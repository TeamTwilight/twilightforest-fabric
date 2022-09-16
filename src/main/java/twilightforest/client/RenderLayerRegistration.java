package twilightforest.client;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import io.github.fabricators_of_create.porting_lib.client.NamedRenderTypeManager;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class RenderLayerRegistration {
	@SuppressWarnings("removal")
	public static void init() {
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> manager.listResources("models/block", resourceLocation -> {
			if (resourceLocation.getNamespace().equals(TwilightForestMod.ID) && resourceLocation.getPath().endsWith(".json")) {
				manager.getResource(resourceLocation).ifPresent(resource -> {
					try {
						JsonObject jsonObject = Streams.parse(new JsonReader(new InputStreamReader(resource.open(), Charsets.UTF_8))).getAsJsonObject();
						if (jsonObject.has("render_type")) {
							ResourceLocation blockId = new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath().replace(".json", "").replace("models/block/", ""));
							BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(blockId), NamedRenderTypeManager.get(new ResourceLocation(GsonHelper.getAsString(jsonObject, "render_type"))).block());
						}
					} catch (IOException | NoSuchElementException e) {
						e.fillInStackTrace();
					}
				});
			}
			return true;
		}));
	}
}
