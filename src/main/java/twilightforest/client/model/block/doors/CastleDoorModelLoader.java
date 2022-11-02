package twilightforest.client.model.block.doors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.fabricators_of_create.porting_lib.model.geometry.IGeometryLoader;

public class CastleDoorModelLoader implements IGeometryLoader<UnbakedCastleDoorModel> {
	public static final CastleDoorModelLoader INSTANCE = new CastleDoorModelLoader();

	public CastleDoorModelLoader() {
	}

	public UnbakedCastleDoorModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new UnbakedCastleDoorModel();
	}
}
