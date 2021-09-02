package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TFTickHandler;

import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void playerTickEnd(CallbackInfo ci) {
        TFTickHandler.playerTick((Player) (Object) this);
    }
}
