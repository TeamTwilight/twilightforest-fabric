package twilightforest.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMultipartMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "getRenderer", at = @At("RETURN"), cancellable = true)
    private <E extends Entity> void codex_twilight$getTFPartRenderer(E entity, CallbackInfoReturnable<EntityRenderer<? super E>> callback) {
        if (entity instanceof TFPart<?> part) {
            EntityRenderer<?> renderer = BakedMultiPartRenderers.lookup(part.renderer());
            if (renderer != null) {
                callback.setReturnValue((EntityRenderer<? super E>) renderer);
            }
        }
    }
}
