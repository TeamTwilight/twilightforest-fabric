package twilightforest.data.custom.stalactites.entry;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public record Stalactite(Block ore, float sizeVariation, int maxLength, int weight) {

	private static StalactiteReloadListener STALAGTITE_CONFIG;

	public static void reloadStalactites(AddReloadListenerEvent event) {
		STALAGTITE_CONFIG = new StalactiteReloadListener();
		event.addListener(STALAGTITE_CONFIG);
	}

	public static StalactiteReloadListener getStalactiteConfig() {
		if (STALAGTITE_CONFIG == null)
			throw new IllegalStateException("Can not retrieve Stalactites yet!");
		return STALAGTITE_CONFIG;
	}

	public static class Serializer implements JsonDeserializer<Stalactite>, JsonSerializer<Stalactite> {

		@Override
		public Stalactite deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonobject = GsonHelper.convertToJsonObject(json, "stalactite");
			String block = GsonHelper.getAsString(jsonobject, "ore");
			if (ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(block)) == null) {
				throw new JsonParseException("Block " + block + " defined in Stalactite config does not exist!");
			}
			Block ore = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(block));
			float size = GsonHelper.getAsFloat(jsonobject, "size_variation");
			int maxLength = GsonHelper.getAsInt(jsonobject, "max_length");
			int weight = GsonHelper.getAsInt(jsonobject, "weight");

			return new Stalactite(ore, size, maxLength, weight);
		}

		@Override
		public JsonElement serialize(Stalactite stalactite, Type type, JsonSerializationContext context) {
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("ore", context.serialize(ForgeRegistries.BLOCKS.getKey(stalactite.ore()).toString()));
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
