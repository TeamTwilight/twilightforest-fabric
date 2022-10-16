package twilightforest.capabilities.giant_pick;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class GiantPickMineCapabilityHandler implements GiantPickMineCapability {

	private long mining;

	public GiantPickMineCapabilityHandler(LivingEntity entity) {}

	@Override
	public void setMining(long mining) {
		this.mining = mining;
	}

	@Override
	public long getMining() {
		return this.mining;
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putLong("giantPickMining", this.getMining());
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		this.setMining(nbt.getLong("giantPickMining"));
	}
}
