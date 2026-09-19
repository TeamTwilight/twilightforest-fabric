package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asm.hooks.AddBreakingBlockEffectDuck;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Redirect(
		method = "continueAttack(Z)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientLevel;addBreakingBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V"
		)
	)
	private void twilightforest$redirectAddBreakingBlockEffect(
		ClientLevel instance,
		BlockPos pos,
		Direction direction,
		@Local(name = "blockHit") BlockHitResult blockHit
	) {
		((AddBreakingBlockEffectDuck) instance).twilightforest$addBreakingBlockEffect(pos, direction, blockHit);
	}
}