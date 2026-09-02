package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
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

	@WrapWithCondition(
		method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"
		)
	)
	private boolean twilightforest$renderSky(
		FramePass instance,
		Runnable runnable,
		@Local(name = "state") SkyRenderState state,
		@Local(argsOnly = true, name = "skyFog") GpuBufferSlice skyFog
	) {
		return !Minecraft.getInstance().level.dimension().equals(TFDimension.DIMENSION_KEY)
			|| !TwilightForestRenderInfo.INSTANCE.renderSky(this.levelRenderState, state, RenderSystem.getModelViewMatrix(), () -> RenderSystem.setShaderFog(skyFog));
	}
}