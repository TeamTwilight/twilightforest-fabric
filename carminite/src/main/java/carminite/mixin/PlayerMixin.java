package carminite.mixin;

import carminite.extensions.IPlayerExtension;
import carminite.hooks.EventHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerExtension {

	@Override
	public boolean carminite$hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
		return EventHooks.doPlayerHarvestCheck((Player) (Object) this, state, level, pos);
	}
}