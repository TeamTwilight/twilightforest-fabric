package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.tags.TFDimensionTypeTags;

import java.util.Objects;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow
	@Final
	private LevelRenderState levelRenderState;

	@WrapOperation(
		method = "lambda$addWeatherPass$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;render(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V"
		)
	)
	private void twilightforest$renderSnowAndRain(
		WeatherEffectRenderer instance,
		Vec3 cameraPos,
		WeatherRenderState renderState,
		Operation<Void> original
	) {
		if (Objects.requireNonNull(Minecraft.getInstance().level).dimensionTypeRegistration().is(TFDimensionTypeTags.TF_WEATHER_RENDERER) && TwilightForestRenderInfo.INSTANCE.renderSnowAndRain(this.levelRenderState, cameraPos)) {
			return;
		}

		original.call(instance, cameraPos, renderState);
	}

	@WrapMethod(method = "lambda$addSkyPass$0")
	private void twilightforest$renderSky(
		GpuBufferSlice skyFog,
		SkyRenderState state,
		Operation<Void> original
	) {
		if (Objects.requireNonNull(Minecraft.getInstance().level).dimensionTypeRegistration().is(TFDimensionTypeTags.TF_SKY_RENDERER) && TwilightForestRenderInfo.INSTANCE.renderSky(this.levelRenderState, state, () -> RenderSystem.setShaderFog(skyFog))) {
			return;
		}

		original.call(skyFog, state);
	}
}