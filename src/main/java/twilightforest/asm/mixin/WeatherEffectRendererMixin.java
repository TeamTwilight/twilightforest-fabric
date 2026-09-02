package twilightforest.asm.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.init.TFDimension;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {

	@Inject(
		method = "render(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$customRainAndSnow(
		Vec3 cameraPos,
		WeatherRenderState renderState,
		CallbackInfo ci
	) {
		if (Minecraft.getInstance().level.dimension().equals(TFDimension.DIMENSION_KEY) && TwilightForestRenderInfo.INSTANCE.renderSnowAndRain(Minecraft.getInstance().renderBuffers().bufferSource(), cameraPos)) {
			ci.cancel();
		}
	}

	@Inject(
		method = "tickRainParticles(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;ILnet/minecraft/server/level/ParticleStatus;I)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$customRainTick(
		ClientLevel level,
		Camera camera,
		int ticks,
		ParticleStatus particleStatus,
		int weatherRadius,
		CallbackInfo ci
	) {
		if (level.dimension().equals(TFDimension.DIMENSION_KEY) && TwilightForestRenderInfo.INSTANCE.tickRain(level, ticks, camera)) {
			ci.cancel();
		}
	}
}