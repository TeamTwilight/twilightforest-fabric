package twilightforest.data.custom.stalactites.entry;

import com.google.gson.*;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Type;

public record Stalactite(Block ore, float sizeVariation, int maxLength, int weight) {

	private static StalactiteReloadListener STALACTITE_CONFIG;

	public static void reloadStalactites() {
		STALACTITE_CONFIG = new StalactiteReloadListener();
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(STALACTITE_CONFIG);
	}

	public static StalactiteReloadListener getStalactiteConfig() {
		if (STALACTITE_CONFIG == null)
			throw new IllegalStateException("Can not retrieve Stalactites yet!");
		return STALACTITE_CONFIG;
	}

	public static class Serializer implements JsonDeserializer<Stalactite>, JsonSerializer<Stalactite> {

		@Override
		public Stalactite deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonobject = GsonHelper.convertToJsonObject(json, "stalactite");
			String block = GsonHelper.getAsString(jsonobject, "ore");
			if (Registry.BLOCK.get(ResourceLocation.tryParse(block)) == null) {
				throw new JsonParseException("Block " + block + " defined in Stalactite config does not exist!");
			}
			Block ore = Registry.BLOCK.get(ResourceLocation.tryParse(block));
			float size = GsonHelper.getAsFloat(jsonobject, "size_variation");
			int maxLength = GsonHelper.getAsInt(jsonobject, "max_length");
			int weight = GsonHelper.getAsInt(jsonobject, "weight");

			return new Stalactite(ore, size, maxLength, weight);
		}

		@Override
		public JsonElement serialize(Stalactite stalactite, Type type, JsonSerializationContext context) {
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("ore", context.serialize(Registry.BLOCK.getKey(stalactite.ore()).toString()));
			jsonobject.add("size_variation", context.serialize(stalactite.sizeVariation()));
			jsonobject.add("max_length", context.serialize(stalactite.maxLength()));
			jsonobject.add("weight", context.serialize(stalactite.weight()));
			return jsonobject;
		}
	}

	public enum HollowHillType {
		SMALL,
		MEDIUM,
		LARGE
	}
}
