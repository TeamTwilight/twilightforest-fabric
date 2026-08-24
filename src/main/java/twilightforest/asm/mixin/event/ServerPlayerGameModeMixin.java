package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.util.TriState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.ProgressionEventHooks;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

	@ModifyExpressionValue(
		method = "useItemOn(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;isSecondaryUseActive()Z"
		)
	)
	private boolean twilightforest$modifyUseBlock(
		boolean original,
		@Local(argsOnly = true, name = "player") ServerPlayer player,
		@Local(argsOnly = true, name = "hitResult") BlockHitResult hitResult
	) {
		var useBlock = TriState.DEFAULT;

		// PlayerInteractEvent.RightClickBlock events go here and need to set useBlock...
		useBlock = twilightforest$mergeTriState(
			useBlock,
			ProgressionEventHooks.preventLockedAreaBlockInteracting(
				player,
				hitResult.getBlockPos()
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