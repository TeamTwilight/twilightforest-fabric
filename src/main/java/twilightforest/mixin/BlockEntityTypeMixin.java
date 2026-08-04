package twilightforest.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to allow Twilight Forest chest blocks to use vanilla ChestBlockEntity.
 * In 1.21.1, BlockEntityType.isValid() checks if the block state is in the type's
 * supported blocks set. TF custom chests (dark_chest, mangrove_chest, etc.) inherit
 * ChestBlock but are not in BlockEntityType.CHEST's supported blocks, causing
 * IllegalStateException when Boss loot chests are placed.
 */
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
		// If already valid, don't interfere
		if (cir.getReturnValue()) return;

		// Allow TF chest/trapped chest blocks to be used with vanilla chest block entity types
		String blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
		if (blockName.startsWith("twilightforest:") && (blockName.endsWith("_chest") || blockName.endsWith("_trapped_chest"))) {
			cir.setReturnValue(true);
		}
	}
}