package twilightforest.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TwilightForestMod;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

	@Inject(
		method = "isValid",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$isValid(
		BlockState state,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (cir.getReturnValue()) return;

		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
		if (id.getNamespace().equals(TwilightForestMod.ID) && (id.getPath().endsWith("_chest") || id.getPath().endsWith("_trapped_chest"))) {
			cir.setReturnValue(true);
		}
	}
}