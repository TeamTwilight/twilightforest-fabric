package twilightforest.client.model.block.patch;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public final class PatchModelLoader {
	public static final PatchModelLoader INSTANCE = new PatchModelLoader();

	private PatchModelLoader() {
	}

	public UnbakedPatchModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		if (!object.has("texture")) {
			throw new JsonParseException("Patch model missing value for 'texture'.");
		}

		return new UnbakedPatchModel(ResourceLocation.parse(object.get("texture").getAsString()), GsonHelper.getAsBoolean(object, "shaggify", false));
	}
}
