package twilightforest.client.model.block.forcefield;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ForceFieldModelBuilder {
	private final ResourceLocation loader;
	private final JsonObject textures = new JsonObject();
	private final List<JsonObject> elements = new ArrayList<>();

	public ForceFieldModelBuilder(ResourceLocation loader) {
		this.loader = loader;
	}

	public ForceFieldModelBuilder texture(String key, ResourceLocation texture) {
		this.textures.addProperty(key, texture.toString());
		return this;
	}

	public ForceFieldModelBuilder element(JsonObject element) {
		this.elements.add(element);
		return this;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("loader", this.loader.toString());
		json.add("textures", this.textures);
		JsonArray elementsJson = new JsonArray();
		this.elements.forEach(elementsJson::add);
		json.add("elements", elementsJson);
		return json;
	}
}
