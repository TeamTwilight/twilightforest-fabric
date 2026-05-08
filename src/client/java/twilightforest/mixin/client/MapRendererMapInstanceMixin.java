package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.map.TFMapDecorationRenderers;
import twilightforest.item.mapdata.TFMagicMapData;

@Mixin(targets = "net.minecraft.client.gui.MapRenderer$MapInstance")
public abstract class MapRendererMapInstanceMixin {
	@Shadow
	private MapItemSavedData data;
	@Shadow
	@Final
	private RenderType renderType;
	@Shadow
	private boolean requiresUpload;
	@Shadow
	@Final
	private MapRenderer field_2047;

	@Shadow
	private void updateTexture() {
	}

	@Inject(method = "draw", at = @At("HEAD"), cancellable = true)
	private void twilightforest$drawMagicMap(PoseStack stack, MultiBufferSource bufferSource, boolean renderOnFrame, int light, CallbackInfo ci) {
		if (!(this.data instanceof TFMagicMapData)) {
			return;
		}

		if (this.requiresUpload) {
			this.updateTexture();
			this.requiresUpload = false;
		}

		MapDecorationTextureManager decorationTextures = ((MapRendererAccessor) this.field_2047).twilightforest$getDecorationTextures();
		Matrix4f matrix = stack.last().pose();
		VertexConsumer mapConsumer = bufferSource.getBuffer(this.renderType);
		mapConsumer.addVertex(matrix, 0.0F, 128.0F, -0.01F).setColor(-1).setUv(0.0F, 1.0F).setLight(light);
		mapConsumer.addVertex(matrix, 128.0F, 128.0F, -0.01F).setColor(-1).setUv(1.0F, 1.0F).setLight(light);
		mapConsumer.addVertex(matrix, 128.0F, 0.0F, -0.01F).setColor(-1).setUv(1.0F, 0.0F).setLight(light);
		mapConsumer.addVertex(matrix, 0.0F, 0.0F, -0.01F).setColor(-1).setUv(0.0F, 0.0F).setLight(light);

		int decorationIndex = 0;
		for (MapDecoration decoration : this.data.getDecorations()) {
			if (!renderOnFrame || decoration.renderOnFrame()) {
				boolean skipVanillaIcon = TFMapDecorationRenderers.renderMagicPlayerIcon(decoration, stack, bufferSource, this.data, decorationTextures, light);
				if (!skipVanillaIcon) {
					this.twilightforest$renderVanillaDecoration(decoration, stack, bufferSource, decorationTextures, light, decorationIndex);
				}
				TFMapDecorationRenderers.renderConqueredOverlay(decoration, stack, bufferSource, this.data, decorationTextures, light);
				this.twilightforest$renderDecorationName(decoration, stack, bufferSource, light);
			}
			++decorationIndex;
		}

		ci.cancel();
	}

	private void twilightforest$renderVanillaDecoration(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, MapDecorationTextureManager decorationTextures, int light, int decorationIndex) {
		stack.pushPose();
		stack.translate(decoration.x() / 2.0F + 64.0F, decoration.y() / 2.0F + 64.0F, -0.02F);
		stack.mulPose(Axis.ZP.rotationDegrees((decoration.rot() & 15) * 360.0F / 16.0F));
		stack.scale(4.0F, 4.0F, 3.0F);
		stack.translate(-0.125F, 0.125F, 0.0F);
		Matrix4f matrix = stack.last().pose();
		float depth = decorationIndex * -0.001F;
		TextureAtlasSprite sprite = decorationTextures.get(decoration);
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.text(sprite.atlasLocation()));
		consumer.addVertex(matrix, -1.0F, 1.0F, depth).setColor(-1).setUv(sprite.getU0(), sprite.getV1()).setLight(light);
		consumer.addVertex(matrix, 1.0F, 1.0F, depth).setColor(-1).setUv(sprite.getU1(), sprite.getV1()).setLight(light);
		consumer.addVertex(matrix, 1.0F, -1.0F, depth).setColor(-1).setUv(sprite.getU1(), sprite.getV0()).setLight(light);
		consumer.addVertex(matrix, -1.0F, -1.0F, depth).setColor(-1).setUv(sprite.getU0(), sprite.getV0()).setLight(light);
		stack.popPose();
	}

	private void twilightforest$renderDecorationName(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, int light) {
		if (decoration.name().isEmpty()) {
			return;
		}

		Font font = Minecraft.getInstance().font;
		Component name = decoration.name().get();
		float width = font.width(name);
		float scale = Mth.clamp(25.0F / width, 0.0F, 6.0F / 9.0F);
		stack.pushPose();
		stack.translate(decoration.x() / 2.0F + 64.0F - width * scale / 2.0F, decoration.y() / 2.0F + 64.0F + 4.0F, -0.025F);
		stack.scale(scale, scale, 1.0F);
		stack.translate(0.0F, 0.0F, -0.1F);
		font.drawInBatch(name, 0.0F, 0.0F, -1, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, Integer.MIN_VALUE, light);
		stack.popPose();
	}
}
