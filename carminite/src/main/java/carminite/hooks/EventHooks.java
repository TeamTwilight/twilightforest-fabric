package carminite.hooks;

import carminite.event.EntityStruckByLightningEvent;
import carminite.event.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class EventHooks {
	public static boolean onEntityStruckByLightning(Entity entity, LightningBolt bolt) {
		var event = new EntityStruckByLightningEvent.EntityStruckByLightningEventImpl(entity, bolt);
		EntityStruckByLightningEvent.EVENT.invoker().onEntityStruckByLightning(event);
		return event.isCanceled();
	}

	public static boolean doPlayerHarvestCheck(Player player, BlockState state, BlockGetter level, BlockPos pos) {
		boolean vanillaValue = player.hasCorrectToolForDrops(state);
		var event = new PlayerEvent.HarvestCheckImpl(player, state, level, pos, vanillaValue);
		PlayerEvent.HARVEST.invoker().doPlayerHarvestCheck(event);
		return event.canHarvest();
	}
}