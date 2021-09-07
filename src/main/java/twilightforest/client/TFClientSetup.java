package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFLayerDefinitions;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.particle.TFParticleType;
import twilightforest.client.renderer.entity.IceLayer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.entity.TFEntities;
import twilightforest.inventory.TFContainers;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TFTileEntities;
import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.LivingEntity;

public class TFClientSetup implements ClientModInitializer {

	public static boolean optifinePresent = false;

	@Override
	public void onInitializeClient() {
        clientSetup();
	}

	public static class ForgeEvents {

		private static boolean optifineWarningShown = false;

		public static void showOptifineWarning(Screen screen) {
			if (optifinePresent && !optifineWarningShown && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen && screen instanceof TitleScreen) {
				optifineWarningShown = true;
				Minecraft.getInstance().setScreen(new OptifineWarningScreen(screen));
			}
		}

	}

	//TODO: Clean this shit up
    public static void clientSetup() {
        TFLayerDefinitions.registerLayers();
        TFModelLayers.init();
        TFEntities.registerEntityRenderer();
        TFParticleType.registerFactories();
        //twilightforest.client.TFClientSetup.addLegacyPack();
        System.out.println(FabricLoader.getInstance().isModLoaded("optifabric")+ ": Optifine loaded?");
        if(FabricLoader.getInstance().isModLoaded("optifabric"))
            optifinePresent = true;
//		try {
//			Class.forName("net.optifine.Config");
//			optifinePresent = true;
//		} catch (ClassNotFoundException e) {
//			optifinePresent = false;
//		}
		TFItems.addItemModelProperties();
        RenderLayerRegistration.init();
        //MinecraftForge.EVENT_BUS.register(new LoadingScreenListener());
        TFTileEntities.registerTileEntityRenders();
        TFContainers.renderScreens();

        TwilightForestRenderInfo renderInfo = new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false);
        DimensionSpecialEffects.EFFECTS.put(TwilightForestMod.prefix("renderer"), renderInfo);

        Minecraft.getInstance().execute(() -> {
        	Sheets.SIGN_MATERIALS.put(TFBlocks.TWILIGHT_OAK, Sheets.createSignMaterial(TFBlocks.TWILIGHT_OAK));
            Sheets.SIGN_MATERIALS.put(TFBlocks.CANOPY, Sheets.createSignMaterial(TFBlocks.CANOPY));
            Sheets.SIGN_MATERIALS.put(TFBlocks.MANGROVE, Sheets.createSignMaterial(TFBlocks.MANGROVE));
            Sheets.SIGN_MATERIALS.put(TFBlocks.DARKWOOD, Sheets.createSignMaterial(TFBlocks.DARKWOOD));
            Sheets.SIGN_MATERIALS.put(TFBlocks.TIMEWOOD, Sheets.createSignMaterial(TFBlocks.TIMEWOOD));
            Sheets.SIGN_MATERIALS.put(TFBlocks.TRANSFORMATION, Sheets.createSignMaterial(TFBlocks.TRANSFORMATION));
            Sheets.SIGN_MATERIALS.put(TFBlocks.MINING, Sheets.createSignMaterial(TFBlocks.MINING));
            Sheets.SIGN_MATERIALS.put(TFBlocks.SORTING, Sheets.createSignMaterial(TFBlocks.SORTING));
        });
       
    }

    public static void addLegacyPack() {
        //noinspection ConstantConditions
        if (Minecraft.getInstance() == null)
            // Normally Minecraft Client is never null except when generating through runData
            return;

        Minecraft.getInstance().getResourcePackRepository().sources.add(
                (consumer, iFactory) -> consumer.accept(
                        Pack.create(
                                TwilightForestMod.prefix("classic_textures").toString(),
                                false,
                                () -> new TwilightLegacyPack(
                                        FabricLoader
                                                .getInstance()
                                                .getModContainer(TwilightForestMod.ID)
                                                .get()
                                ),
                                iFactory,
                                Pack.Position.TOP,
								PackSource.BUILT_IN
                        )
                )
        );
    }

	private static Field field_EntityRenderersEvent$AddLayers_renderers;

//	@SuppressWarnings("unchecked")
//	public static void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
//		if (field_EntityRenderersEvent$AddLayers_renderers == null) {
//			try {
//				field_EntityRenderersEvent$AddLayers_renderers = EntityRenderersEvent.AddLayers.class.getDeclaredField("renderers");
//				field_EntityRenderersEvent$AddLayers_renderers.setAccessible(true);
//			} catch (NoSuchFieldException e) {
//				e.printStackTrace();
//			}
//		}
//		if (field_EntityRenderersEvent$AddLayers_renderers != null) {
//			event.getSkins().forEach(renderer -> {
//				LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
//				attachRenderLayers(Objects.requireNonNull(skin));
//			});
//			try {
//				((Map<EntityType<?>, EntityRenderer<?>>) field_EntityRenderersEvent$AddLayers_renderers.get(event)).values().stream().
//						filter(LivingEntityRenderer.class::isInstance).map(LivingEntityRenderer.class::cast).forEach(TFClientSetup::attachRenderLayers);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
//		renderer.addLayer(new ShieldLayer<>(renderer));
//		renderer.addLayer(new IceLayer<>(renderer));
	}
}
