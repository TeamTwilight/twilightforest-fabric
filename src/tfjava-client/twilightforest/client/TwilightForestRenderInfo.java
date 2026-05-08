package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBiomes;

import java.util.Optional;

public class TwilightForestRenderInfo extends DimensionSpecialEffects {
	public TwilightForestRenderInfo(float cloudHeight, boolean hasGround, SkyType skyType, boolean forceBrightLightmap, boolean constantAmbientLight) {
		super(cloudHeight, hasGround, skyType, forceBrightLightmap, constantAmbientLight);
	}

	@Nullable
	@Override
	public float[] getSunriseColor(float daycycle, float partialTicks) {
		return null;
	}

	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) {
		return biomeFogColor.multiply(daylight * 0.94F + 0.06F, daylight * 0.94F + 0.06F, daylight * 0.91F + 0.09F);
	}

	@Override
	public boolean isFoggyAt(int x, int y) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			Optional<ResourceKey<Biome>> biome = player.level().getBiome(player.blockPosition()).unwrapKey();
			if (biome.isPresent()) {
				boolean spooky = biome.get() == TFBiomes.SPOOKY_FOREST;
				if (player.position().y > 20 && !spooky) {
					return false;
				}

				return spooky || biome.get() == TFBiomes.DARK_FOREST || biome.get() == TFBiomes.DARK_FOREST_CENTER;
			}
		}

		return false;
	}

}
