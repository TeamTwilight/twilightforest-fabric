package twilightforest.asm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.hooks.CommonHooks;
import twilightforest.fabric.hooks.EventHooks;
import twilightforest.fabric.interfaces.extension.IPlayerExtension;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerExtension {

	@Override
	public boolean twilightforest$hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
		return EventHooks.doPlayerHarvestCheck((Player) (Object) this, state, level, pos);
	}

	@Inject(
		method = "tick()V",
		at = @At("HEAD")
	)
	private void twilightforest$playerTickPre(CallbackInfo ci) {
		EventHooks.firePlayerTickPre((Player) (Object) this);
	}

	@Inject(
		method = "tick()V",
		at = @At("TAIL")
	)
	private void twilightforest$playerTickPost(CallbackInfo ci) {
		EventHooks.firePlayerTickPost((Player) (Object) this);
	}

	@Inject(
		method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$livingDeath(
		DamageSource source,
		CallbackInfo ci
	) {
		if (CommonHooks.onLivingDeath((LivingEntity) (Object) this, source)) {
			ci.cancel();
		}
	}
}