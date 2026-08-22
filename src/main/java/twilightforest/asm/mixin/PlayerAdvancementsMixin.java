package twilightforest.asm.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

	@Shadow
	private ServerPlayer player;

	@Inject(
		method = "lambda$award$0",
		at = @At("RETURN")
	)
	private void twilightforest$advancementEarned(
		AdvancementHolder holder,
		DisplayInfo display,
		CallbackInfo ci
	) {
		EventHooks.onAdvancementEarnedEvent(this.player, holder);
	}
}