package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFLayerDefinitions;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.particle.TFParticleType;
import twilightforest.client.providers.*;
import twilightforest.client.renderer.entity.IceLayer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.entity.TFEntities;
import twilightforest.inventory.TFContainers;
import twilightforest.item.*;
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

@Environment(EnvType.CLIENT)
public class TFClientSetup implements ClientModInitializer {

	public static boolean optifinePresent = false;

    @Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
        clientSetup();
	}

	public static class ForgeEvents {

		private static boolean optifineWarningShown = false;

		public static void showOptifineWarning(Screen screen) {
			if (optifinePresent && !optifineWarningShown && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen.get() && screen instanceof TitleScreen) {
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
        //ShaderManager.initShaders();
        ScreenEvents.BEFORE_INIT.register(((client, screen, scaledWidth, scaledHeight) -> LoadingScreenListener.onOpenGui(screen)));
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
        armorRegistry();
        
        //MinecraftForge.EVENT_BUS.register(new LoadingScreenListener());
        TFTileEntities.registerTileEntityRenders();
        TFTileEntities.registerTileEntitysItemRenders();
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

    public static void armorRegistry(){
        ArmorRenderingRegistry.registerModel( ((ArmorRenderingRegistry.ModelProvider) TFItems.arctic_boots), TFItems.arctic_boots, TFItems.arctic_leggings, TFItems.arctic_chestplate, TFItems.arctic_helmet);
        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.arctic_boots), TFItems.arctic_boots, TFItems.arctic_leggings, TFItems.arctic_chestplate, TFItems.arctic_helmet);

        ArmorRenderingRegistry.registerModel( ((ArmorRenderingRegistry.ModelProvider) TFItems.fiery_boots), TFItems.fiery_boots, TFItems.fiery_leggings, TFItems.fiery_chestplate, TFItems.fiery_helmet);
        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.fiery_boots), TFItems.fiery_boots, TFItems.fiery_leggings, TFItems.fiery_chestplate, TFItems.fiery_helmet);

        ArmorRenderingRegistry.registerModel( ((ArmorRenderingRegistry.ModelProvider) TFItems.knightmetal_boots), TFItems.knightmetal_boots, TFItems.knightmetal_leggings, TFItems.knightmetal_chestplate, TFItems.knightmetal_helmet);
        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.knightmetal_boots), TFItems.knightmetal_boots, TFItems.knightmetal_leggings, TFItems.knightmetal_chestplate, TFItems.knightmetal_helmet);

        ArmorRenderingRegistry.registerModel( ((ArmorRenderingRegistry.ModelProvider) TFItems.phantom_chestplate), TFItems.phantom_helmet, TFItems.phantom_chestplate);
        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.phantom_chestplate), TFItems.phantom_helmet, TFItems.phantom_chestplate);

        ArmorRenderingRegistry.registerModel( ((ArmorRenderingRegistry.ModelProvider) TFItems.yeti_boots), TFItems.yeti_boots, TFItems.yeti_leggings, TFItems.yeti_chestplate, TFItems.yeti_helmet);
        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.yeti_boots), TFItems.yeti_boots, TFItems.yeti_leggings, TFItems.yeti_chestplate, TFItems.yeti_helmet);

        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.naga_chestplate), TFItems.naga_chestplate, TFItems.naga_leggings);

        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.ironwood_boots), TFItems.ironwood_boots, TFItems.ironwood_leggings, TFItems.ironwood_chestplate, TFItems.ironwood_helmet);

        ArmorRenderingRegistry.registerTexture( ((ArmorRenderingRegistry.TextureProvider) TFItems.steeleaf_boots), TFItems.steeleaf_boots, TFItems.steeleaf_leggings, TFItems.steeleaf_chestplate, TFItems.steeleaf_helmet);
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
