package twilightforest.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import twilightforest.block.CloudBlock;
import twilightforest.config.TFConfig;

import java.util.ArrayList;
import java.util.List;

public final class CloudEvents {
	private static final List<PrecipitationRenderHelper> RENDER_HELPER = new ArrayList<>();

	private CloudEvents() {
	}

	public static void bootstrap() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> tickWeatherEffects());
	}

	private static void tickWeatherEffects() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.isPaused() || mc.level == null || TFConfig.getClientCloudBlockPrecipitationDistance() <= 0 || mc.level.getGameTime() % 10L != 0L) {
			return;
		}
		RENDER_HELPER.clear();
		BlockPos camera = BlockPos.containing(mc.gameRenderer.getMainCamera().getPosition());
		int renderDistance = Minecraft.useFancyGraphics() ? 10 : 5;
		int precipitationDistance = TFConfig.getClientCloudBlockPrecipitationDistance();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int z = camera.getZ() - renderDistance; z <= camera.getZ() + renderDistance; z++) {
			for (int x = camera.getX() - renderDistance; x <= camera.getX() + renderDistance; x++) {
				for (int y = camera.getY() - renderDistance; y < camera.getY() + precipitationDistance + renderDistance; y++) {
					pos.set(x, y, z);
					if (mc.level.getBlockState(pos).getBlock() instanceof CloudBlock cloudBlock) {
						org.apache.commons.lang3.tuple.Pair<Biome.Precipitation, Float> precipitation = cloudBlock.getCurrentPrecipitation(pos, mc.level, mc.level.getRainLevel(1.0F));
						if (precipitation.getLeft() != Biome.Precipitation.NONE) {
							RENDER_HELPER.add(new PrecipitationRenderHelper(pos.immutable(), precipitation.getLeft(), Mth.clamp(precipitation.getRight(), 0.0F, 1.0F), y - precipitationDistance));
						}
					}
				}
			}
		}
	}

	public static List<PrecipitationRenderHelper> renderHelpers() {
		return List.copyOf(RENDER_HELPER);
	}

	public record PrecipitationRenderHelper(BlockPos cloudPos, Biome.Precipitation precipitation, float precipitationLevel, int rainOnY) {
	}
}
