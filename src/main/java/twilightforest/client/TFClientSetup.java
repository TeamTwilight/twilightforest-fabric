package twilightforest.client;

import com.google.common.collect.Maps;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import twilightforest.TFConstants;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFLayerDefinitions;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.particle.TFParticleType;
import twilightforest.client.providers.*;
import twilightforest.compat.clothConfig.TFClientConfigEvent;
import twilightforest.compat.clothConfig.configFiles.TFConfig;
import twilightforest.compat.clothConfig.configFiles.TFConfigClient;
import twilightforest.dispenser.CrumbleDispenseBehavior;
import twilightforest.dispenser.FeatherFanDispenseBehavior;
import twilightforest.dispenser.MoonwormDispenseBehavior;
import twilightforest.dispenser.TransformationDispenseBehavior;
import twilightforest.entity.projectile.MoonwormShotEntity;
import twilightforest.entity.projectile.TwilightWandBoltEntity;
import twilightforest.inventory.TFContainers;
import twilightforest.item.TFItems;
import twilightforest.network.TFPacketHandler;
import twilightforest.tileentity.TFTileEntities;

import java.lang.reflect.Field;

@Environment(EnvType.CLIENT)
public class TFClientSetup implements ClientModInitializer {

	public static boolean optifinePresent = false;
    protected static boolean optifineWarningShown = false;

	public static TFConfigClient CLIENT_CONFIG;

    public static void clientConfigInit(){
        CLIENT_CONFIG = AutoConfig.getConfigHolder(TFConfig.class).getConfig().tfConfigClient;
    }

    @Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
        TFClientConfigEvent.init();

        TFPacketHandler.CHANNEL.initClient();
        TFLayerDefinitions.registerLayers();
        TFModelLayers.init();
        TFClientEvents.registerFabricEvents();
        TFClientEvents.registerModels();
        TFEntityRenderers.registerEntityRenderer();
        TFParticleType.registerFactories();
        //TODO: Double Checl that Shaders are even working
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
        DimensionSpecialEffects.EFFECTS.put(TFConstants.prefix("renderer"), renderInfo);

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
        TFBlocks.tfCompostables();
        TFBlocks.TFBurnables();
        TFBlocks.TFPots();
        TFSounds.registerParrotSounds();

        AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
        AxeItem.STRIPPABLES.put(TFBlocks.oak_log, TFBlocks.stripped_oak_log);
        AxeItem.STRIPPABLES.put(TFBlocks.canopy_log, TFBlocks.stripped_canopy_log);
        AxeItem.STRIPPABLES.put(TFBlocks.mangrove_log, TFBlocks.stripped_mangrove_log);
        AxeItem.STRIPPABLES.put(TFBlocks.dark_log, TFBlocks.stripped_dark_log);
        AxeItem.STRIPPABLES.put(TFBlocks.time_log, TFBlocks.stripped_time_log);
        AxeItem.STRIPPABLES.put(TFBlocks.transformation_log, TFBlocks.stripped_transformation_log);
        AxeItem.STRIPPABLES.put(TFBlocks.mining_log, TFBlocks.stripped_mining_log);
        AxeItem.STRIPPABLES.put(TFBlocks.sorting_log, TFBlocks.stripped_sorting_log);

        AxeItem.STRIPPABLES.put(TFBlocks.oak_wood, TFBlocks.stripped_oak_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.canopy_wood, TFBlocks.stripped_canopy_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.mangrove_wood, TFBlocks.stripped_mangrove_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.dark_wood, TFBlocks.stripped_dark_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.time_wood, TFBlocks.stripped_time_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.transformation_wood, TFBlocks.stripped_transformation_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.mining_wood, TFBlocks.stripped_mining_wood);
        AxeItem.STRIPPABLES.put(TFBlocks.sorting_wood, TFBlocks.stripped_sorting_wood);

