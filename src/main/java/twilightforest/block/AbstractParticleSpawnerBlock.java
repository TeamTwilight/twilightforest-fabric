package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public abstract class AbstractParticleSpawnerBlock extends Block {

	public AbstractParticleSpawnerBlock(Properties p_49795_) {
		super(p_49795_);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for(int partCount = 0; partCount < getParticleCountPerSpawn(); ++partCount) {
			mutablePos.set(x + Mth.nextInt(rand, -10, 10), y + Mth.nextInt(rand, -10, 10), z + Mth.nextInt(rand, -10, 10));
			BlockState var16 = world.getBlockState(mutablePos);
			if (!var16.isCollisionShapeFullBlock(world, mutablePos)) {
				world.addParticle((ParticleOptions) getParticlesToSpawn(), (double)mutablePos.getX() + rand.nextDouble(), (double)mutablePos.getY() + rand.nextDouble(), (double)mutablePos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public abstract ParticleType<?> getParticlesToSpawn();

	public abstract int getParticleCountPerSpawn();
}
