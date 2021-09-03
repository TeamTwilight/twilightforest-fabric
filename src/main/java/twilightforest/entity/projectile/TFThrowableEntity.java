package twilightforest.entity.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public abstract class TFThrowableEntity extends ThrowableProjectile implements ITFProjectile {

	public TFThrowableEntity(EntityType<? extends TFThrowableEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	public TFThrowableEntity(EntityType<? extends TFThrowableEntity> type, Level worldIn, double x, double y, double z) {
		super(type, x, y, z, worldIn);
	}

	public TFThrowableEntity(EntityType<? extends TFThrowableEntity> type, Level worldIn, LivingEntity throwerIn) {
		super(type, throwerIn, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
