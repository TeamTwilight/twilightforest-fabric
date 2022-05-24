package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.Minoshroom;

public class MinoshroomSpawnerBlockEntity extends BossSpawnerBlockEntity<Minoshroom> {

	public MinoshroomSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.MINOSHROOM_SPAWNER.get(), TFEntities.MINOSHROOM.get(), pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() < worldPosition.getY() + 4;
	}

	@Override
	public ParticleOptions getSpawnerParticle() {
		return ParticleTypes.CRIMSON_SPORE;
	}
}
