package twilightforest.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

public class TwilightForestRenderInfo {
	public static final TwilightForestRenderInfo INSTANCE = new TwilightForestRenderInfo();

	public static final Identifier SKY_RENDERER = TFCommon.prefix("renderer");
	public static final Identifier WEATHER_RENDERER = TFCommon.prefix("weather");

	@Nullable
	private TFSkyRenderer skyRenderer;

	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Runnable setupFog) {
		if (this.skyRenderer == null) {
			this.skyRenderer = new TFSkyRenderer();
		}
		return skyRenderer.renderSky(levelRenderState, skyRenderState, setupFog);
	}

	public boolean renderSnowAndRain(LevelRenderState levelRenderState, Vec3 camPos) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return false;

		return TFWeatherRenderer.renderSnowAndRain(level, (int) (levelRenderState.gameTime & 131071L), Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false), camPos);
	}

	public boolean tickRain(ClientLevel level, long ticks, Camera camera) {
		return TFWeatherRenderer.tickRain(level, ticks, camera.blockPosition());
	}
}