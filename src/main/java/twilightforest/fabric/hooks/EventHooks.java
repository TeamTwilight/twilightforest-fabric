package twilightforest.fabric.hooks;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import twilightforest.fabric.events.neo.*;
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
		return new EntityStruckByLightningEvent(entity, bolt).post().isCanceled();
	}

	public static boolean doPlayerHarvestCheck(Player player, BlockState state, BlockGetter level, BlockPos pos) {
		boolean vanillaValue = player.hasCorrectToolForDrops(state);
		return new PlayerEvent.HarvestCheck(player, state, level, pos, vanillaValue).post().canHarvest();
	}

	public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
		var event = new ArrowLooseEvent(player, stack, level, charge, hasAmmo).post();
		if (event.isCanceled())
			return -1;
		return event.getCharge();
	}

	public static void onStartEntityTracking(Entity entity, Player player) {
		new PlayerEvent.StartTracking(player, entity).post();
	}

	public static void onStopEntityTracking(Entity entity, Player player) {
		new PlayerEvent.StopTracking(player, entity).post();
	}

	public static EntityTickEvent.Pre fireEntityTickPre(Entity entity) {
		return new EntityTickEvent.Pre(entity).post();
	}

	public static void fireEntityTickPost(Entity entity) {
		new EntityTickEvent.Post(entity).post();
	}

	public static void firePlayerTickPre(Player player) {
		new PlayerTickEvent.Pre(player).post();
	}

	public static void firePlayerTickPost(Player player) {
		new PlayerTickEvent.Post(player).post();
	}

	public static void firePlayerLoggedIn(Player player) {
		new PlayerEvent.PlayerLoggedInEvent(player).post();
	}

	public static void firePlayerLoggedOut(Player player) {
		new PlayerEvent.PlayerLoggedOutEvent(player).post();
	}

	public static void firePlayerRespawnEvent(ServerPlayer player, boolean fromEndFight) {
		new PlayerEvent.PlayerRespawnEvent(player, fromEndFight).post();
	}

	@ApiStatus.Internal
	public static void onAdvancementEarnedEvent(Player player, AdvancementHolder earned) {
		new AdvancementEvent.AdvancementEarnEvent(player, earned).post();
	}
}