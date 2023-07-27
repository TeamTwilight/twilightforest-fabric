package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import twilightforest.entity.monster.BlockChainGoblin;

public class Chain extends BlockChainGoblin.MultipartGenericsAreDumb {

	public Chain(Entity parent) {
		super(parent);
		this.realSize = EntityDimensions.scalable(0.25F, 0.25F);
	}

	@Override
	public EntityDimensions getDimensions(Pose pos) {
		return EntityDimensions.scalable(0.25F, 0.25F);
	}

	@Override
	protected void defineSynchedData() {

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
