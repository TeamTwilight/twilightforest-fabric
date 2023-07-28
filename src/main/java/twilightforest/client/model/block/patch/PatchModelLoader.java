package twilightforest.client.model.block.patch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import io.github.fabricators_of_create.porting_lib.models.PortingLibModelLoadingRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.fabric.models.TFModelResolver;

public final class PatchModelLoader implements TFModelResolver {
    public static final PatchModelLoader INSTANCE = new PatchModelLoader();

    private PatchModelLoader() {
    }

    @Override
    public UnbakedModel tryResolveModel(Context ctx) throws Exception {
        ResourceLocation id = ctx.id();
        if(!id.getNamespace().equals(TwilightForestMod.ID))
            return null;
        JsonObject object = BlockModel.GSON.fromJson(PortingLibModelLoadingRegistry.getModelJson(id), JsonObject.class);
        if(object.has("loader")) {
            if(!object.get("loader").getAsString().equals("twilightforest:patch"))
                return null;
            if (!object.has("texture"))
                throw new JsonParseException("Patch model missing value for 'texture'.");

            return new UnbakedPatchModel(new ResourceLocation(object.get("texture").getAsString()), JsonUtils.getBooleanOr("shaggify", object, false));
        }

        return null;
    }
}
