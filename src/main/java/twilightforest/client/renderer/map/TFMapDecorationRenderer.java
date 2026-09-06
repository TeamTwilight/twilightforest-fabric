package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;

public interface TFMapDecorationRenderer {
	boolean render(
		MapRenderState.MapDecorationRenderState decorationRenderState,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		MapRenderState mapRenderState,
		TextureAtlas decorationSprites,
		boolean inItemFrame,
		int packedLight,
		int index
	);
}