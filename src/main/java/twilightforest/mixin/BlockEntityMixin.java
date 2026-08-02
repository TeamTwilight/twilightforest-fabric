package twilightforest.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to allow Twilight Forest chest blocks to use vanilla ChestBlockEntity.
 * In 1.21.1, ChestBlock.newBlockEntity hardcodes BlockEntityType.CHEST,
 * ignoring the supplier passed to the constructor. This causes
 * IllegalStateException when TF chest blocks are placed.
 *
 * This mixin skips validation for TF chest blocks, but still
 * syncs the blockState to prevent chests from becoming unopenable.
 */
@Mixin(BlockEntity.class)
public class BlockEntityMixin {

	@Shadow
	private BlockState blockState;

	@Inject(method = "validateBlockState", at = @At("HEAD"), cancellable = true)
	private void twilightforest$validateBlockState(BlockState state, CallbackInfo ci) {
		String blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
		if (blockName.startsWith("twilightforest:") && (blockName.endsWith("_chest") || blockName.endsWith("_trapped_chest"))) {
			// Set blockState before cancelling to keep chest BE in sync
			this.blockState = state;
			ci.cancel();
		}
	}
}