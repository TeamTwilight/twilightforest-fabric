package carminite.client.mixin;

import carminite.event.hooks.CommonHooks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
	private void carminite$fireBlockBreakEvent(
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
	private boolean carminite$ignoreCanDestroyBlock(boolean original) {
		return true;
	}
}