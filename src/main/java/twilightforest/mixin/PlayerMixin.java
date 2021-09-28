package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TFEventListener;
import twilightforest.TFTickHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.block.ThornsBlock;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void playerTickEnd(CallbackInfo ci) {
        TFTickHandler.playerTick((Player) (Object) this);
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", ordinal = 0), cancellable = true)
    public void entityHurt(DamageSource damageSrc, float damageAmount, CallbackInfo ci) {
        if (!TFEventListener.entityHurts((LivingEntity) (Object) this, damageSrc))
            ci.cancel();
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void onAttack(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!TFEventListener.livingAttack((LivingEntity) (Object) this, source))
            cir.cancel();
    }

}
