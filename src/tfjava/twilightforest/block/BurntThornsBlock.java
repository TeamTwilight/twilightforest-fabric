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

/**
 * 1:1 port of upstream {@code twilightforest.block.BurntThornsBlock}.
 *
 * <p>Subclass of {@link ThornsBlock} that dissolves on living/projectile contact instead
 * of dealing damage, returns null path-type (mob AI ignores it entirely), and destroys
 * itself when broken — preserving any liquid that was waterlogging it. </p>
 *
 * <p>Codex Fabric port note: upstream NeoForge had {@code @Override Block#getBlockPathType}
 * (NF patch) and {@code @Override Block#onDestroyedByPlayer} (NF-only callback). Vanilla
 * Fabric 1.21.1 has no equivalent hooks on {@link Block} — pathfinding goes through
 * {@code isPathfindable(...)}, and player-break replacement happens via the LiquidBlock
 * system. Both NF-only methods are kept here without {@code @Override} so a future
 * Fabric mixin or content-registry hookup can re-attach them; the 1:1 behaviour intent
 * is preserved in source rather than discarded.</p>
 */
public class BurntThornsBlock extends ThornsBlock {

	public BurntThornsBlock(Properties properties) {
		super(properties);
	}

	// Codex Fabric port note: kept body 1:1 with upstream so future mixin can re-attach.
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return null;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		// dissolve
		if (!level.isClientSide() && (entity instanceof LivingEntity || entity instanceof Projectile)) {
			level.destroyBlock(pos, false);
		}
	}

	// Codex Fabric port note: kept body 1:1 with upstream so future mixin can re-attach.
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		state.getBlock().playerWillDestroy(level, pos, state, player);
		return level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide() ? Block.UPDATE_ALL_IMMEDIATE : Block.UPDATE_ALL);
	}
}
