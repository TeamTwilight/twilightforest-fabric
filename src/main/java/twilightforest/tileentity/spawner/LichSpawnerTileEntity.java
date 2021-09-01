package twilightforest.tileentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.LichEntity;
import twilightforest.tileentity.TFTileEntities;

public class LichSpawnerTileEntity extends BossSpawnerTileEntity<LichEntity> {

	public LichSpawnerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.LICH_SPAWNER, TFEntities.lich, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
