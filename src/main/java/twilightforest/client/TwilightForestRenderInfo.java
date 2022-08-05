package twilightforest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

import javax.annotation.Nullable;

public class TwilightForestRenderInfo extends DimensionSpecialEffects {

    public TwilightForestRenderInfo(float cloudHeight, boolean placebo, SkyType fogType, boolean brightenLightMap, boolean entityLightingBottomsLit) {
        super(cloudHeight, placebo, fogType, brightenLightMap, entityLightingBottomsLit);
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float daycycle, float partialTicks) { // Fog color
        return null;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) { // For modifying biome fog color with daycycle
        return biomeFogColor.multiply(daylight * 0.94F + 0.06F, (daylight * 0.94F + 0.06F), (daylight * 0.91F + 0.09F));
    }

    @Override
    public boolean isFoggyAt(int x, int y) { // true = nearFog
        //TODO enable if the fog is fixed to smoothly transition. Otherwise the fog nearness just snaps and it's pretty janky tbh
        /*Player player = Minecraft.getInstance().player;

        if (player == null || player.isCreative() || player.isSpectator() || player.position().y > 42)
            return false; // If player is above the dark forest then no need to make it so spooky. The darkwood leaves cover everything as low as y42.

        ResourceKey<Biome> biome = Minecraft.getInstance().player.level.getBiome(new BlockPos(player.position())).unwrapKey().get();

        //Make the fog on these biomes much much darker, maybe pitch black even. Do we keep this harsher fog underground too?
        return biome == BiomeKeys.DARK_FOREST || biome == BiomeKeys.DARK_FOREST_CENTER;*/
        return false;
    }


    public void renderSky(WorldRenderContext context) {
        ClientLevel level = context.world();
        float partialTick = context.tickDelta();
        PoseStack poseStack = context.matrixStack();
        Camera camera = context.camera();
        TFSkyRenderer.renderSky(level, partialTick, poseStack, camera);
    }

    public void renderSnowAndRain(WorldRenderContext context) {
        ClientLevel level = context.world();
        float partialTick = context.tickDelta();
        LightTexture lightTexture = context.lightmapTextureManager();
        Camera camera = context.camera();
        Vec3 pos = camera.getPosition();
        TFWeatherRenderer.renderSnowAndRain(level, partialTick, lightTexture, pos.x, pos.y, pos.z);
    }
}
