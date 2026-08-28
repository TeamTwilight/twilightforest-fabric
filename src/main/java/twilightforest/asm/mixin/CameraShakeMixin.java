package twilightforest.asm.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.event.ClientGameEvents;
import twilightforest.config.TFConfig;

/**
 * Recreates NeoForge's ViewportEvent.ComputeCameraAngles handling for the
 * beanstalk earthquake screen shake.
 */
@Mixin(Camera.class)
public class CameraShakeMixin {

	@Shadow
	@Final
	private org.joml.Quaternionf rotation;

	@Inject(method = "setRotation(FF)V", at = @At("TAIL"))
	private void twilightforest$applyShake(float yaw, float pitch, CallbackInfo ci) {
		if (!ClientGameEvents.shouldShakeCamera()) return;
		Minecraft minecraft = Minecraft.getInstance();
		float intensity = ClientGameEvents.consumeShakeIntensity();
		if (minecraft.player == null) return;
		float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
		float yawDelta = (minecraft.player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity;
		float pitchDelta = (minecraft.player.getRandom().nextFloat() * 2.5F - 1.25F) * intensity;
		float rollDelta = (minecraft.player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity;
		float yawDeg = (yaw + Mth.lerp(partialTick, 0.0F, yawDelta)) * Mth.DEG_TO_RAD;
		float pitchDeg = (pitch + Mth.lerp(partialTick, 0.0F, pitchDelta)) * Mth.DEG_TO_RAD;
		this.rotation.rotationYXZ(yawDeg, pitchDeg, rollDelta * Mth.DEG_TO_RAD);
	}
}
