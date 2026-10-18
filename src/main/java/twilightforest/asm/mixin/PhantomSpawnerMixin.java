package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.init.custom.TravellersModifiersManager;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

	@ModifyExpressionValue(
		method = "tick(Lnet/minecraft/server/level/ServerLevel;Z)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/DifficultyInstance;isHarderThan(F)Z"
		)
	)
	private boolean twilightforest$cancelphantomSpawns(
		boolean original,
		@Local(name = "player") ServerPlayer player
	) {
		return !TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER) && original;
	}
}
