package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

public class SkullCandleBlockEntity extends SkullBlockEntity {

	public int candleColor;
	public int candleAmount;

	public SkullCandleBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public SkullCandleBlockEntity(BlockPos pos, BlockState state, int color, int amount) {
		super(pos, state);
		this.candleColor = color;
		this.candleAmount = amount;
	}

	@Override
	public BlockEntityType<?> getType() {
		return TFBlockEntities.SKULL_CANDLE.get();
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("CandleColor", this.candleColor);
		if (this.candleAmount != 0) tag.putInt("CandleAmount", this.candleAmount);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.candleColor = tag.getInt("CandleColor");
		this.candleAmount = tag.getInt("CandleAmount");
	}
}
