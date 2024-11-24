package twilightforest.client;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.io.IOException;

public class TFShaders {

	public static ShaderInstance RED_THREAD;
	public static ShaderInstance AURORA;
	public static PositionAwareShaderUtil AURORA_POSAWARE;

	public static void init() {
		CoreShaderRegistrationCallback.EVENT.register(context -> {
			try {
				context.register(TwilightForestMod.prefix("red_thread/red_thread"), DefaultVertexFormat.BLOCK, shader -> RED_THREAD = shader);
				context.register(TwilightForestMod.prefix("aurora/aurora"), DefaultVertexFormat.POSITION_COLOR, shader -> {
					AURORA = shader;
					AURORA_POSAWARE = new PositionAwareShaderUtil(AURORA);
				});
			} catch (IOException e) {
				TwilightForestMod.LOGGER.error(e);
			}
		});
	}

	public static class BindableShaderInstanceUtil {

		private ShaderInstance last;
		public final ShaderInstance thiz;

		public BindableShaderInstanceUtil(ShaderInstance shaderInstance) {
			thiz = shaderInstance;
		}

		ShaderInstance getSelf() {
			return thiz;
		}

		public final void bind(@Nullable Runnable exec) {
			last = RenderSystem.getShader();
			RenderSystem.setShader(this::getSelf);
			if (exec != null)
				exec.run();
			thiz.apply();
		}

		public final void runThenClear(Runnable exec) {
			exec.run();
			thiz.clear();
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

		public final void invokeThenEndTesselator(@Nullable Runnable execBind) {
			invokeThenClear(execBind, () -> Tesselator.getInstance().end());
		}

		public final void invokeThenEndTesselator() {
			invokeThenClear(() -> Tesselator.getInstance().end());
		}

	}

	public static class PositionAwareShaderUtil extends BindableShaderInstanceUtil {

		@Nullable
		public final Uniform SEED;

		@Nullable
		public final Uniform POSITION;

		public PositionAwareShaderUtil(ShaderInstance shaderInstance) {
			super(shaderInstance);
			SEED = thiz.getUniform("SeedContext");
			POSITION = thiz.getUniform("PositionContext");
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

		public final void invokeThenEndTesselator(int seed, float x, float y, float z) {
			invokeThenClear(seed, x, y, z, () -> Tesselator.getInstance().end());
		}

	}
}
