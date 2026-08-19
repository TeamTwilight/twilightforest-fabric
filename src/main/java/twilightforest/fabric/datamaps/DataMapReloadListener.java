package twilightforest.fabric.datamaps;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import twilightforest.TFMain;

import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DataMapReloadListener implements ResourceManagerReloadListener {

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		for (SimpleDataMap<?, ?> map : SimpleDataMap.ALL) {
			loadOne(manager, map);
		}
	}

	private static <R, T> void loadOne(ResourceManager manager, SimpleDataMap<R, T> map) {
		String registryPath = map.registryKey().identifier().getPath();
		String dir = "data_maps/" + registryPath + "/" + map.id().getPath();

		Map<Identifier, T> result = new LinkedHashMap<>();

		for (Resource resource : manager.getResourceStack(map.id().withPath(dir + ".json"))) {
			try (Reader reader = resource.openAsReader()) {
				JsonObject json = GsonHelper.parse(reader);

				if (GsonHelper.getAsBoolean(json, "replace", false)) {
					result.clear();
				}

				if (json.has("values")) {
					JsonObject values = GsonHelper.getAsJsonObject(json, "values");
					for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
						Identifier entryId = Identifier.parse(entry.getKey());
						T value = map.codec().parse(JsonOps.INSTANCE, entry.getValue())
							.getOrThrow(msg -> new IllegalStateException(
								"Failed to parse data map entry " + entryId + " for " + map.id() + ": " + msg));
						result.put(entryId, value);
					}
				}

				if (json.has("remove")) {
					for (JsonElement el : GsonHelper.getAsJsonArray(json, "remove")) {
						result.remove(Identifier.parse(el.getAsString()));
					}
				}
			} catch (Exception e) {
				TFMain.LOGGER.error("Error loading data map {} from {}", map.id(), resource.sourcePackId(), e);
			}
		}

		map.load(result);
	}
}