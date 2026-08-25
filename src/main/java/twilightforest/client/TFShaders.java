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

public final class TFShaders {

	@Nullable
	public static ShaderInstance RED_THREAD;

	@Nullable
	public static ShaderInstance AURORA;

	@Nullable
	private static Uniform AURORA_SEED;

	@Nullable
	private static Uniform AURORA_POSITION;

	public static void registerShaders() {
		CoreShaderRegistrationCallback.EVENT.register(context -> {
			context.register(
				TwilightForestMod.prefix("red_thread/red_thread"),
				DefaultVertexFormat.BLOCK,
				shader -> RED_THREAD = shader
			);

			context.register(
				TwilightForestMod.prefix("aurora/aurora"),
				DefaultVertexFormat.POSITION_COLOR,
				shader -> {
					AURORA = shader;
					AURORA_SEED = shader.getUniform("SeedContext");
					AURORA_POSITION = shader.getUniform("PositionContext");
				}
			);
		});
	}

	public static void invokeAurora(int seed, float x, float y, float z, BufferBuilder builder) {
		ShaderInstance shader = AURORA;
		if (shader == null) {
			return;
		}

		ShaderInstance previousShader = RenderSystem.getShader();
		RenderSystem.setShader(() -> shader);

		if (AURORA_SEED != null) {
			AURORA_SEED.set(seed);
		}

		if (AURORA_POSITION != null) {
			AURORA_POSITION.set(x, y, z);
		}

		shader.apply();
		BufferUploader.drawWithShader(builder.buildOrThrow());

		if (AURORA_SEED != null) {
			AURORA_SEED.set(0);
		}

		if (AURORA_POSITION != null) {
			AURORA_POSITION.set(0.0F, 0.0F, 0.0F);
		}

		shader.clear();

		RenderSystem.setShader(() -> previousShader);
	}
}