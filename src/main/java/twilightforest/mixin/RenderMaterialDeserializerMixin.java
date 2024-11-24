package twilightforest.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.fabricators_of_create.porting_lib.models.RenderMaterialDeserializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.BiConsumer;

@Mixin(value = RenderMaterialDeserializer.class, remap = false)
public class RenderMaterialDeserializerMixin {
	/**
	 * Fixing the ClassCastException if there is just one object instead of an Array.
	 * @author marlester
	 * @reason caused by ItemLayerModelBuilder not properly utilizing an array.
	 */
	@Overwrite
	public void forEachSpriteIndex(JsonObject obj, String key, BiConsumer<Integer, JsonElement> matFunc) {
		if (obj.has(key)) {
			JsonArray array = null;
			try {
				array = obj.getAsJsonArray(key);
			} catch (ClassCastException e) {
				var newArray = new JsonArray();
				newArray.add(obj.get(key));
				array = newArray;
			}

			for(int i = 0; i < array.size(); ++i) {
				matFunc.accept(i, array.get(i));
			}
		}
	}
}
