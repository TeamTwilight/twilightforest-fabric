package twilightforest.client;

import io.github.fabricators_of_create.porting_lib.event.client.EntityAddedLayerCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.client.model.TFLayerDefinitions;
import twilightforest.client.particle.TFParticleType;
import twilightforest.client.renderer.entity.IceLayer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.compat.CuriosCompat;
import twilightforest.inventory.TFContainers;
import twilightforest.item.TFItems;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class TFClientSetup implements ClientModInitializer {

	public static boolean optifinePresent = false;

	@Override
	public void onInitializeClient() {
		TFShaders.init();

		TFLayerDefinitions.registerLayers();
		ColorHandler.registerBlockColors();
		ColorHandler.registerItemColors();
		TFClientEvents.init();
		ScreenEvents.AFTER_INIT.register(FabricEvents::showOptifineWarning);
		FogHandler.init();
		TFParticleType.registerFactories();
		ClientTickEvents.END_CLIENT_TICK.register(LockedBiomeListener::clientTick);
		EntityAddedLayerCallback.EVENT.register(TFClientSetup::attachRenderLayers);
	}

	public static class FabricEvents {

		private static boolean optifineWarningShown = false;

		public static void showOptifineWarning(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
			if (optifinePresent && !optifineWarningShown && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen.get() && screen instanceof TitleScreen) {
				optifineWarningShown = true;
				Minecraft.getInstance().setScreen(new OptifineWarningScreen(screen));
			}
		}

	}

	private static void registerWoodType(WoodType woodType) {
		Sheets.SIGN_MATERIALS.put(woodType, Sheets.createSignMaterial(woodType));
	}

    public static void clientSetup() {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}
		TFItems.addItemModelProperties();

        RenderLayerRegistration.init();
        TFBlockEntities.registerTileEntityRenders();
        TFContainers.renderScreens();

        TwilightForestRenderInfo renderInfo = new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false);
        DimensionSpecialEffects.EFFECTS.put(TwilightForestMod.prefix("renderer"), renderInfo);

		for(BannerPattern pattern : BannerPattern.values()) {
			if(pattern.getFilename().startsWith(TwilightForestMod.ID)) {
				Sheets.BANNER_MATERIALS.put(pattern, Sheets.createBannerMaterial(pattern));
				Sheets.SHIELD_MATERIALS.put(pattern, Sheets.createShieldMaterial(pattern));
			}
		}

//        evt.enqueueWork(() -> {
            registerWoodType(TFBlocks.TWILIGHT_OAK);
            registerWoodType(TFBlocks.CANOPY);
            registerWoodType(TFBlocks.MANGROVE);
            registerWoodType(TFBlocks.DARKWOOD);
            registerWoodType(TFBlocks.TIMEWOOD);
            registerWoodType(TFBlocks.TRANSFORMATION);
            registerWoodType(TFBlocks.MINING);
            registerWoodType(TFBlocks.SORTING);

			if(FabricLoader.getInstance().isModLoaded("curios")) {
				CuriosCompat.registerCurioRenderers();
			}
//        });

    }

	@SuppressWarnings("unchecked")
	public static void attachRenderLayers(final Map<EntityType<?>, EntityRenderer<?>> renderers, final Map<String, EntityRenderer<? extends Player>> skins) {
		skins.forEach((id, renderer) -> {
			LivingEntityRenderer<Player, EntityModel<Player>> skin = (LivingEntityRenderer<Player, EntityModel<Player>>) skins.get(renderer);
			attachRenderLayers(Objects.requireNonNull(skin));
		});
		renderers.values().stream().
				filter(LivingEntityRenderer.class::isInstance).map(LivingEntityRenderer.class::cast).forEach(TFClientSetup::attachRenderLayers);
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}
}
