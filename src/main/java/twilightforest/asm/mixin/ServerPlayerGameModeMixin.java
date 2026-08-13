package twilightforest.asm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.BossSpawnerBlock;
import twilightforest.block.ForceFieldBlock;
import twilightforest.block.SkullChestBlock;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

	@Shadow
	protected ServerLevel level;

	@Inject(
		method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$canEntityDestroy(
		BlockPos pos,
		CallbackInfoReturnable<Boolean> cir
	) {
		BlockState state = this.level.getBlockState(pos);
		Block block = state.getBlock();

		if (block instanceof BossSpawnerBlock) {
			cir.setReturnValue(false);
		}

		if (block instanceof ForceFieldBlock) {
			cir.setReturnValue(false);
		}

		if (block instanceof SkullChestBlock) {
			cir.setReturnValue(false);
		}
	}
}