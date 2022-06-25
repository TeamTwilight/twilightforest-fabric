package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import twilightforest.entity.monster.BlockChainGoblin;

public class Chain extends BlockChainGoblin.MultipartGenericsAreDumb {

	public Chain(Entity parent) {
		super(parent);
	}

	@Override
	protected void defineSynchedData() {
		this.realSize = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}
}
