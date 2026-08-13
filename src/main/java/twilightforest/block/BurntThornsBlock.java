package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class BurntThornsBlock extends ThornsBlock {

	public BurntThornsBlock(Properties properties) {
		super(properties);
	}

	// NOTE: getBlockPathType is NeoForge-specific. No equivalent in vanilla/Fabric 1.21.1.
	// PathType is handled differently in Fabric (via path node evaluators or mixins).
	/*
	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return null;
	}
	*/

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		// dissolve
		if (!level.isClientSide() && (entity instanceof LivingEntity || entity instanceof Projectile)) {
			level.destroyBlock(pos, false);
		}
	}
}
