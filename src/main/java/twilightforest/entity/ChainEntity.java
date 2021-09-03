package twilightforest.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.nbt.CompoundTag;

public class ChainEntity extends BlockChainGoblinEntity.MultipartGenericsAreDumb {

	public ChainEntity(Entity parent) {
		super(parent);
	}

	@Override
	protected void defineSynchedData() {
		realSize = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {

	}

}
