package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import twilightforest.block.entity.ReactorDebrisBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class ReactorDebrisRenderer implements BlockEntityRenderer<ReactorDebrisBlockEntity> {
	private static final Map<ResourceLocation, TextureAtlasSprite> SPRITE_CACHE = new HashMap<>();

	public ReactorDebrisRenderer(BlockEntityRendererProvider.Context context) {
	}

	private enum Axis {
		X, Y, Z
	}

	private record QuadRenderInfo(VertexConsumer builder, Matrix4f matrix, int light, int overlay) {
		private void vertex(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
			this.builder.addVertex(this.matrix, x, y, z)
				.setUv(u, v)
				.setLight(this.light)
				.setOverlay(this.overlay)
				.setNormal(nx, ny, nz)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void render(ReactorDebrisBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
		if (entity.getLevel() == null) {
			return;
		}

		poseStack.pushPose();
		VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));
		Matrix4f matrix = poseStack.last().pose();
		QuadRenderInfo info = new QuadRenderInfo(builder, matrix, light, overlay);

		this.renderBlock(info, entity);
		poseStack.popPose();
	}

	private void renderBlock(QuadRenderInfo info, ReactorDebrisBlockEntity entity) {
		float minX = entity.minPos.x;
		float minY = entity.minPos.y;
		float minZ = entity.minPos.z;
		float maxX = entity.maxPos.x;
		float maxY = entity.maxPos.y;
		float maxZ = entity.maxPos.z;

		this.renderSide(info, getSprite(entity.textures[0]), Axis.X, minX, minY, minZ, maxY, maxZ, -1.0F);
		this.renderSide(info, getSprite(entity.textures[1]), Axis.X, maxX, minY, maxZ, maxY, minZ, 1.0F);
		this.renderSide(info, getSprite(entity.textures[2]), Axis.Y, minY, minX, maxZ, maxX, minZ, -1.0F);
		this.renderSide(info, getSprite(entity.textures[3]), Axis.Y, maxY, minX, minZ, maxX, maxZ, 1.0F);
		this.renderSide(info, getSprite(entity.textures[4]), Axis.Z, minZ, minX, minY, maxX, maxY, -1.0F);
		this.renderSide(info, getSprite(entity.textures[5]), Axis.Z, maxZ, maxX, minY, minX, maxY, 1.0F);

		this.renderSide(info, getSprite(entity.textures[0]), Axis.X, minX, minY, maxZ, maxY, minZ, 1.0F);
		this.renderSide(info, getSprite(entity.textures[1]), Axis.X, maxX, minY, minZ, maxY, maxZ, -1.0F);
		this.renderSide(info, getSprite(entity.textures[2]), Axis.Y, minY, minX, minZ, maxX, maxZ, 1.0F);
		this.renderSide(info, getSprite(entity.textures[3]), Axis.Y, maxY, minX, maxZ, maxX, minZ, -1.0F);
		this.renderSide(info, getSprite(entity.textures[4]), Axis.Z, minZ, maxX, minY, minX, maxY, 1.0F);
		this.renderSide(info, getSprite(entity.textures[5]), Axis.Z, maxZ, minX, minY, maxX, maxY, -1.0F);
	}

	private void renderSide(QuadRenderInfo info, TextureAtlasSprite sprite, Axis axis, float c1, float min1, float min2, float max1, float max2, float n) {
		float u0 = Mth.lerp(min1, sprite.getU0(), sprite.getU1());
		float v0 = Mth.lerp(min2, sprite.getV0(), sprite.getV1());
		float u1 = Mth.lerp(max1, sprite.getU0(), sprite.getU1());
		float v1 = Mth.lerp(max2, sprite.getV0(), sprite.getV1());

		switch (axis) {
			case X -> {
				info.vertex(c1, min1, min2, u0, v0, n, 0.0F, 0.0F);
				info.vertex(c1, min1, max2, u0, v1, n, 0.0F, 0.0F);
				info.vertex(c1, max1, max2, u1, v1, n, 0.0F, 0.0F);
				info.vertex(c1, max1, min2, u1, v0, n, 0.0F, 0.0F);
			}
			case Y -> {
				info.vertex(min1, c1, min2, u0, v0, 0.0F, n, 0.0F);
				info.vertex(min1, c1, max2, u0, v1, 0.0F, n, 0.0F);
				info.vertex(max1, c1, max2, u1, v1, 0.0F, n, 0.0F);
				info.vertex(max1, c1, min2, u1, v0, 0.0F, n, 0.0F);
			}
			case Z -> {
				info.vertex(min1, min2, c1, u0, v0, 0.0F, 0.0F, n);
				info.vertex(min1, max2, c1, u0, v1, 0.0F, 0.0F, n);
				info.vertex(max1, max2, c1, u1, v1, 0.0F, 0.0F, n);
				info.vertex(max1, min2, c1, u1, v0, 0.0F, 0.0F, n);
			}
		}
	}

	public static TextureAtlasSprite getSprite(ResourceLocation location) {
		if (location == null) {
			return getSprite(ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
		}
		return SPRITE_CACHE.computeIfAbsent(location, loc -> Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(loc));
	}
}
