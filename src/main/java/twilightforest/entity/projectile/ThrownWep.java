package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.init.TFDamageSources;
import twilightforest.init.TFItems;

public class ThrownWep extends TFThrowable {

	private static final EntityDataAccessor<ItemStack> DATA_ITEMSTACK = SynchedEntityData.defineId(ThrownWep.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Float> DATA_VELOCITY = SynchedEntityData.defineId(ThrownWep.class, EntityDataSerializers.FLOAT);

	private float projectileDamage = 6;

	public ThrownWep(EntityType<? extends ThrownWep> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public ThrownWep(EntityType<? extends ThrownWep> type, Level world) {
		super(type, world);
	}

	public ThrownWep setDamage(float damage) {
		this.projectileDamage = damage;
		return this;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ITEMSTACK, ItemStack.EMPTY);
		this.entityData.define(DATA_VELOCITY, 0.001F);
	}

	public ThrownWep setItem(ItemStack stack) {
		this.entityData.set(DATA_ITEMSTACK, stack);
		return this;
	}

	public ItemStack getItem() {
		return this.entityData.get(DATA_ITEMSTACK);
	}

	public ThrownWep setVelocity(float velocity) {
		this.entityData.set(DATA_VELOCITY, velocity);
		return this;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.getLevel().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof KnightPhantom || result.getEntity() == this.getOwner()) {
			return;
		}

		if (!this.getLevel().isClientSide()) {
			result.getEntity().hurt(this.getItem().getItem() == TFItems.KNIGHTMETAL_PICKAXE.get() ? TFDamageSources.THROWN_PICKAXE : TFDamageSources.THROWN_AXE, projectileDamage);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!this.getLevel().isClientSide()) {
			this.getLevel().broadcastEntityEvent(this, (byte) 3);
			discard();
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
		return this.entityData.get(DATA_VELOCITY);
	}
}
