package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.*;
import twilightforest.init.TFBlocks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

	@Shadow
	protected ServerLevel level;

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
			cir.setReturnValue(state.getValue(VanishingBlock.ACTIVE) || !areBlocksLocked(state, pos));
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
				this.doThornBurst(level, pos, state);
			}
			return false;
		}

		return original.call(level, pos, b);
	}

	@Unique
	private static boolean areBlocksLocked(BlockState state, BlockPos start) {
		int limit = 512;
		Deque<BlockPos> queue = new ArrayDeque<>();
		Set<BlockPos> checked = new HashSet<>();
		queue.offer(start);

		for (int iter = 0; !queue.isEmpty() && iter < limit; iter++) {
			BlockPos cur = queue.pop();
			if (state.getBlock() == TFBlocks.LOCKED_VANISHING_BLOCK && state.getValue(LockedVanishingBlock.LOCKED)) {
				return true;
			}

			checked.add(cur);

			if (state.getBlock() instanceof VanishingBlock) {
				for (Direction facing : Direction.values()) {
					BlockPos neighbor = cur.relative(facing);
					if (!checked.contains(neighbor)) {
						queue.offer(neighbor);
					}
				}
			}
		}

		return false;
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	@Unique
	private void doThornBurst(Level level, BlockPos pos, BlockState state) {
		switch (state.getValue(ThornsBlock.AXIS)) {
			case Y -> {
				this.growThorns(level, pos, Direction.UP);
				this.growThorns(level, pos, Direction.DOWN);
			}
			case X -> {
				this.growThorns(level, pos, Direction.EAST);
				this.growThorns(level, pos, Direction.WEST);
			}
			case Z -> {
				this.growThorns(level, pos, Direction.NORTH);
				this.growThorns(level, pos, Direction.SOUTH);
			}
		}

		// also try three random directions
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	@Unique
	private void growThorns(Level level, BlockPos pos, Direction dir) {
		int length = 1 + level.getRandom().nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			if (level.isEmptyBlock(dPos)) {
				level.setBlock(dPos, TFBlocks.GREEN_THORNS.defaultBlockState().setValue(ThornsBlock.AXIS, dir.getAxis()), Block.UPDATE_CLIENTS);
			} else {
				break;
			}
		}
	}
}