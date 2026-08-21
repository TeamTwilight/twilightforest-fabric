package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.hooks.EventHooks;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

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
		if (!EventHooks.fireEntityTickPre(instance).isCanceled()) {
			original.call(instance);
			EventHooks.fireEntityTickPost(instance);
		}
	}
}