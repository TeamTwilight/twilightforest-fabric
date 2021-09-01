package twilightforest.tileentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.SnowQueenEntity;
import twilightforest.tileentity.TFTileEntities;

public class SnowQueenSpawnerTileEntity extends BossSpawnerTileEntity<SnowQueenEntity> {

	public SnowQueenSpawnerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.SNOW_QUEEN_SPAWNER, TFEntities.snow_queen, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
