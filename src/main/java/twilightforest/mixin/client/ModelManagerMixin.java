package twilightforest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.TFClientEvents;

import java.util.Map;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Shadow private Map<ResourceLocation, BakedModel> bakedRegistry;

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    public void onModelLoad(ModelBakery modelBakery, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        TFClientEvents.ModBusEvents.modelBake(this.bakedRegistry);
    }
}
