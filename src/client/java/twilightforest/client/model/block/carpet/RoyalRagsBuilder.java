package twilightforest.client.model.block.carpet;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class RoyalRagsBuilder {
	private final ResourceLocation loader;

	public RoyalRagsBuilder(ResourceLocation loader) {
		this.loader = loader;
	}

	public JsonObject toJson(JsonObject json) {
		json.addProperty("loader", this.loader.toString());
		return json;
	}
}
