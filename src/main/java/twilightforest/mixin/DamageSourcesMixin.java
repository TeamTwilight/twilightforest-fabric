package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.DamageSourceHooks;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {

	@ModifyReturnValue(
		method = "mobAttack",
		at = @At("RETURN")
	)
	private DamageSource twilightforest$customMobAttackDamage(DamageSource original, LivingEntity mob) {
		return DamageSourceHooks.getCustomDamageSource(original, mob);
	}

	@ModifyReturnValue(
		method = "playerAttack",
		at = @At("RETURN")
	)
	private DamageSource twilightforest$customPlayerAttackDamage(DamageSource original, Player player) {
		return DamageSourceHooks.getCustomDamageSource(original, player);
	}
}
