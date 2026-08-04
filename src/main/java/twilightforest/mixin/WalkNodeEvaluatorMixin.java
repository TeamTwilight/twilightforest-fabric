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
	private static PathType twilightforest$modifyBlockPathType(
		PathType original,
		BlockGetter level,
		BlockPos pos
	) {
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		// Damage-dealing blocks
		if (block instanceof KnightmetalBlock || block instanceof BurntThornsBlock || block instanceof ThornsBlock || block instanceof OreBerryBlock) {
			return PathType.DAMAGE_OTHER;
		}

		// Hedge block - damages like cactus
		return switch (block) {
			case HedgeBlock hedgeBlock -> PathType.DANGER_OTHER;


			// Fire jet - fire damage when active
			case FireJetBlock fireJetBlock -> state.getValue(FireJetBlock.STATE) == FireJetVariant.IDLE ? original : PathType.DAMAGE_FIRE;


			// Banister - treated as a fence
			case BanisterBlock banisterBlock -> PathType.FENCE;
			default -> original;
		};
	}
}