package twilightforest.client.model.block.giantblock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class GiantBlockModelLoader {
	public static final GiantBlockModelLoader INSTANCE = new GiantBlockModelLoader();

	private GiantBlockModelLoader() {
	}

	public UnbakedGiantBlockModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		ResourceLocation parent = ResourceLocation.parse(object.get("parent_block").getAsString());
		Map<String, ResourceLocation> textures = new LinkedHashMap<>();
		JsonObject textureObject = object.getAsJsonObject("textures");
		textureObject.entrySet().forEach(entry -> textures.put(entry.getKey(), ResourceLocation.parse(entry.getValue().getAsString())));
		return new UnbakedGiantBlockModel(parent, textures, true);
	}
}
