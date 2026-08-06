package twilightforest.data.custom;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.CustomLoaderBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelBuilder;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.TwilightForestMod;

import java.util.LinkedHashMap;
import java.util.Map;

// FIXME: Try to get Porting Lib model working
public class TFSeparateTransformsModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
	public static <T extends ModelBuilder<T>> TFSeparateTransformsModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
		return new TFSeparateTransformsModelBuilder<>(parent, existingFileHelper);
	}

	private T base;
	private final Map<String, T> childModels = new LinkedHashMap<>();

	protected TFSeparateTransformsModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("separate_transforms"), parent, existingFileHelper, false);
	}

	public TFSeparateTransformsModelBuilder<T> base(T modelBuilder) {
		Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
		base = modelBuilder;
		return this;
	}

	public TFSeparateTransformsModelBuilder<T> perspective(ItemDisplayContext perspective, T modelBuilder) {
		Preconditions.checkNotNull(perspective, "perspective must not be null");
		Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
		childModels.put(perspective.getSerializedName(), modelBuilder);
		return this;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);

		if (base != null) {
			json.add("base", base.toJson());
		}

		JsonObject parts = new JsonObject();
		for (Map.Entry<String, T> entry : childModels.entrySet()) {
			parts.add(entry.getKey(), entry.getValue().toJson());
		}
		json.add("perspectives", parts);

		return json;
	}
}
