package twilightforest.tileentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.PlateauBossEntity;
import twilightforest.tileentity.TFTileEntities;

public class FinalBossSpawnerTileEntity extends BossSpawnerTileEntity<PlateauBossEntity> {

	public FinalBossSpawnerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.FINAL_BOSS_SPAWNER, TFEntities.plateau_boss, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BossSpawnerTileEntity<?> te) {

	}
}
