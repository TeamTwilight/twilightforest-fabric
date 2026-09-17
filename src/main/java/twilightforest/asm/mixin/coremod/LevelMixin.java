package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.BlockHooks;

@Mixin(Level.class)
public class LevelMixin {

	@ModifyReturnValue(
		method = "isRainingAt(Lnet/minecraft/core/BlockPos;)Z",
		at = @At("RETURN")
	)
	private boolean twilightforest$isRainingAt(
		boolean original,
		@Local(argsOnly = true, name = "pos") BlockPos pos
	) {
		return BlockHooks.isRainingAt(original, (Level) (Object) this, pos);
	}
}