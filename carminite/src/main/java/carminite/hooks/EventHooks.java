package carminite.hooks;

import carminite.event.ArrowLooseEvent;
import carminite.event.EntityStruckByLightningEvent;
import carminite.event.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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

	public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
		var event = new ArrowLooseEvent.ArrowLooseEventImpl(player, stack, level, charge, hasAmmo);
		ArrowLooseEvent.EVENT.invoker().onArrowLoose(event);
		if (event.isCanceled())
			return -1;
		return event.getCharge();
	}
}