package twilightforest.client.model.block.doors;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import twilightforest.TwilightForestMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class CastleDoorModelLoader implements ModelResourceProvider {
	public static final CastleDoorModelLoader INSTANCE = new CastleDoorModelLoader();

	public CastleDoorModelLoader() {
	}

	@Override
	public UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) {
		if(!resourceId.getNamespace().equals(TwilightForestMod.ID))
			return null;
		JsonObject object = BlockModel.GSON.fromJson(getModelJson(resourceId), JsonObject.class);
		if(object.has("loader")) {
			if(!object.get("loader").getAsString().equals("twilightforest:castle_door"))
				return null;
			BlockModel ownerModel = BlockModel.fromString(object.toString());

			return new UnbakedCastleDoorModel(ownerModel);
		}

		return null;
	}

	static BufferedReader getModelJson(ResourceLocation location) {
		ResourceLocation file = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
		Optional<Resource> resource = null;
		resource = Minecraft.getInstance().getResourceManager().getResource(file);
		try {
			return resource.get().openAsReader();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
