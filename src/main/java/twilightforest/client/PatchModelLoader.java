package twilightforest.client;

import com.google.gson.JsonObject;
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
        JsonObject modelContents = BlockModelAccessor.port_lib$GSON().fromJson(getModelJson(resourceId), JsonObject.class);
        if(modelContents.has("loader")) {
            if(!modelContents.get("loader").getAsString().equals("twilightforest:patch"))
                return null;
            if (!modelContents.has("texture"))
                throw new RuntimeException("Patch model missing value for 'texture'.");

            return new UnbakedPatchModel(new ResourceLocation(modelContents.get("texture").getAsString()), JsonUtils.getBooleanOr("shaggify", modelContents, false));
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
