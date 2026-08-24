package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.TriState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.ProgressionEventHooks;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@ModifyExpressionValue(
		method = "performUseItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;isSecondaryUseActive()Z"
		)
	)
	private boolean twilightforest$modifyUseBlock(
		boolean original,
		@Local(argsOnly = true, name = "player") LocalPlayer player,
		@Local(argsOnly = true, name = "blockHit") BlockHitResult blockHit
	) {
		var useBlock = TriState.DEFAULT;

		// PlayerInteractEvent.RightClickBlock events go here and need to set useBlock...
		useBlock = twilightforest$mergeTriState(
			useBlock,
			ProgressionEventHooks.preventLockedAreaBlockInteracting(
				player,
				blockHit.getBlockPos()
			)
		);

		return switch (useBlock) {
			case TRUE -> false;
			case FALSE -> true;
			case DEFAULT -> original;
		};
	}

	@Unique
	private static TriState twilightforest$mergeTriState(TriState current, TriState result) {
		return result == TriState.DEFAULT ? current : result;
	}
}