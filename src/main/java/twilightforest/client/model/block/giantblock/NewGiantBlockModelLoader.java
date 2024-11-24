package twilightforest.client.model.block.giantblock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class NewGiantBlockModelLoader implements IGeometryLoader<NewUnbakedGiantBlockModel> {
	public static final NewGiantBlockModelLoader INSTANCE = new NewGiantBlockModelLoader();
	public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "giant_block");

	@Override
	public NewUnbakedGiantBlockModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		ResourceLocation parent = ResourceLocation.tryParse(object.get("parent_block").getAsString());

		assert parent != null;
		return new NewUnbakedGiantBlockModel(parent);
	}
}
