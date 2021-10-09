package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import twilightforest.world.registration.biomes.BiomeKeys;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;

//TODO: PORT
public class FogHandler {

	private static final float[] spoopColors = new float[3];
	private static float spoopColor = 0F;
	private static float spoopFog = 1F;

	public static Color fogColors(Camera info, float partialTicks, float red, float green, float blue) {
		//I don't know if I can modify params so lets do this for now
		float _red = red;
		float _green = green;
		float _blue = blue;
		boolean flag = isSpooky();
		if (flag || spoopColor > 0F) {
			final float[] realColors = {_red, _green, _blue};
			final float[] lerpColors = {130F / 255F, 115F / 255F, 145F / 255F};
			for (int i = 0; i < 3; i++) {
				final float real = realColors[i];
				final float spoop = lerpColors[i];
				final boolean inverse = real > spoop;
				spoopColors[i] = real == spoop ? spoop : Mth.clampedLerp(inverse ? spoop : real, inverse ? real : spoop, spoopColor);
			}
			float shift = (float) (0.01F * partialTicks);
			if (flag)
				spoopColor += shift;
			else
				spoopColor -= shift;
			spoopColor = Mth.clamp(spoopColor, 0F, 1F);
			_red = spoopColors[0];
			_green = spoopColors[1];
			_blue = spoopColors[2];
		}
		return new Color(_red, _green, _blue);
	}

//	public static void fog(EntityViewRenderEvent.RenderFogEvent event) {
//		boolean flag = isSpooky();
//		if (flag || spoopFog < 1F) {
//			float f = 48F;
//			f = f >= event.getFarPlaneDistance() ? event.getFarPlaneDistance() : Mth.clampedLerp(f, event.getFarPlaneDistance(), spoopFog);
//			float shift = (float) (0.001F * event.getRenderPartialTicks());
//			if (flag)
//				spoopFog -= shift;
//			else
//				spoopFog += shift;
//			spoopFog = Mth.clamp(spoopFog, 0F, 1F);
//
//			//FIXME: These two are commented out as they do not exist in the main game. While this might mean they aren't needed, look at this if there is a problem
////			RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
//
//			if (event.getType() == FogRenderer.FogMode.FOG_SKY) {
//				RenderSystem.setShaderFogStart(0.0F);
//				RenderSystem.setShaderFogEnd(f);
//			} else {
//				RenderSystem.setShaderFogStart(f * 0.75F);
//				RenderSystem.setShaderFogEnd(f);
//			}
//
////			RenderSystem.setupNvFogDistance();
//		}
//	}

	private static boolean isSpooky() {
		return Minecraft.getInstance().level != null && Minecraft.getInstance().player != null &&
				Objects.equals(Minecraft.getInstance().level.getBiomeName(Minecraft.getInstance().player.blockPosition()), Optional.of(BiomeKeys.SPOOKY_FOREST));
	}


}
