package twilightforest.client.event;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFDimension;

public class FogHandler extends FogEnvironment {

	private static boolean SKY_CHUNK_LOADED = false;

	private static float SKY_FAR = 0.0F;
	private static float SKY_NEAR = 0.0F;

	private static boolean TERRAIN_CHUNK_LOADED = false;

	private static float TERRAIN_FAR = 0.0F;
	private static float TERRAIN_NEAR = 0.0F;

	@Override
	public boolean isApplicable(FogType fogType, Entity entity) {
		return fogType == FogType.NONE
			&& entity instanceof LocalPlayer
			&& entity.level() instanceof ClientLevel clientLevel
			&& TFDimension.DIMENSION_KEY.equals(clientLevel.dimension());
	}

	@Override
	public void setupFog(FogData data, Camera camera, ClientLevel level, float partialTick, DeltaTracker deltaTracker) {
		if (!(camera.entity() instanceof LocalPlayer player))
			return;

		boolean spooky = isSpooky(level, player);

		float far = spooky ? data.renderDistanceEnd * 0.5F : data.renderDistanceEnd;
		float near = spooky ? far * 0.75F : data.renderDistanceStart;

		if (TERRAIN_CHUNK_LOADED) {
			TERRAIN_FAR = Mth.lerp(0.003F, TERRAIN_FAR, far);
			TERRAIN_NEAR = Mth.lerp(0.003F * (TERRAIN_NEAR < near ? 0.5F : 2.0F), TERRAIN_NEAR, near);
		} else if (SKY_CHUNK_LOADED || level.isLoaded(player.blockPosition())) {
			TERRAIN_CHUNK_LOADED = true;
			TERRAIN_FAR = far;
			TERRAIN_NEAR = near;
		}

		data.renderDistanceStart = TERRAIN_NEAR;
		data.renderDistanceEnd = TERRAIN_FAR;

		float skyFar = spooky ? data.skyEnd * 0.5F : data.skyEnd;
		if (SKY_CHUNK_LOADED) {
			SKY_FAR = Mth.lerp(0.003F, SKY_FAR, skyFar);
			SKY_NEAR = Mth.lerp(0.003F * (SKY_NEAR < near ? 0.5F : 2.0F), SKY_NEAR, near);
		} else if (level.isLoaded(player.blockPosition())) {
			SKY_CHUNK_LOADED = true;
			SKY_FAR = skyFar;
			SKY_NEAR = near;
		}

		data.skyEnd = SKY_FAR;
	}

	private static boolean isSpooky(ClientLevel level, LocalPlayer player) {
		return level.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST);
	}
}
