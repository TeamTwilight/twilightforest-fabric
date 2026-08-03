package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.init.custom.TravellersModifiersManager;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

	@WrapWithCondition(
		method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"
		)
	)
	private boolean twilightforest$cancelPhantomSpawn(
		ServerLevel level,
		Entity entity,
		@Local ServerPlayer serverPlayer
	) {
		return !(entity instanceof Phantom)
			|| !TravellersModifiersManager.isModifierActive(serverPlayer, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
	}
}