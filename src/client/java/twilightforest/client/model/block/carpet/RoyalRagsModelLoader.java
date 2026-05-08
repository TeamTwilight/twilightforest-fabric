package twilightforest.client.model.block.carpet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.LinkedHashMap;
import java.util.Map;

public class RoyalRagsModelLoader {
	@Deprecated
	public static final RoyalRagsModelLoader INSTANCE = new RoyalRagsModelLoader();

	public UnbakedRoyalRagsModel read(JsonObject jsonObject) throws JsonParseException {
		Map<String, Material> materials = new LinkedHashMap<>();
		JsonObject textures = GsonHelper.getAsJsonObject(jsonObject, "textures");
		for (Map.Entry<String, JsonElement> entry : textures.entrySet()) {
			materials.put(entry.getKey(), new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse(entry.getValue().getAsString())));
		}
		return new UnbakedRoyalRagsModel(materials);
	}
}
