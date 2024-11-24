package twilightforest.client.model.block.doors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class CastleDoorModelLoader implements IGeometryLoader<UnbakedCastleDoorModel> {
	public static final CastleDoorModelLoader INSTANCE = new CastleDoorModelLoader();
	public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "castle_door");

	public CastleDoorModelLoader() {}

	@Override
	public UnbakedCastleDoorModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new UnbakedCastleDoorModel();
	}
}