        DispenserBlock.registerBehavior(TFItems.moonworm_queen, new MoonwormDispenseBehavior() {
            @Override
            protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
                return new MoonwormShotEntity(worldIn, position.x(), position.y(), position.z());
            }
        });

        DispenseItemBehavior idispenseitembehavior = new OptionalDispenseItemBehavior() {
            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                this.setSuccess(ArmorItem.dispenseArmor(source, stack));
                return stack;
            }
        };
        DispenserBlock.registerBehavior(TFBlocks.naga_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.lich_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.minoshroom_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.hydra_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.knight_phantom_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.ur_ghast_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.snow_queen_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.quest_ram_trophy.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.cicada.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.firefly.asItem(), idispenseitembehavior);
        DispenserBlock.registerBehavior(TFBlocks.moonworm.asItem(), idispenseitembehavior);

        DispenseItemBehavior pushmobsbehavior = new FeatherFanDispenseBehavior();
        DispenserBlock.registerBehavior(TFItems.peacock_fan.asItem(), pushmobsbehavior);

        DispenseItemBehavior crumblebehavior = new CrumbleDispenseBehavior();
        DispenserBlock.registerBehavior(TFItems.crumble_horn.asItem(), crumblebehavior);

        DispenseItemBehavior transformbehavior = new TransformationDispenseBehavior();
        DispenserBlock.registerBehavior(TFItems.transformation_powder.asItem(), transformbehavior);

        DispenserBlock.registerBehavior(TFItems.twilight_scepter, new MoonwormDispenseBehavior() {
            @Override
            protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
                return new TwilightWandBoltEntity(worldIn, position.x(), position.y(), position.z());
            }

            @Override
            protected void playSound(BlockSource source) {
                BlockPos pos = source.getPos();
                source.getLevel().playSound(null, pos, TFSounds.SCEPTER_PEARL, SoundSource.BLOCKS, 1, 1);
            }
        });

    }

    @Environment(EnvType.CLIENT)
    public static void armorRegistry() {
        ArcticArmorProvider arcticArmorProvider = new ArcticArmorProvider();
        ArmorRenderingRegistry.registerModel(arcticArmorProvider, TFItems.arctic_boots, TFItems.arctic_leggings, TFItems.arctic_chestplate, TFItems.arctic_helmet);
        ArmorRenderingRegistry.registerTexture(arcticArmorProvider, TFItems.arctic_boots, TFItems.arctic_leggings, TFItems.arctic_chestplate, TFItems.arctic_helmet);

        FieryArmorProvider fieryArmorProvider = new FieryArmorProvider();
        ArmorRenderingRegistry.registerModel(fieryArmorProvider, TFItems.fiery_boots, TFItems.fiery_leggings, TFItems.fiery_chestplate, TFItems.fiery_helmet);
        ArmorRenderingRegistry.registerTexture(fieryArmorProvider, TFItems.fiery_boots, TFItems.fiery_leggings, TFItems.fiery_chestplate, TFItems.fiery_helmet);

        KnightArmorProvider knightArmorProvider = new KnightArmorProvider();
        ArmorRenderingRegistry.registerModel(knightArmorProvider, TFItems.knightmetal_boots, TFItems.knightmetal_leggings, TFItems.knightmetal_chestplate, TFItems.knightmetal_helmet);
        ArmorRenderingRegistry.registerTexture(knightArmorProvider, TFItems.knightmetal_boots, TFItems.knightmetal_leggings, TFItems.knightmetal_chestplate, TFItems.knightmetal_helmet);

        PhantomArmorProvider phantomArmorProvider = new PhantomArmorProvider();
        ArmorRenderingRegistry.registerModel(phantomArmorProvider, TFItems.phantom_helmet, TFItems.phantom_chestplate);
        ArmorRenderingRegistry.registerTexture(phantomArmorProvider, TFItems.phantom_helmet, TFItems.phantom_chestplate);

        YetiArmorProvider yetiArmorProvider = new YetiArmorProvider();
        ArmorRenderingRegistry.registerModel( yetiArmorProvider, TFItems.yeti_boots, TFItems.yeti_leggings, TFItems.yeti_chestplate, TFItems.yeti_helmet);
        ArmorRenderingRegistry.registerTexture(yetiArmorProvider, TFItems.yeti_boots, TFItems.yeti_leggings, TFItems.yeti_chestplate, TFItems.yeti_helmet);

        ArmorRenderingRegistry.registerTexture(new NagaArmorProvider(), TFItems.naga_chestplate, TFItems.naga_leggings);

        ArmorRenderingRegistry.registerTexture(new IronwoodArmorProvider(), TFItems.ironwood_boots, TFItems.ironwood_leggings, TFItems.ironwood_chestplate, TFItems.ironwood_helmet);

        ArmorRenderingRegistry.registerTexture(new SteeleafArmorProvider(), TFItems.steeleaf_boots, TFItems.steeleaf_leggings, TFItems.steeleaf_chestplate, TFItems.steeleaf_helmet);
    }


    public static void addLegacyPack() {
        //noinspection ConstantConditions
        if (Minecraft.getInstance() == null)
            // Normally Minecraft Client is never null except when generating through runData
            return;

        Minecraft.getInstance().getResourcePackRepository().sources.add(
                (consumer, iFactory) -> consumer.accept(
                        Pack.create(
                                TFConstants.prefix("classic_textures").toString(),
                                false,
                                () -> new TwilightLegacyPack(
                                        FabricLoader
                                                .getInstance()
                                                .getModContainer(TFConstants.ID)
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
