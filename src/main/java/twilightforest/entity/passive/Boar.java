package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class Boar extends Pig {

	public Boar(EntityType<? extends Boar> type, Level world) {
		super(type, world);
	}

	@Override
	public Pig getBreedOffspring(ServerLevel world, AgeableMob ageableMob) {
		return TFEntities.BOAR.get().create(world);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BOAR_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.BOAR_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BOAR_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.BOAR_STEP.get(), 0.15F, 1.0F);
	}
}
