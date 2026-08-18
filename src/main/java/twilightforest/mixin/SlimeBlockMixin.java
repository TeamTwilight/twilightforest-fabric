package twilightforest.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TwilightForestMod;
import twilightforest.asmhooks.BlockHooks;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

	@Inject(
		method = "stepOn",
		at = @At("HEAD")
	)
	private void twilightforest$storeStepOnVelocity(
		Level level,
		BlockPos pos,
		BlockState state,
		Entity entity,
		CallbackInfo ci,
		@Share(value = "velocity", namespace = TwilightForestMod.ID) LocalRef<Vec3> velocityRef
	) {
		velocityRef.set(entity.getDeltaMovement());
	}

	@Inject(
		method = "stepOn",
		at = @At("TAIL")
	)
	private void twilightforest$restoreStepOnVelocity(
		Level level,
		BlockPos pos,
		BlockState state,
		Entity entity,
		CallbackInfo ci,
		@Share(value = "velocity", namespace = TwilightForestMod.ID) LocalRef<Vec3> velocityRef
	) {
		Vec3 original = velocityRef.get();
		BlockHooks.restoreStepOnVelocity(entity, original);
	}

	@Inject(
		method = "bounceUp",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$stopBouncing(
		Entity entity,
		CallbackInfo ci
	) {
		if (BlockHooks.stopBouncing(entity)) {
			ci.cancel();
		}
	}
}