package twilightforest.client;

import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFConfig;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.DimensionSpecialEffects.SkyType;

public class TwilightForestRenderInfo extends DimensionSpecialEffects {

    private DimensionRenderingRegistry.SkyRenderer skyRenderer;
    private DimensionRenderingRegistry.WeatherRenderer weatherRenderer;

    public TwilightForestRenderInfo(float cloudHeight, boolean placebo, SkyType fogType, boolean brightenLightMap, boolean entityLightingBottomsLit) {
        super(cloudHeight, placebo, fogType, brightenLightMap, entityLightingBottomsLit);
        DimensionRenderingRegistry.registerSkyRenderer(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())), getSkyRenderHandler());
//        DimensionRenderingRegistry.registerWeatherRenderer(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())), getWeatherRenderHandler());
    }

    // { red, green, blue, interpolation from white } DO NOT INLINE VARIABLE it avoids spamming array creation each render tick
    //private final float[] fourBeanDip = new float[4];

    @Nullable
    @Override
    public float[] getSunriseColor(float daycycle, float partialTicks) { // Fog color
        // TODO Vanilla copy, I just name a few stuff. Decide if we want to keep and cook our own thing, or we ditch it
        // Likely that we will need to ditch this. It only controls the colour of the fog based on celestial angle
        /*float f1 = MathHelper.cos(daycycle * ((float)Math.PI * 2F)) - 0.0F;
        if (f1 >= -0.4F && f1 <= 0.4F) {
            float f3 = f1 / 0.4F * 0.5F + 0.5F;
            float interpolateFromWhite = 1.0F - (1.0F - MathHelper.sin(f3 * (float)Math.PI)) * 0.99F;
            interpolateFromWhite = interpolateFromWhite * interpolateFromWhite;
            this.fourBeanDip[0] = f3 * 0.3F + 0.7F; // red
            this.fourBeanDip[1] = f3 * f3 * 0.7F + 0.2F; // green
            this.fourBeanDip[2] = f3 * f3 * 0.0F + 0.2F; // blue
            this.fourBeanDip[3] = interpolateFromWhite;
            return this.fourBeanDip;
        } else {
            return null;
        }*/
        return null;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) { // For modifying biome fog color with daycycle
        return /*biomeFogColor;*/biomeFogColor.multiply(daylight * 0.94F + 0.06F, (daylight * 0.94F + 0.06F), (daylight * 0.91F + 0.09F));
    }

    @Override
    public boolean isFoggyAt(int x, int y) { // true = nearFog
        return false;

        /*//TODO enable if the fog is fixed to smoothly transition. Otherwise the fog nearness just snaps and it's pretty janky tbh
        PlayerEntity playerEntity = Minecraft.getInstance().player;

        if (playerEntity == null || playerEntity.isCreative() || playerEntity.isSpectator() || playerEntity.getPositionVec().y > 42)
            return false; // If player is above the dark forest then no need to make it so spooky. The darkwood leaves cover everything as low as y42.

        Biome biome = Minecraft.getInstance().player.world.getBiome(new BlockPos(playerEntity.getPositionVec()));

        // FIXME Make the fog on these biomes much much darker, maybe pitch black even. Do we keep this harsher fog underground too?
        return biome == TFBiomes.darkForest.get() || biome == TFBiomes.darkForestCenter.get();*/
    }

    @Nullable
    public DimensionRenderingRegistry.SkyRenderer getSkyRenderHandler() {
        if (skyRenderer == null)
            skyRenderer = new TFSkyRenderer();
        return skyRenderer;
    }

    @Nullable
    public DimensionRenderingRegistry.WeatherRenderer getWeatherRenderHandler() {
        if (weatherRenderer == null)
            weatherRenderer = null;//new TFWeatherRenderer();
        return weatherRenderer;
    }
}
