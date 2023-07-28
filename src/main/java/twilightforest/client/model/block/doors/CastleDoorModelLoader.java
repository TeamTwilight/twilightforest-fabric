package twilightforest.client.model.block.doors;

import com.google.gson.JsonObject;
import io.github.fabricators_of_create.porting_lib.models.PortingLibModelLoadingRegistry;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.fabric.models.TFModelResolver;

public class CastleDoorModelLoader implements TFModelResolver {
	public static final CastleDoorModelLoader INSTANCE = new CastleDoorModelLoader();

	public CastleDoorModelLoader() {
	}

	@Override
	public UnbakedModel tryResolveModel(Context ctx) throws  Exception {
		ResourceLocation id = ctx.id();
		if(!id.getNamespace().equals(TwilightForestMod.ID))
			return null;
		JsonObject object = BlockModel.GSON.fromJson(PortingLibModelLoadingRegistry.getModelJson(id), JsonObject.class);
		if(object.has("loader")) {
			if(!object.get("loader").getAsString().equals("twilightforest:castle_door"))
				return null;
			BlockModel ownerModel = BlockModel.fromString(object.toString());

			return new UnbakedCastleDoorModel(ownerModel);
		}

		return null;
	}
}
