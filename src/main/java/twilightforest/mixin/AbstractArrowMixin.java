package twilightforest.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.ProjectileHitEvent;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @Unique
    private HitResult result;

    @ModifyVariable(method = "tick", at = @At(value = "STORE"), ordinal = 0)
    public HitResult saveHitResult(HitResult result){
        this.result = result;
        return result;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    public void onHitEvent(CallbackInfo ci) {
        ProjectileHitEvent.PROJECTILE_HIT_EVENT.invoker().onHit((AbstractArrow) (Object) this, result);
    }
}
