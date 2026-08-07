package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.MultipartHooks;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@Inject(
		method = "addEntity",
		at = @At("HEAD")
	)
	private void twilightforest$onAddEntity(
		Entity entity,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (entity != null) {
			MultipartHooks.sendDirtyEntityData(entity);
		}
	}
}