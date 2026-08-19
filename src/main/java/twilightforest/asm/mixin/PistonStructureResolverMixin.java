package twilightforest.asm.mixin;

import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.interfaces.marker.ISpecialStickyBlock;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {

	@Inject(
		method = "isSticky(Lnet/minecraft/world/level/block/state/BlockState;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void twilightforest$isSticky(
		BlockState state,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (state.getBlock() instanceof ISpecialStickyBlock specialStickyBlock) {
			cir.setReturnValue(specialStickyBlock.isStickyBlock(state));
		}
	}

	@Inject(
		method = "canStickToEachOther(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void twilightforest$canStickTo(
		BlockState state1,
		BlockState state2,
		CallbackInfoReturnable<Boolean> cir
	) {
		boolean useCustomLogic = false;
		boolean canStickTo = state1.twilightforest$canStickTo(state2);
		boolean canStickToAdj = state2.twilightforest$canStickTo(state1);
		if (state1.getBlock() instanceof ISpecialStickyBlock) {
			useCustomLogic = true;
		}
		if (state2.getBlock() instanceof ISpecialStickyBlock) {
			useCustomLogic = true;
		}
		if (useCustomLogic) {
			cir.setReturnValue(canStickTo && canStickToAdj);
		}
	}
}