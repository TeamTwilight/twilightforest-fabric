package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

/**
 * [VanillaCopy] Replaced all BlockState Checking/Setting with stateIsWaterlogged and setWaterlogging
 */
public interface WaterloggedBlock extends BucketPickup, LiquidBlockContainer {
	boolean isStateWaterlogged(BlockState state);

	BlockState setWaterlog(BlockState prior, boolean doWater);

	default boolean canPlaceLiquid(BlockGetter getter, BlockPos pos, BlockState state, Fluid fluid) {
		return !this.isStateWaterlogged(state) && fluid == Fluids.WATER;
	}

	default boolean placeLiquid(LevelAccessor accessor, BlockPos pos, BlockState state, FluidState fluidState) {
		if (!this.isStateWaterlogged(state) && fluidState.getType() == Fluids.WATER) {
			if (!accessor.isClientSide()) {
				accessor.setBlock(pos, this.setWaterlog(state, true), 3);
				accessor.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(accessor));
			}

			return true;
		} else {
			return false;
		}
	}

	default ItemStack pickupBlock(LevelAccessor accessor, BlockPos pos, BlockState state) {
		if (this.isStateWaterlogged(state)) {
			accessor.setBlock(pos, this.setWaterlog(state, false), 3);
			if (!state.canSurvive(accessor, pos)) {
				accessor.destroyBlock(pos, true);
			}

			return new ItemStack(Items.WATER_BUCKET);
		} else {
			return ItemStack.EMPTY;
		}
	}

	default Optional<SoundEvent> getPickupSound() {
		return Fluids.WATER.getPickupSound();
	}
}
