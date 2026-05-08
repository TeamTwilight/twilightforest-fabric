package twilightforest.client;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

public class TFShaders {
	public static ShaderInstance RED_THREAD;
	public static PositionAwareShaderInstance AURORA;

	public static void bootstrap() {
		CoreShaderRegistrationCallback.EVENT.register(context -> {
			context.register(TwilightForestMod.prefix("red_thread/red_thread"), DefaultVertexFormat.BLOCK, shader -> RED_THREAD = shader);
			context.register(TwilightForestMod.prefix("aurora/aurora"), DefaultVertexFormat.POSITION_COLOR, shader -> AURORA = new PositionAwareShaderInstance(shader));
		});
	}

	public static class BindableShaderInstance {
		protected final ShaderInstance shader;
		private ShaderInstance last;

		public BindableShaderInstance(ShaderInstance shader) {
			this.shader = shader;
		}

		public ShaderInstance getSelf() {
			return this.shader;
		}

		public final void bind(@Nullable Runnable exec) {
			this.last = RenderSystem.getShader();
			RenderSystem.setShader(this::getSelf);
			if (exec != null) {
				exec.run();
			}
			this.shader.apply();
		}

		public final void runThenClear(Runnable exec) {
			exec.run();
			this.shader.clear();
			RenderSystem.setShader(() -> this.last);
			this.last = null;
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

		public PositionAwareShaderInstance(ShaderInstance shader) {
			super(shader);
			this.SEED = shader.getUniform("SeedContext");
			this.POSITION = shader.getUniform("PositionContext");
		}

		public final void setValue(int seed, float x, float y, float z) {
			if (this.SEED != null) {
				this.SEED.set(seed);
			}
			if (this.POSITION != null) {
				this.POSITION.set(x, y, z);
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
