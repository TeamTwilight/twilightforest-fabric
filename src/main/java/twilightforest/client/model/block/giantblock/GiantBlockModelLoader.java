package twilightforest.client.model.block.giantblock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GiantBlockModelLoader implements IGeometryLoader<UnbakedGiantBlockModel> {

	public static final GiantBlockModelLoader INSTANCE = new GiantBlockModelLoader();

	private final Map<ResourceLocation, Map<Direction, ResourceLocation>> textures = new HashMap<>();

	@Override
	public UnbakedGiantBlockModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		ResourceLocation parent = ResourceLocation.tryParse(object.get("parent_block").getAsString());

		if (object.has("textures")) {
			JsonObject textures = object.get("textures").getAsJsonObject();
			Map<Direction, ResourceLocation> rawTextures = new HashMap<>();
			for (Direction direction : Direction.values()) {
				if (!textures.has(direction.getName().toLowerCase(Locale.ROOT)))
					throw new JsonParseException("Giant block model is missing a texture for side " + direction.getName().toLowerCase(Locale.ROOT));

				rawTextures.put(direction, new ResourceLocation(textures.get(direction.getName().toLowerCase(Locale.ROOT)).getAsString()));
			}

			this.textures.put(parent, rawTextures);
		}

		String rendertype = "solid";

		if (object.has("render_type")) {
			rendertype = object.get("render_type").getAsString();
		}

		return new UnbakedGiantBlockModel(this.textures, rendertype, parent);
	}
}
