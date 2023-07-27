package twilightforest.client;

import com.mojang.blaze3d.shaders.FogShape;
import io.github.fabricators_of_create.porting_lib.event.client.ClientWorldEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import twilightforest.init.TFBiomes;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
public class FogHandler {

	private static final float[] spoopColors = new float[3];
	private static float spoopColor = 0F;

	public static void fogColors(FogEvents.ColorData data, float partialTicks) {
		boolean flag = isSpooky(Minecraft.getInstance().level, Minecraft.getInstance().player);
		if (flag || spoopColor > 0F) {
			final float[] realColors = {data.getRed(), data.getGreen(), data.getBlue()};
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
			data.setRed(spoopColors[0]);
			data.setGreen(spoopColors[1]);
			data.setBlue(spoopColors[2]);
		}
	}

	private static boolean SKY_CHUNK_LOADED = false;

	private static float SKY_FAR = 0.0F;
	private static float SKY_NEAR = 0.0F;

	private static boolean TERRAIN_CHUNK_LOADED = false;

	private static float TERRAIN_FAR = 0.0F;
	private static float TERRAIN_NEAR = 0.0F;


	public static boolean renderFog(FogRenderer.FogMode mode, FogType type, Camera camera, float partialTick, float renderDistance, float nearDistance, float farDistance, FogShape shape, FogEvents.FogData fogData) {
		if (type.equals(FogType.NONE) && Minecraft.getInstance().cameraEntity instanceof LocalPlayer player && player.level() instanceof ClientLevel clientLevel && clientLevel.effects() instanceof TwilightForestRenderInfo) {
			if (mode.equals(FogRenderer.FogMode.FOG_SKY)) {
				if (SKY_CHUNK_LOADED) {
					boolean spooky = isSpooky(clientLevel, player);

					float far = spooky ? fogData.getFarPlaneDistance() * 0.5F : fogData.getFarPlaneDistance();
					float near = spooky ? 0.0F : fogData.getNearPlaneDistance();

					SKY_FAR = Mth.lerp(0.003F, SKY_FAR, far);
					SKY_NEAR = Mth.lerp(0.003F * (SKY_NEAR < near ? 0.5F : 2.0F), SKY_NEAR, near);

					fogData.setFarPlaneDistance(SKY_FAR);
					fogData.setNearPlaneDistance(SKY_NEAR);
					return true;
				} else if (clientLevel.isLoaded(player.blockPosition())) { //We do a first-time set up after the chunk the player is in is loaded
					SKY_CHUNK_LOADED = true;
					SKY_FAR = isSpooky(clientLevel, player) ? fogData.getFarPlaneDistance() * 0.5F : fogData.getFarPlaneDistance();
					SKY_NEAR = isSpooky(clientLevel, player) ? 0.0F : fogData.getNearPlaneDistance();
				}
			} else {
				if (TERRAIN_CHUNK_LOADED) {
					boolean spooky = isSpooky(clientLevel, player);

					float far = spooky ? fogData.getFarPlaneDistance() * 0.5F : fogData.getFarPlaneDistance();
					float near = spooky ? far * 0.75F : fogData.getNearPlaneDistance();

					TERRAIN_FAR = Mth.lerp(0.003F, TERRAIN_FAR, far);
					TERRAIN_NEAR = Mth.lerp(0.003F * (TERRAIN_NEAR < near ? 0.5F : 2.0F), TERRAIN_NEAR, near);

					fogData.setFarPlaneDistance(TERRAIN_FAR);
					fogData.setNearPlaneDistance(TERRAIN_NEAR);
					return true;
				} else if (SKY_CHUNK_LOADED || clientLevel.isLoaded(player.blockPosition())) { //SKY is always called first in vanilla, so we only need to check if the SKY flag is true, but just in case
					TERRAIN_CHUNK_LOADED = true;
					TERRAIN_FAR = isSpooky(clientLevel, player) ? fogData.getFarPlaneDistance() * 0.5F : fogData.getFarPlaneDistance();
					TERRAIN_NEAR = isSpooky(clientLevel, player) ? TERRAIN_FAR * 0.75F : fogData.getNearPlaneDistance();
				}
			}
		}
		return false;
	}

	public static void onUnload(Minecraft mc, ClientLevel level) { //As supernatural as the fog is, it shouldn't follow the player between worlds
		SKY_CHUNK_LOADED = false;
		TERRAIN_CHUNK_LOADED = false;
	}

	private static boolean isSpooky(@Nullable ClientLevel level, @Nullable LocalPlayer player) {
		return level != null && player != null && level.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST);
	}

	public static void init() {
		FogEvents.SET_COLOR.register(FogHandler::fogColors);
		FogEvents.RENDER_FOG.register(FogHandler::renderFog);
		ClientWorldEvents.UNLOAD.register(FogHandler::onUnload);
	}
}
