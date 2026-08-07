package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class TFAuxiliaryLightManager {

	public static int getLightAt(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getLightEmission();
	}
}