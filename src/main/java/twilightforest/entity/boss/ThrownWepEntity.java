package twilightforest.entity.boss;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.entity.projectile.TFThrowableEntity;
import twilightforest.item.TFItems;
import twilightforest.util.TFDamageSources;

public class ThrownWepEntity extends TFThrowableEntity {

	private static final EntityDataAccessor<ItemStack> DATA_ITEMSTACK = SynchedEntityData.defineId(ThrownWepEntity.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Float> DATA_VELOCITY = SynchedEntityData.defineId(ThrownWepEntity.class, EntityDataSerializers.FLOAT);

	private float projectileDamage = 6;

	public ThrownWepEntity(EntityType<? extends ThrownWepEntity> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public ThrownWepEntity(EntityType<? extends ThrownWepEntity> type, Level world) {
		super(type, world);
	}

	public ThrownWepEntity setDamage(float damage) {
		projectileDamage = damage;
		return this;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_ITEMSTACK, ItemStack.EMPTY);
		entityData.define(DATA_VELOCITY, 0.001F);
	}

	public ThrownWepEntity setItem(ItemStack stack) {
		entityData.set(DATA_ITEMSTACK, stack);
		return this;
	}

	public ItemStack getItem() {
		return entityData.get(DATA_ITEMSTACK);
	}

	public ThrownWepEntity setVelocity(float velocity) {
		entityData.set(DATA_VELOCITY, velocity);
		return this;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (result instanceof EntityHitResult) {
			if (((EntityHitResult)result).getEntity() instanceof KnightPhantomEntity || ((EntityHitResult)result).getEntity() == this.getOwner()) {
				return;
			}

			if (!level.isClientSide) {
				if (((EntityHitResult)result).getEntity() != null) {
					((EntityHitResult)result).getEntity().hurt(this.getItem().getItem() == TFItems.knightmetal_pickaxe.get() ? TFDamageSources.THROWN_PICKAXE : TFDamageSources.THROWN_AXE, projectileDamage);
				}
				level.broadcastEntityEvent(this, (byte) 3);
				discard();
			}
		}
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getPickRadius() {
		return 1.0F;
	}

	@Override
	protected float getGravity() {
		return entityData.get(DATA_VELOCITY);
	}
}
