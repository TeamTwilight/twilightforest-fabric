package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.block.*;
import twilightforest.enums.FireJetVariant;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

	@ModifyReturnValue(
		method = "getPathTypeFromState",
		at = @At("RETURN")
	)
	private static PathType twilightforest$modifyBlockPathType(PathType original, BlockGetter level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		// Damage-dealing blocks
		if (block instanceof KnightmetalBlock || block instanceof BurntThornsBlock || block instanceof ThornsBlock || block instanceof OreBerryBlock) {
			return PathType.DAMAGE_OTHER;
		}

		// Hedge block - damages like cactus
		if (block instanceof HedgeBlock) {
			return PathType.DANGER_OTHER;
		}

		// Fire jet - fire damage when active
		if (block instanceof FireJetBlock) {
			return state.getValue(FireJetBlock.STATE) == FireJetVariant.IDLE ? original : PathType.DAMAGE_FIRE;
		}

		// Banister - treated as a fence
		if (block instanceof BanisterBlock) {
			return PathType.FENCE;
		}

		return original;
	}
}