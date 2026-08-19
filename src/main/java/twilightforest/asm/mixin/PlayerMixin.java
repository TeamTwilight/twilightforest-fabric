package twilightforest.asm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import twilightforest.fabric.hooks.EventHooks;
import twilightforest.fabric.interfaces.extension.IPlayerExtension;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerExtension {

	@Override
	public boolean twilightforest$hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
		return EventHooks.doPlayerHarvestCheck((Player) (Object) this, state, level, pos);
	}
}