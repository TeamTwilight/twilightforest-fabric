package twilightforest.client.model.block.forcefield;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.client.model.block.forcefield.ForceFieldModel.ExtraDirection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ForceFieldModelLoader {
	public static final ForceFieldModelLoader INSTANCE = new ForceFieldModelLoader();

	public UnbakedForceFieldModel read(JsonObject json) throws JsonParseException {
		Map<String, Material> materials = readTextures(json);
		Map<BlockElement, Condition> elementsAndConditions = new LinkedHashMap<>();

		if (json.has("elements")) {
			for (JsonElement jsonElement : GsonHelper.getAsJsonArray(json, "elements")) {
				JsonObject element = GsonHelper.convertToJsonObject(jsonElement, "element");
				elementsAndConditions.put(parseElement(element), parseCondition(element));
			}
		}

		return new UnbakedForceFieldModel(elementsAndConditions, materials);
	}

	private static Map<String, Material> readTextures(JsonObject json) {
		Map<String, Material> textures = new LinkedHashMap<>();
		JsonObject textureObject = GsonHelper.getAsJsonObject(json, "textures", new JsonObject());
		for (Map.Entry<String, JsonElement> entry : textureObject.entrySet()) {
			textures.put(entry.getKey(), new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse(entry.getValue().getAsString())));
		}
		return textures;
	}

	private static Condition parseCondition(JsonObject element) {
		ExtraDirection direction = null;
		boolean present = false;
		List<ExtraDirection> parents = new ArrayList<>();

		if (element.get("condition") instanceof JsonObject condition) {
			direction = ForceFieldModel.ExtraDirection.byName(GsonHelper.getAsString(condition, "direction", "up"));
			present = GsonHelper.getAsBoolean(condition, "if", true);
			if (condition.has("parents")) {
				for (JsonElement parentElement : GsonHelper.getAsJsonArray(condition, "parents")) {
					ExtraDirection parent = ForceFieldModel.ExtraDirection.byName(parentElement.getAsString());
					if (parent != null) {
						parents.add(parent);
					}
				}
			}
		}

		return new Condition(direction, present, parents);
	}

	private static BlockElement parseElement(JsonObject element) {
		Vector3f from = parseVector(GsonHelper.getAsJsonArray(element, "from"), "from");
		Vector3f to = parseVector(GsonHelper.getAsJsonArray(element, "to"), "to");
		Map<Direction, BlockElementFace> faces = new LinkedHashMap<>();

		JsonObject faceObject = GsonHelper.getAsJsonObject(element, "faces");
		for (Map.Entry<String, JsonElement> entry : faceObject.entrySet()) {
			Direction direction = Direction.byName(entry.getKey());
			if (direction == null) {
				throw new JsonParseException("Unknown force field face direction " + entry.getKey());
			}
			faces.put(direction, parseFace(GsonHelper.convertToJsonObject(entry.getValue(), "face")));
		}

		BlockElementRotation rotation = element.has("rotation") ? parseRotation(GsonHelper.getAsJsonObject(element, "rotation")) : null;
		boolean shade = GsonHelper.getAsBoolean(element, "shade", true);
		return new BlockElement(from, to, faces, rotation, shade);
	}

	private static BlockElementFace parseFace(JsonObject face) {
		Direction cull = face.has("cullface") ? Direction.byName(GsonHelper.getAsString(face, "cullface")) : null;
		int tintIndex = GsonHelper.getAsInt(face, "tintindex", BlockElementFace.NO_TINT);
		String texture = GsonHelper.getAsString(face, "texture");
		float[] uv = face.has("uv") ? parseUv(GsonHelper.getAsJsonArray(face, "uv")) : null;
		int rotation = GsonHelper.getAsInt(face, "rotation", 0);
		return new BlockElementFace(cull, tintIndex, texture, new BlockFaceUV(uv, rotation));
	}

	private static BlockElementRotation parseRotation(JsonObject rotation) {
		Vector3f origin = parseVector(GsonHelper.getAsJsonArray(rotation, "origin"), "origin");
		Direction.Axis axis = Direction.Axis.byName(GsonHelper.getAsString(rotation, "axis"));
		if (axis == null) {
			throw new JsonParseException("Unknown force field rotation axis");
		}
		float angle = GsonHelper.getAsFloat(rotation, "angle");
		boolean rescale = GsonHelper.getAsBoolean(rotation, "rescale", false);
		return new BlockElementRotation(origin, axis, angle, rescale);
	}

	private static Vector3f parseVector(JsonArray array, String name) {
		if (array.size() != 3) {
			throw new JsonParseException("Expected 3 " + name + " values");
		}
		return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
	}

	private static float[] parseUv(JsonArray array) {
		if (array.size() != 4) {
			throw new JsonParseException("Expected 4 uv values");
		}
		return new float[]{array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), array.get(3).getAsFloat()};
	}

	public record Condition(@Nullable ExtraDirection direction, boolean b, List<ExtraDirection> parents) {
	}
}
