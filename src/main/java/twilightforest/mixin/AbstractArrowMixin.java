package twilightforest.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.events.ProjectileHitEvent;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    public void onHitEvent(AbstractArrow abstractArrow, HitResult result) {
        ProjectileHitEvent.PROJECTILE_HIT_EVENT.invoker().onHit(abstractArrow, result);
        try {
            //Use reflection to avoid using a aw
            Projectile.class.getDeclaredMethod(FabricLoader.getInstance().isDevelopmentEnvironment() ? "onHit" : "method_7488", HitResult.class).invoke(abstractArrow, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
