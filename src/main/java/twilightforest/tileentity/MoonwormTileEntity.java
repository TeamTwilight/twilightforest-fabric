package twilightforest.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MoonwormTileEntity extends BlockEntity {
	public int yawDelay;
	public int currentYaw;
	public int desiredYaw;

	public MoonwormTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.MOONWORM, pos, state);
		currentYaw = -1;
		yawDelay = 0;
		desiredYaw = 0;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, MoonwormTileEntity te) {
		if (level.isClientSide) {
			if (te.currentYaw == -1) {
				te.currentYaw = level.random.nextInt(4) * 90;
			}

			if (te.yawDelay > 0) {
				te.yawDelay--;
			} else {
				if (te.desiredYaw == 0) {
					// make it rotate!
					te.yawDelay = 200 + level.random.nextInt(200);
					te.desiredYaw = level.random.nextInt(4) * 90;
				}

				te.currentYaw++;

				if (te.currentYaw > 360) {
					te.currentYaw = 0;
				}

				if (te.currentYaw == te.desiredYaw) {
					te.desiredYaw = 0;
				}
			}
		}
	}
}
