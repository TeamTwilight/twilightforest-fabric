package twilightforest.client.model.block.aurorablock;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

public class NoiseVaryingModelBuilder {
	private final List<ResourceLocation> variants = new ArrayList<>();

	public NoiseVaryingModelBuilder add(ResourceLocation location) {
		this.variants.add(location);
		return this;
	}

	public NoiseVaryingModelBuilder addAll(ResourceLocation[] locations) {
		for (ResourceLocation location : locations) {
			this.add(location);
		}
		return this;
	}

	public JsonObject toJson(JsonObject json) {
		Preconditions.checkArgument(!this.variants.isEmpty(), "Noise Varying builder cannot have zero variants.");
		json.addProperty("loader", TwilightForestMod.prefix("noise_varying").toString());
		JsonArray variants = new JsonArray();
		this.variants.stream().map(ResourceLocation::toString).forEach(variants::add);
		json.add("variants", variants);
		return json;
	}
}
