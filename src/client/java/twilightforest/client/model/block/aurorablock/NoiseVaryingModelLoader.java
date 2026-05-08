package twilightforest.client.model.block.aurorablock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

public class NoiseVaryingModelLoader {
	public static final NoiseVaryingModelLoader INSTANCE = new NoiseVaryingModelLoader();

	private NoiseVaryingModelLoader() {
	}

	public UnbakedNoiseVaryingModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		List<String> builder = new ArrayList<>();

		for (JsonElement entry : json.getAsJsonArray("variants")) {
			builder.add(entry.getAsString());
		}

		return new UnbakedNoiseVaryingModel(builder.toArray(String[]::new));
	}
}
