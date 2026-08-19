package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.*;
import twilightforest.fabric.hooks.CommonHooks;
import twilightforest.init.TFBlocks;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

	@Shadow
	protected ServerLevel level;

	@Shadow
	private GameType gameModeForPlayer;

	@Shadow
	@Final
	protected ServerPlayer player;

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

		if (block instanceof StrongholdShieldBlock) {
			cir.setReturnValue(false);
		}

		if (block instanceof LockedVanishingBlock) {
			cir.setReturnValue(!state.getValue(LockedVanishingBlock.LOCKED));
		}

		if (block instanceof VanishingBlock) {
			cir.setReturnValue(state.getValue(VanishingBlock.ACTIVE) || !VanishingBlock.areBlocksLocked(state, pos));
		}
	}

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

	@Inject(
		method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void twilightforest$fireBreakBlockEvent(
		BlockPos pos,
		CallbackInfoReturnable<Boolean> cir
	) {
		BlockState state = this.level.getBlockState(pos);
		var event = CommonHooks.fireBlockBreak(this.level, this.gameModeForPlayer, this.player, pos, state);
		if (event.isCanceled()) {
			cir.setReturnValue(false);
		}
	}
}