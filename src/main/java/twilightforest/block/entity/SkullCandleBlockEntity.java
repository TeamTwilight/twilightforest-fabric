package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

public class SkullCandleBlockEntity extends SkullBlockEntity {

	private int candleColor;
	private int candleAmount;

	private int animationTickCount;
	private boolean isAnimating;

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

	public int getCandleColor() {
		return this.candleColor;
	}

	public int getCandleAmount() {
		return this.candleAmount;
	}

	public void setCandleColor(int color) {
		this.candleColor = color;
	}

	public void setCandleAmount(int amount) {
		this.candleAmount = amount;
	}

	public void incrementCandleAmount() {
		this.candleAmount++;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SkullCandleBlockEntity entity) {
		if (level.hasNeighborSignal(pos)) {
			entity.isAnimating = true;
			++entity.animationTickCount;
		} else {
			entity.isAnimating = false;
		}

	}

	public float getAnimation(float pPartialTick) {
		return this.isAnimating ? (float)this.animationTickCount + pPartialTick : (float)this.animationTickCount;
	}
}
