package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.Lich;
import twilightforest.block.entity.TFBlockEntities;

public class LichSpawnerBlockEntity extends BossSpawnerBlockEntity<Lich> {

	public LichSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.LICH_SPAWNER, TFEntities.lich, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
