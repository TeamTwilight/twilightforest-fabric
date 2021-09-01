package twilightforest.tileentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.NagaEntity;
import twilightforest.tileentity.TFTileEntities;

public class NagaSpawnerTileEntity extends BossSpawnerTileEntity<NagaEntity> {

	public NagaSpawnerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.NAGA_SPAWNER, TFEntities.naga, pos, state);
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}
}
