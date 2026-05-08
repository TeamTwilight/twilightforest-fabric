package twilightforest.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TFSkyRenderer {
	private static VertexBuffer starBuffer;

	private TFSkyRenderer() {
	}

	// VanillaCopy: LevelRenderer.renderSky's overworld branch, minus sun/moon/sunrise/sunset,
	// with full-bright Twilight stars and a void horizon lowered to the real build floor.
	public static boolean renderSky(ClientLevel level, float partialTicks, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, Runnable setupFog) {
		if (starBuffer == null) {
			createStars();
		}

		LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;
		PoseStack stack = new PoseStack();
		stack.mulPose(frustumMatrix);

		setupFog.run();
		Vec3 skyColor = level.getSkyColor(camera.getPosition(), partialTicks);
		float red = (float) skyColor.x();
		float green = (float) skyColor.y();
		float blue = (float) skyColor.z();
		FogRenderer.levelFogColor();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(red, green, blue, 1.0F);
		ShaderInstance shader = RenderSystem.getShader();
		levelRenderer.skyBuffer.bind();
		levelRenderer.skyBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shader);
		VertexBuffer.unbind();
		RenderSystem.enableBlend();

		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		stack.pushPose();
		float rainAlpha = 1.0F - level.getRainLevel(partialTicks);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, rainAlpha);
		stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		stack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		FogRenderer.setupNoFog();
		starBuffer.bind();
		starBuffer.drawWithShader(stack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
		VertexBuffer.unbind();
		setupFog.run();

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		stack.popPose();
		RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
		double eyeOverVoid = camera.getEntity().getEyePosition(partialTicks).y() - level.getMinBuildHeight();
		if (eyeOverVoid < 0.0D) {
			stack.pushPose();
			stack.translate(0.0F, 12.0F, 0.0F);
			levelRenderer.darkBuffer.bind();
			levelRenderer.darkBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shader);
			VertexBuffer.unbind();
			stack.popPose();
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.depthMask(true);
		return true;
	}

	public static void createStars() {
		if (starBuffer != null) {
			starBuffer.close();
		}

		starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		starBuffer.bind();
		starBuffer.upload(drawStars(Tesselator.getInstance()));
		VertexBuffer.unbind();
	}

	private static MeshData drawStars(Tesselator tesselator) {
		RandomSource random = RandomSource.create(10842L);
		BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

		for (int i = 0; i < 3000; ++i) {
			float x = random.nextFloat() * 2.0F - 1.0F;
			float y = random.nextFloat() * 2.0F - 1.0F;
			float z = random.nextFloat() * 2.0F - 1.0F;
			float size = 0.15F + random.nextFloat() * 0.1F;
			float length = Mth.lengthSquared(x, y, z);
			if (length > 0.010000001F && length < 1.0F) {
				Vector3f vector = new Vector3f(x, y, z).normalize(100.0F);
				float twist = (float) (random.nextDouble() * Math.PI * 2.0D);
				Quaternionf rotation = new Quaternionf().rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), vector).rotateZ(twist);
				builder.addVertex(vector.add(new Vector3f(size, -size, 0.0F).rotate(rotation)));
				builder.addVertex(vector.add(new Vector3f(size, size, 0.0F).rotate(rotation)));
				builder.addVertex(vector.add(new Vector3f(-size, size, 0.0F).rotate(rotation)));
				builder.addVertex(vector.add(new Vector3f(-size, -size, 0.0F).rotate(rotation)));
			}
		}

		MeshData mesh = builder.buildOrThrow();
		if (mesh == null) {
			throw new IllegalStateException("Twilight Forest star buffer did not build");
		}
		return mesh;
	}
}
