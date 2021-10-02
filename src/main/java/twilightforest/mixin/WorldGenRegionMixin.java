package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.level.WorldGenRegion;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    //TODO: HMMM kinda a concern
    @Redirect(method = "ensureCanWrite", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V"))
    public void shush(String error) {}
}
