package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.MultipartHooks;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@ModifyExpressionValue(
		method = "getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;",
			ordinal = 2
		)
	)
	@SuppressWarnings("unchecked")
	private <T extends Entity> Object twilightforest$resolveEntityRenderer(
		Object renderer,
		T entity
	) {
		return MultipartHooks.resolveEntityRenderer((EntityRenderer<? super T>) renderer, entity);
	}
}
