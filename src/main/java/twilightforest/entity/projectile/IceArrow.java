package twilightforest.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMobEffects;

public class IceArrow extends TFArrow {

	public IceArrow(EntityType<? extends IceArrow> type, Level world) {
		super(type, world);
	}

	public IceArrow(Level world, Entity shooter) {
		super(TFEntities.ICE_ARROW.get(), world, shooter);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getLevel().isClientSide() && !this.inGround) {
			BlockState stateId = Blocks.SNOW.defaultBlockState();
			for (int i = 0; i < 4; ++i) {
				this.getLevel().addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, stateId), this.getX() + this.getDeltaMovement().x() * i / 4.0D, this.getY() + this.getDeltaMovement().y() * i / 4.0D, this.getZ() + this.getDeltaMovement().z() * i / 4.0D, -this.getDeltaMovement().x(), -this.getDeltaMovement().y() + 0.2D, -this.getDeltaMovement().z());
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.getLevel().isClientSide() && result.getEntity() instanceof LivingEntity living) {
			int chillLevel = 2;
			living.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 20 * 10, chillLevel));
		}
	}
}
