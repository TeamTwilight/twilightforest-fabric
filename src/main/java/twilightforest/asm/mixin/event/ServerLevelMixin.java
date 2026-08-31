package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.event.TravellersGearEventHooks;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@WrapOperation(
		method = "tickNonPassenger(Lnet/minecraft/world/entity/Entity;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;tick()V"
		)
	)
	private void twilightforest$entityTick(
		Entity instance,
		Operation<Void> original
	) {
		boolean canceled = false;

		// EntityTickEvent.Pre events go here and need to set canceled...

		if (!canceled) {
			original.call(instance);

			// EntityTickEvent.Post events go here...
			TravellersGearEventHooks.updateOtherModifiers(instance);
		}
	}
}