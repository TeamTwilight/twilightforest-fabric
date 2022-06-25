package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.boss.Naga;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;

public class NagaSpawnerBlockEntity extends BossSpawnerBlockEntity<Naga> {

	public NagaSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.NAGA_SPAWNER.get(), TFEntities.NAGA.get(), pos, state);
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}

	@Override
	public ParticleOptions getSpawnerParticle() {
		return ParticleTypes.CRIT;
	}
}
