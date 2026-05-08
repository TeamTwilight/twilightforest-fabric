package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.EncasedSmokerBlock;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

/**
 * 1:1 port of upstream {@code twilightforest.block.entity.TFSmokerBlockEntity}.
 *
 * <p>Client-side ticker that emits {@code TFParticleType.HUGE_SMOKE} every 4th tick
 * when the host block is a SMOKER (always active) or an ENCASED_SMOKER with ACTIVE=true.
 * The particle's horizontal velocity rotates with the counter to make a swirling
 * column. Server side is dormant (no save data).</p>
 */
public class TFSmokerBlockEntity extends BlockEntity {

	private long counter = 0;

	public TFSmokerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.SMOKER, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TFSmokerBlockEntity te) {
		if (level.isClientSide() && ++te.counter % 4 == 0) {
			if (state.getBlock() == TFBlocks.ENCASED_SMOKER.get() && state.getValue(EncasedSmokerBlock.ACTIVE)) {
				te.particles(level, pos, te);
			} else if (state.getBlock() == TFBlocks.SMOKER.get()) {
				te.particles(level, pos, te);
			}
		}
	}

	public void particles(Level level, BlockPos pos, TFSmokerBlockEntity te) {
		level.addParticle(TFParticleType.HUGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5,
			Math.cos(te.counter / 10.0) * 0.05, 0.25D, Math.sin(te.counter / 10.0) * 0.05
		);
	}
}
