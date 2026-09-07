package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.init.TFDimension;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow
	@Final
	private LevelRenderState levelRenderState;

	@WrapOperation(
		method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"
		)
	)
	private void twilightforest$renderSky(
		FramePass instance,
		Runnable original,
		Operation<Void> operation,
		@Local(name = "state") SkyRenderState state,
		@Local(argsOnly = true, name = "skyFog") GpuBufferSlice skyFog
	) {
		boolean isTwilightForest = Minecraft.getInstance().level != null
			&& Minecraft.getInstance().level.dimension().equals(TFDimension.DIMENSION_KEY);

		Runnable replacement = () -> {
			if (isTwilightForest) {
				TwilightForestRenderInfo.INSTANCE.renderSky(
					this.levelRenderState,
					state,
					RenderSystem.getModelViewMatrix(),
					() -> RenderSystem.setShaderFog(skyFog)
				);
			} else {
				original.run();
			}
		};

		operation.call(instance, replacement);
	}
}