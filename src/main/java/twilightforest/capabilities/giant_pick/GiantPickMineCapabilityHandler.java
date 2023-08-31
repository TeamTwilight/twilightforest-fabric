package twilightforest.capabilities.giant_pick;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class GiantPickMineCapabilityHandler implements GiantPickMineCapability {

	private long mining;
	private boolean breaking;
	private int giantBlockConversion;

	public GiantPickMineCapabilityHandler(LivingEntity entity) {}

	@Override
	public void setMining(long mining) {
		this.mining = mining;
	}

	@Override
	public long getMining() { // Is block breaking with the use of a giant pickaxe occurring
		return this.mining;
	}

	@Override
	public void setBreaking(boolean breaking) {
		this.breaking = breaking;
	}

	@Override
	public boolean getBreaking() { // Is code in the process of breaking a 4x4x4 cube of blocks
		return this.breaking;
	}

	@Override
	public void setGiantBlockConversion(int giantBlockConversion) {
		this.giantBlockConversion = giantBlockConversion;
	}

	@Override
	public int getGiantBlockConversion() {
		return this.giantBlockConversion;
	}

	@Override
	public boolean canMakeGiantBlock() { // Is code in the process of converting block drops to a giant block drop
		return this.giantBlockConversion > 0;
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putLong("giantPickMining", this.getMining());
		tag.putBoolean("giantPickBreaking", this.getBreaking());
		tag.putInt("giantBlockConversion", this.getGiantBlockConversion());
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		this.setMining(nbt.getLong("giantPickMining"));
		this.setBreaking(nbt.getBoolean("giantPickBreaking"));
		this.setGiantBlockConversion(nbt.getInt("giantBlockConversion"));
	}
}
