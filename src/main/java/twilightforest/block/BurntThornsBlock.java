package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BurntThornsBlock extends ThornsBlock {

	public BurntThornsBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		// dissolve
		if (!level.isClientSide() && (entity instanceof LivingEntity || entity instanceof Projectile)) {
			level.destroyBlock(pos, false);
		}
	}
}
