package twilightforest.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import io.github.fabricators_of_create.porting_lib.mixin.client.accessor.BlockModelAccessor;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import twilightforest.TwilightForestMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public final class PatchModelLoader implements ModelResourceProvider {
    public static final PatchModelLoader INSTANCE = new PatchModelLoader();

    private PatchModelLoader() {
    }

    @Override
    public UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) {
        if(!resourceId.getNamespace().equals(TwilightForestMod.ID))
            return null;
        JsonObject object = BlockModelAccessor.port_lib$GSON().fromJson(getModelJson(resourceId), JsonObject.class);
        if(object.has("loader")) {
            if(!object.get("loader").getAsString().equals("twilightforest:patch"))
                return null;
            if (!object.has("texture"))
                throw new JsonParseException("Patch model missing value for 'texture'.");

            return new UnbakedPatchModel(new ResourceLocation(object.get("texture").getAsString()), JsonUtils.getBooleanOr("shaggify", object, false));
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
