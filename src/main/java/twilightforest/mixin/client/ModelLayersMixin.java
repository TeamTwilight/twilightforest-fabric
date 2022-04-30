package twilightforest.mixin.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLayers.class)
public class ModelLayersMixin {
  @Inject(method = "createSignModelName", at = @At("HEAD"), cancellable = true)
  private static void fixSignLocation(WoodType woodType, CallbackInfoReturnable<ModelLayerLocation> cir) {
    if (woodType.name().contains("twilightforest:")) {
        ResourceLocation location = new ResourceLocation(woodType.name());
        cir.setReturnValue(new ModelLayerLocation(new ResourceLocation(location.getNamespace(), "sign/" + location.getPath()), "main"));
    }
  }
}
