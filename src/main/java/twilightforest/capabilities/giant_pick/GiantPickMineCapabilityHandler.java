package twilightforest.capabilities.giant_pick;

import net.minecraft.nbt.CompoundTag;

public class GiantPickMineCapabilityHandler implements GiantPickMineCapability {

	private long mining;

	@Override
	public void setMining(long mining) {
		this.mining = mining;
	}

	@Override
	public long getMining() {
		return this.mining;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putLong("giantPickMining", this.getMining());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setMining(nbt.getLong("giantPickMining"));
	}
}
