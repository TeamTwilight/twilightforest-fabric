package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import twilightforest.world.registration.biomes.BiomeKeys;

public class FogHandler {

	private static final float[] spoopColors = new float[3];
	private static float spoopColor = 0F;
	private static float spoopFog = 1F;

	public static void fogColors(FogEvents.ColorData event, float partialTicks) {
		boolean flag = isSpooky();
		if (flag || spoopColor > 0F) {
			final float[] realColors = {event.getRed(), event.getGreen(), event.getBlue()};
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
			event.setRed(spoopColors[0]);
			event.setGreen(spoopColors[1]);
			event.setBlue(spoopColors[2]);
		}
	}

	public static void fog(FogRenderer.FogMode type, Camera info, float partial, float distance) {
		boolean flag = isSpooky();
		if (flag || spoopFog < 1F) {
			float f = 48F;
			f = f >= distance ? distance : Mth.clampedLerp(f, distance, spoopFog);
			float shift = (float) (0.001F * partial);
			if (flag)
				spoopFog -= shift;
			else
				spoopFog += shift;
			spoopFog = Mth.clamp(spoopFog, 0F, 1F);

			//FIXME: These two are commented out as they do not exist in the main game. While this might mean they aren't needed, look at this if there is a problem
//			RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);

			if (type == FogRenderer.FogMode.FOG_SKY) {
				RenderSystem.setShaderFogStart(0.0F);
				RenderSystem.setShaderFogEnd(f);
			} else {
				RenderSystem.setShaderFogStart(f * 0.75F);
				RenderSystem.setShaderFogEnd(f);
			}

//			RenderSystem.setupNvFogDistance();
		}
	}

	private static boolean isSpooky() {
		return Minecraft.getInstance().level != null && Minecraft.getInstance().player != null &&
				Minecraft.getInstance().level.getBiome(Minecraft.getInstance().player.blockPosition()).is(BiomeKeys.SPOOKY_FOREST);
	}

	public static void init() {
		FogEvents.SET_COLOR.register(FogHandler::fogColors);
		FogEvents.RENDER_FOG.register(FogHandler::fog);
	}
}
