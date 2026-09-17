package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(SnowyBlock.class)
public class SnowyBlockMixin {

	@ModifyReturnValue(
		method = "isSnowySetting(Lnet/minecraft/world/level/block/state/BlockState;)Z",
		at = @At("RETURN")
	)
	private static boolean twilightforest$keepSnowyStateForSnowloggableBlocks(
		boolean original,
		@Local(argsOnly = true, name = "aboveState") BlockState aboveState
	) {
		return BlockHooks.keepSnowyStateForSnowloggableBlocks(original, aboveState);
	}
}