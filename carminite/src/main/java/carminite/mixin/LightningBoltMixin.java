package carminite.mixin;

import carminite.hooks.EventHooks;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

	@WrapWithCondition(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"
		)
	)
	private boolean carminite$onEntityStruckByLightning(
		Entity entity,
		ServerLevel level,
		LightningBolt lightningBolt
	) {
		return !EventHooks.onEntityStruckByLightning(entity, lightningBolt);
	}
}