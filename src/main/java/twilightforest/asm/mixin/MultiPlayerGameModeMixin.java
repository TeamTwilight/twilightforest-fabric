package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.hooks.CommonHooks;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private GameType localPlayerMode;

	@Inject(
		method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$fireBlockBreakEvent(
		BlockPos pos,
		CallbackInfoReturnable<Boolean> cir
	) {
		ClientLevel level = this.minecraft.level;
		BlockState state = level.getBlockState(pos);
		var event = CommonHooks.fireBlockBreak(level, this.localPlayerMode, this.minecraft.player, pos, state);
		if (event.isCanceled()) {
			cir.setReturnValue(false);
		}
	}

	@ModifyExpressionValue(
		method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;canDestroyBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Z"
		)
	)
	private boolean twilightforest$ignoreCanDestroyBlock(boolean original) {
		return true;
	}

	@Inject(
		method = "performUseItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void twilightforest$rightClickBlock(
		LocalPlayer player,
		InteractionHand hand,
		BlockHitResult blockHit,
		CallbackInfoReturnable<InteractionResult> cir,
		@Local(name = "pos") BlockPos pos
	) {
		var event = CommonHooks.onRightClickBlock(player, hand, pos, blockHit);
		if (event.isCanceled()) {
			cir.setReturnValue(event.getCancellationResult());
		}
	}
}