package twilightforest.client;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.io.IOException;

public class TFShaders {

	public static ShaderInstance RED_THREAD;
	public static PositionAwareShaderInstance AURORA;

	public static void registerShaders() {
		// Defer shader loading to after client is fully initialized and resources are available
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			loadShaders();
		});
	}

	private static void loadShaders() {
		ResourceProvider resourceProvider = Minecraft.getInstance().getResourceManager();
		try {
			// Use "modid:path" format - ShaderInstanceMixin will handle the namespace
			RED_THREAD = new ShaderInstance(resourceProvider, "twilightforest:red_thread/red_thread", DefaultVertexFormat.BLOCK);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.warn("Failed to load red_thread shader: {}", e.getMessage());
		}
		try {
			AURORA = new PositionAwareShaderInstance(resourceProvider, "twilightforest:aurora/aurora", DefaultVertexFormat.POSITION_COLOR);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.warn("Failed to load aurora shader: {}", e.getMessage());
		}
	}

	public static class BindableShaderInstance extends ShaderInstance {

		private ShaderInstance last;

		public BindableShaderInstance(ResourceProvider p_173336_, String shaderLocation, VertexFormat p_173338_) throws IOException {
			super(p_173336_, shaderLocation, p_173338_);
		}

		ShaderInstance getSelf() {
			return this;
		}

		public final void bind(@Nullable Runnable exec) {
			last = RenderSystem.getShader();
			RenderSystem.setShader(this::getSelf);
			if (exec != null)
				exec.run();
			apply();
		}

		public final void runThenClear(Runnable exec) {
			exec.run();
			clear();
			RenderSystem.setShader(() -> last);
			last = null;
		}

		public final void invokeThenClear(@Nullable Runnable execBind, Runnable execPost) {
			bind(execBind);
			runThenClear(execPost);
		}

		public final void invokeThenClear(Runnable execPost) {
			invokeThenClear(null, execPost);
		}

		public final void invokeThenEndTesselator(@Nullable Runnable execBind, BufferBuilder builder) {
			invokeThenClear(execBind, () -> BufferUploader.drawWithShader(builder.buildOrThrow()));
		}

		public final void invokeThenEndTesselator(BufferBuilder builder) {
			invokeThenClear(() -> BufferUploader.drawWithShader(builder.buildOrThrow()));
		}

	}

	public static class PositionAwareShaderInstance extends BindableShaderInstance {

		@Nullable
		public final Uniform SEED;

		@Nullable
		public final Uniform POSITION;

		public PositionAwareShaderInstance(ResourceProvider p_173336_, String shaderLocation, VertexFormat p_173338_) throws IOException {
			super(p_173336_, shaderLocation, p_173338_);
			SEED = getUniform("SeedContext");
			POSITION = getUniform("PositionContext");
		}

		public final void setValue(int seed, float x, float y, float z) {
			if (SEED != null) {
				SEED.set(seed);
			}
			if (POSITION != null) {
				POSITION.set(x, y, z);
			}
		}

		public final void setValueBindApply(int seed, float x, float y, float z) {
			bind(() -> setValue(seed, x, y, z));
		}

		public final void reset() {
			setValue(0, 0, 0, 0);
		}

		public final void resetClear() {
			runThenClear(this::reset);
		}

		public final void invokeThenClear(int seed, float x, float y, float z, Runnable exec) {
			setValueBindApply(seed, x, y, z);
			exec.run();
			resetClear();
		}

		public final void invokeThenEndTesselator(int seed, float x, float y, float z, BufferBuilder builder) {
			invokeThenClear(seed, x, y, z, () -> BufferUploader.drawWithShader(builder.buildOrThrow()));
		}

	}

}