package twilightforest.client.model.block.forcefield;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.util.GsonHelper;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForceFieldModelLoader implements IGeometryLoader<UnbakedForceFieldModel> {
	public static final ForceFieldModelLoader INSTANCE = new ForceFieldModelLoader();

	@Override
	@SuppressWarnings("ConstantConditions")
	public UnbakedForceFieldModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

		Map<BlockElement, Condition> elementsAndConditions = new HashMap<>();

		if (json.has("elements")) {
			for (JsonElement jsonElement : GsonHelper.getAsJsonArray(json, "elements")) {
				ForceFieldModel.ExtraDirection direction = null;
				boolean b = false;
				List<ForceFieldModel.ExtraDirection> parents = new ArrayList<>();

				if (jsonElement instanceof JsonObject element) {
					if (element.get("condition") instanceof JsonObject condition) {
						direction = ForceFieldModel.ExtraDirection.byName(GsonHelper.getAsString(condition, "direction", "up"));
						b = GsonHelper.getAsBoolean(condition, "if", true);
						for (JsonElement parentElement : GsonHelper.getAsJsonArray(condition, "parents")) {
							parents.add(ForceFieldModel.ExtraDirection.byName(parentElement.getAsString()));
						}
					}
				}
				elementsAndConditions.put(context.deserialize(jsonElement, BlockElement.class), new Condition(direction, b, parents));
			}
		}

		return new UnbakedForceFieldModel(elementsAndConditions);
	}

	public record Condition(@Nullable ForceFieldModel.ExtraDirection direction, boolean b, List<ForceFieldModel.ExtraDirection> parents) {

	}
}