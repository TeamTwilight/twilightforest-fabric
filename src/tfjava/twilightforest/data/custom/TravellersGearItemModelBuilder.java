package twilightforest.data.custom;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class TravellersGearItemModelBuilder {
	private final String directory;
	private final String brokenDirectory;
	private final JsonObject textures = new JsonObject();

	public static TravellersGearItemModelBuilder begin(String directory, String brokenDirectory) {
		return new TravellersGearItemModelBuilder(directory, brokenDirectory);
	}

	public static TravellersGearItemModelBuilder begin(Object parent, String directory, String brokenDirectory, Object existingFileHelper) {
		return new TravellersGearItemModelBuilder(directory, brokenDirectory);
	}

	protected TravellersGearItemModelBuilder(String directory, String brokenDirectory) {
		this.directory = directory;
		this.brokenDirectory = brokenDirectory;
	}

	public TravellersGearItemModelBuilder texture(String key, ResourceLocation texture) {
		this.textures.addProperty(key, texture.toString());
		return this;
	}

	public TravellersGearItemModelBuilder texture(String key, String texture) {
		this.textures.addProperty(key, texture);
		return this;
	}

	public JsonObject toJson() {
		return this.toJson(new JsonObject());
	}

	public JsonObject toJson(JsonObject json) {
		json.addProperty("loader", TwilightForestMod.prefix("travellers_gear").toString());
		json.addProperty("modifier_directory", this.directory);
		json.addProperty("broken_modifier_directory", this.brokenDirectory);
		if (this.textures.size() > 0) {
			json.add("textures", this.textures.deepCopy());
		}
		return json;
	}
}
