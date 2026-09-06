package twilightforest.asm.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.potions.FrostedEffect;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void twilightforest$addCustomRenderData(
		T entity,
		S state,
		float partialTicks,
		CallbackInfo ci
	) {
		state.setData(ShieldLayer.SHIELD_COUNT_KEY, ShieldLayer.getShieldCount(entity));

		AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed == null)
			return;

		AttributeModifier frost = speed.getModifier(FrostedEffect.MOVEMENT_SPEED_MODIFIER);
		if (frost == null)
			return;

		state.setData(IceLayer.FROST_COUNT_KEY, frost.amount());
		state.setData(IceLayer.FROST_ID_KEY, entity.getId());
	}
}