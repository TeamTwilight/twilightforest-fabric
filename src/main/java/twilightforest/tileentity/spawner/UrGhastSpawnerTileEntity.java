package twilightforest.tileentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.UrGhastEntity;
import twilightforest.tileentity.TFTileEntities;

public class UrGhastSpawnerTileEntity extends BossSpawnerTileEntity<UrGhastEntity> {

	public UrGhastSpawnerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.UR_GHAST_SPAWNER, TFEntities.ur_ghast, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
