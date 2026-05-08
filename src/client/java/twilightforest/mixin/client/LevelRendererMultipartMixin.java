package twilightforest.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.client.BakedMultiPartRenderers;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMultipartMixin {
    @Redirect(
            method = "renderLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
    private Iterable<Entity> codex_twilight$renderTFParts(ClientLevel level) {
        return BakedMultiPartRenderers.injectTFPartEntities(level.entitiesForRendering());
    }
}
