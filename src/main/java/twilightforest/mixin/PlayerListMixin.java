package twilightforest.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TFConfig;
import twilightforest.events.CapabilityEvents;
import twilightforest.events.ProgressionEvents;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Inject(method = "placeNewPlayer", at = @At("TAIL")) // fabric event fires too early
	private void twilightforest$onLoginTail(Connection netManager, ServerPlayer player, CallbackInfo ci) {
		CapabilityEvents.playerLogsIn(player);
		ProgressionEvents.playerLogsIn(player);
		TFConfig.ConfigSync.syncConfigOnLogin(player);
	}
}
