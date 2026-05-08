package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;
import twilightforest.item.MagicMapItem;
import twilightforest.item.mapdata.TFMagicMapData;

import java.util.Optional;

public class ConqueredMapIconRenderer {
	private static final MapDecoration RED_X_DECORATION = new MapDecoration(MapDecorationTypes.RED_X, (byte) 0, (byte) 0, (byte) 0, Optional.empty());

	public void render(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, MapItemSavedData mapData, MapDecorationTextureManager decorationTextures, int light) {
		if (!(mapData instanceof TFMagicMapData map) || !map.conqueredStructures.contains(MagicMapItem.makeName(decoration.type(), decoration.x(), decoration.y()))) {
			return;
		}

		stack.pushPose();
		stack.translate(decoration.x() / 2.0F + 64.0F, decoration.y() / 2.0F + 64.0F, -0.02F);
		stack.mulPose(Axis.ZP.rotationDegrees((decoration.rot() & 15) * 360.0F / 16.0F));
		stack.scale(2.0F, 2.0F, 3.0F);
		stack.translate(-1.0F, -1.0F, -0.005F);

		Matrix4f matrix = stack.last().pose();
		TextureAtlasSprite sprite = decorationTextures.get(RED_X_DECORATION);
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.text(sprite.atlasLocation()));
		consumer.addVertex(matrix, -1.0F, 1.0F, -0.095F).setColor(-1).setUv(sprite.getU0(), sprite.getV1()).setLight(light);
		consumer.addVertex(matrix, 1.0F, 1.0F, -0.095F).setColor(-1).setUv(sprite.getU1(), sprite.getV1()).setLight(light);
		consumer.addVertex(matrix, 1.0F, -1.0F, -0.095F).setColor(-1).setUv(sprite.getU1(), sprite.getV0()).setLight(light);
		consumer.addVertex(matrix, -1.0F, -1.0F, -0.095F).setColor(-1).setUv(sprite.getU0(), sprite.getV0()).setLight(light);
		stack.popPose();
	}
}
