package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.renderer.map.MapDecorationManager;

import java.util.Objects;

@Mixin(MapRenderer.class)
public class MapRendererMixin {

	@Shadow
	@Final
	private TextureAtlas decorationSprites;

	/**
	 * @author Autumn
	 * @reason I don't really see a way around this one unfortunately as Mixin cannot add a 'continue'.
	 */
	@Overwrite
	public void render(final MapRenderState mapRenderState, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final boolean showOnlyFrame, final int lightCoords) {
		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(mapRenderState.texture), (pose, buffer) -> {
			buffer.addVertex(pose, 0.0F, 128.0F, -0.01F).setColor(-1).setUv(0.0F, 1.0F).setLight(lightCoords);
			buffer.addVertex(pose, 128.0F, 128.0F, -0.01F).setColor(-1).setUv(1.0F, 1.0F).setLight(lightCoords);
			buffer.addVertex(pose, 128.0F, 0.0F, -0.01F).setColor(-1).setUv(1.0F, 0.0F).setLight(lightCoords);
			buffer.addVertex(pose, 0.0F, 0.0F, -0.01F).setColor(-1).setUv(0.0F, 0.0F).setLight(lightCoords);
		});
		int count = 0;

		for(MapRenderState.MapDecorationRenderState decoration : mapRenderState.decorations) {
			if (!showOnlyFrame || decoration.renderOnFrame) {
				if (MapDecorationManager.render(decoration, poseStack, submitNodeCollector, mapRenderState, this.decorationSprites, showOnlyFrame, lightCoords, count)) {
					count++;
					continue;
				}
				poseStack.pushPose();
				poseStack.translate((float)decoration.x / 2.0F + 64.0F, (float)decoration.y / 2.0F + 64.0F, -0.02F);
				poseStack.mulPose(Axis.ZP.rotationDegrees((float)(decoration.rot * 360) / 16.0F));
				poseStack.scale(4.0F, 4.0F, 3.0F);
				poseStack.translate(-0.125F, 0.125F, 0.0F);
				TextureAtlasSprite atlasSprite = decoration.atlasSprite;
				if (atlasSprite != null) {
					float z = (float)count * -0.001F;
					submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(atlasSprite.atlasLocation()), (pose, buffer) -> {
						buffer.addVertex(pose, -1.0F, 1.0F, z).setColor(-1).setUv(atlasSprite.getU0(), atlasSprite.getV0()).setLight(lightCoords);
						buffer.addVertex(pose, 1.0F, 1.0F, z).setColor(-1).setUv(atlasSprite.getU1(), atlasSprite.getV0()).setLight(lightCoords);
						buffer.addVertex(pose, 1.0F, -1.0F, z).setColor(-1).setUv(atlasSprite.getU1(), atlasSprite.getV1()).setLight(lightCoords);
						buffer.addVertex(pose, -1.0F, -1.0F, z).setColor(-1).setUv(atlasSprite.getU0(), atlasSprite.getV1()).setLight(lightCoords);
					});
					poseStack.popPose();
				}

				if (decoration.name != null) {
					Font font = Minecraft.getInstance().font;
					float width = (float)font.width(decoration.name);
					float var10000 = 25.0F / width;
					Objects.requireNonNull(font);
					float scale = Mth.clamp(var10000, 0.0F, 6.0F / 9.0F);
					poseStack.pushPose();
					poseStack.translate((float)decoration.x / 2.0F + 64.0F - width * scale / 2.0F, (float)decoration.y / 2.0F + 64.0F + 4.0F, -0.025F);
					poseStack.scale(scale, scale, -1.0F);
					poseStack.translate(0.0F, 0.0F, 0.1F);
					submitNodeCollector.order(1).submitText(poseStack, 0.0F, 0.0F, decoration.name.getVisualOrderText(), false, Font.DisplayMode.NORMAL, lightCoords, -1, Integer.MIN_VALUE, 0);
					poseStack.popPose();
				}

				++count;
			}
		}

	}

	@Inject(
		method = "extractDecorationRenderState(Lnet/minecraft/world/level/saveddata/maps/MapDecoration;)Lnet/minecraft/client/renderer/state/MapRenderState$MapDecorationRenderState;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/texture/TextureAtlas;getSprite(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"
		)
	)
	private void twilightforest$addStateData(
		MapDecoration decoration,
		CallbackInfoReturnable<MapRenderState.MapDecorationRenderState> cir,
		@Local(name = "state") MapRenderState.MapDecorationRenderState state
	) {
		state.setData(MapDecorationManager.DECORATION_TYPE, decoration.type());
	}
}