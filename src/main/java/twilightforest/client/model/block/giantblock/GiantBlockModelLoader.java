package twilightforest.client.model.block.giantblock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import io.github.fabricators_of_create.porting_lib.mixin.client.accessor.BlockModelAccessor;
import io.github.fabricators_of_create.porting_lib.model.geometry.IGeometryLoader;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.block.patch.UnbakedPatchModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class GiantBlockModelLoader implements ModelResourceProvider {

	public static final GiantBlockModelLoader INSTANCE = new GiantBlockModelLoader();

	@Override
	public UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) {
		if(!resourceId.getNamespace().equals(TwilightForestMod.ID))
			return null;
		BufferedReader reader = getModelJson(resourceId);
		JsonObject object = BlockModelAccessor.port_lib$GSON().fromJson(reader, JsonObject.class);
		if(object.has("loader")) {
			if(!object.get("loader").getAsString().equals("twilightforest:giant_block"))
				return null;
			BlockModel ownerModel = BlockModel.fromString(object.toString());
			ResourceLocation parent = ResourceLocation.tryParse(object.get("parent_block").getAsString());

			assert parent != null;
			return new UnbakedGiantBlockModel(parent, ownerModel);
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