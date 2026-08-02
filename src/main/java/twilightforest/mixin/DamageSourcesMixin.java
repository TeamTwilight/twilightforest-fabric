package twilightforest.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.DamageSourceHooks;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {

	@Inject(method = "mobAttack", at = @At("RETURN"), cancellable = true)
	private void twilightforest$customMobAttackDamage(LivingEntity entity, CallbackInfoReturnable<DamageSource> cir) {
		cir.setReturnValue(DamageSourceHooks.getCustomDamageSource(cir.getReturnValue(), entity));
	}

	@Inject(method = "playerAttack", at = @At("RETURN"), cancellable = true)
	private void twilightforest$customPlayerAttackDamage(Player player, CallbackInfoReturnable<DamageSource> cir) {
		cir.setReturnValue(DamageSourceHooks.getCustomDamageSource(cir.getReturnValue(), player));
	}
}