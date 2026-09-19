package twilightforest.asm.hooks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public interface AddBreakingBlockEffectDuck {
	void twilightforest$addBreakingBlockEffect(final BlockPos pos, final Direction direction, @Nullable HitResult hitResult);
}