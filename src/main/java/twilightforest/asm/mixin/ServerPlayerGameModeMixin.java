package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.block.*;
import twilightforest.init.TFBlocks;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

	@Shadow
	protected ServerLevel level;

	@Shadow
	@Final
	protected ServerPlayer player;

	@WrapOperation(
		method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
		)
	)
	private boolean twilightforest$onThornDestroyed(
		ServerLevel instance,
		BlockPos pos,
		boolean b,
		Operation<Boolean> original
	) {
		ServerPlayer player = this.player;
		BlockState state = level.getBlockState(pos);

		if (state.is(TFBlocks.BURNT_THORNS)) {
			state.getBlock().playerWillDestroy(level, pos, state, player);
			level.setBlock(pos, level.getFluidState(pos).createLegacyBlock(), level.isClientSide() ? Block.UPDATE_ALL_IMMEDIATE : Block.UPDATE_ALL);
		} else if (state.getBlock() instanceof ThornsBlock && !player.isCreative()) {
			if (!level.isClientSide()) {
				ThornsBlock.doThornBurst(level, pos, state);
			}
			return false;
		}

		return original.call(level, pos, b);
	}
}