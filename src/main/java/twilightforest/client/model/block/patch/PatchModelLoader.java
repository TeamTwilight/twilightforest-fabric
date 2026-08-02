package twilightforest.client.model.block.patch;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;

public final class PatchModelLoader implements IGeometryLoader<UnbakedPatchModel> {
	public static final PatchModelLoader INSTANCE = new PatchModelLoader();

	private PatchModelLoader() {
	}

	@Override
	public UnbakedPatchModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		if (!object.has("texture"))
			throw new JsonParseException("Patch model missing value for 'texture'.");
		return new UnbakedPatchModel(ResourceLocation.parse(object.get("texture").getAsString()), JsonUtils.getBooleanOr("shaggify", object, false));
	}
}