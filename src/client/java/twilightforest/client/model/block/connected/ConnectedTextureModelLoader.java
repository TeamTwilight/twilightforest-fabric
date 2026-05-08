package twilightforest.client.model.block.connected;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectedTextureModelLoader {
	public static final ConnectedTextureModelLoader INSTANCE = new ConnectedTextureModelLoader();

	public UnbakedConnectedTextureModel read(JsonObject jsonObject) throws JsonParseException {
		JsonObject baseTextureInfo = GsonHelper.getAsJsonObject(jsonObject, "base", new JsonObject());
		int baseTintIndex = GsonHelper.getAsInt(baseTextureInfo, "tint_index", -1);
		int baseEmissivity = GsonHelper.getAsInt(baseTextureInfo, "emissivity", 0);

		JsonObject overlayInfo = GsonHelper.getAsJsonObject(jsonObject, "connected_texture", new JsonObject());
		int tintIndex = GsonHelper.getAsInt(overlayInfo, "tint_index", -1);
		int emissivity = GsonHelper.getAsInt(overlayInfo, "emissivity", 0);
		boolean renderDisabled = GsonHelper.getAsBoolean(overlayInfo, "render_disabled_faces", true);
		EnumSet<Direction> faces = this.parseEnabledFaces(overlayInfo);

		return new UnbakedConnectedTextureModel(faces, renderDisabled, this.parseConnectableBlocks(jsonObject), baseTintIndex, baseEmissivity, tintIndex, emissivity, this.parseMaterials(jsonObject));
	}

	private Map<String, Material> parseMaterials(JsonObject object) {
		Map<String, Material> materials = new LinkedHashMap<>();
		JsonObject textures = GsonHelper.getAsJsonObject(object, "textures");
		for (Map.Entry<String, JsonElement> entry : textures.entrySet()) {
			materials.put(entry.getKey(), new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse(entry.getValue().getAsString())));
		}
		return materials;
	}

	private EnumSet<Direction> parseEnabledFaces(JsonObject object) {
		if (!object.has("faces")) {
			return EnumSet.allOf(Direction.class);
		}

		EnumSet<Direction> faces = EnumSet.noneOf(Direction.class);
		for (JsonElement element : object.getAsJsonArray("faces")) {
			Direction face = Direction.byName(element.getAsString());
			if (face == null) {
				throw new JsonParseException("Invalid face: " + element.getAsString());
			}
			faces.add(face);
		}
		return faces;
	}

	private List<Block> parseConnectableBlocks(JsonObject object) {
		if (!object.has("connectable_blocks")) {
			return List.of();
		}

		List<Block> blocks = new ArrayList<>();
		for (JsonElement element : object.getAsJsonArray("connectable_blocks")) {
			String value = element.getAsString();
			if (value.startsWith("#")) {
				ResourceLocation tag = ResourceLocation.tryParse(value.substring(1));
				if (tag == null) {
					throw new JsonParseException("Invalid block tag: " + value);
				}
				BuiltInRegistries.BLOCK.getTagOrEmpty(TagKey.create(Registries.BLOCK, tag)).forEach(blockHolder -> blocks.add(blockHolder.value()));
			} else {
				ResourceLocation id = ResourceLocation.tryParse(value);
				Block block = id != null ? BuiltInRegistries.BLOCK.get(id) : Blocks.AIR;
				if (block == Blocks.AIR && id != null && "codex_twilight".equals(id.getNamespace())) {
					block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("twilightforest", id.getPath()));
				}
				if (block == Blocks.AIR) {
					throw new JsonParseException("Invalid block: " + value);
				}
				blocks.add(block);
			}
		}
		return blocks;
	}
}
