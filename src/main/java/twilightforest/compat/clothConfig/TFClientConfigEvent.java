package twilightforest.compat.clothConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import shadow.cloth.autoconfig.serializer.Toml4jConfigSerializerExtended;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientSetup;
import twilightforest.compat.clothConfig.configEntry.ExtendedConfigEntry;
import twilightforest.compat.clothConfig.configFiles.TFConfig;

import java.util.Collections;

public class TFClientConfigEvent {

    public static void init(){
        clientConfigEvent();
        registerCustomAnnotations();
    }

    private static void clientConfigEvent(){
        //Move this to event class as this is the event in case the player changes anything within cloth config
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            AutoConfig.register(TFConfig.class, PartitioningSerializer.wrap(Toml4jConfigSerializerExtended::new));

            TFClientSetup.clientConfigInit();
            TwilightForestMod.commonConfigInit();

            AutoConfig.getConfigHolder(TFConfig.class).registerLoadListener((manager, newData) -> {
                TwilightForestMod.COMMON_CONFIG = newData.tfConfigCommon;
                //COMMON_CONFIG = AutoConfig.getConfigHolder(newData.getClass()).getConfig();
                TwilightForestMod.LOGGER.debug("Test: The TFConfigCommon has be reload after a load event!");
                return InteractionResult.SUCCESS;
            });

            AutoConfig.getConfigHolder(TFConfig.class).registerSaveListener((manager, newData) -> {
                TwilightForestMod.COMMON_CONFIG = newData.tfConfigCommon;
                TwilightForestMod.LOGGER.debug("Test: The TFConfigCommon has be reload after a save event!");
                //COMMON_CONFIG = AutoConfig.getConfigHolder(newData.getClass()).getConfig();
                return InteractionResult.SUCCESS;
            });

            //Client Config File
            AutoConfig.getConfigHolder(TFConfig.class).registerLoadListener((manager, newData) -> {
                TFClientSetup.CLIENT_CONFIG = newData.tfConfigClient;
                //CLIENT_CONFIG = AutoConfig.getConfigHolder(newData.tfConfigClient.getClass()).getConfig();
                return InteractionResult.SUCCESS;
            });

            AutoConfig.getConfigHolder(TFConfig.class).registerSaveListener((manager, newData) -> {
                TFClientSetup.CLIENT_CONFIG = newData.tfConfigClient;
                //CLIENT_CONFIG = AutoConfig.getConfigHolder(newData.tfConfigClient.getClass()).getConfig();
                return InteractionResult.SUCCESS;
            });

            TwilightForestMod.SERVER_SIDE_ONLY = false;
        });
    }

    private static void registerCustomAnnotations(){
        GuiRegistry registry = AutoConfig.getGuiRegistry(TFConfig.class);
        ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

        registry.registerAnnotationProvider(
                (i13n, field, config, defaults, guiProvider) -> {
                    ExtendedConfigEntry.BoundedFloat bounds
                            = field.getAnnotation(ExtendedConfigEntry.BoundedFloat.class);

                    return Collections.singletonList(
                            ENTRY_BUILDER.startFloatField(
                                            new TranslatableComponent(i13n),
                                            Utils.getUnsafely(field, config, 0f)

                                    )
                                    .setMax(bounds.max())
                                    .setMin(bounds.min())
                                    .setDefaultValue(() -> Utils.getUnsafely(field, defaults))
                                    .setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
                                    .build()
                    );
                },
                field -> field.getType() == float.class || field.getType() == Float.class,
                ExtendedConfigEntry.BoundedFloat.class
        );

        registry.registerAnnotationProvider(
                (i13n, field, config, defaults, guiProvider) -> {
                    ExtendedConfigEntry.BoundedDouble bounds
                            = field.getAnnotation(ExtendedConfigEntry.BoundedDouble.class);

                    return Collections.singletonList(
                            ENTRY_BUILDER.startDoubleField(
                                            new TranslatableComponent(i13n),
                                            Utils.getUnsafely(field, config, 0f)

                                    )
                                    .setMax(bounds.max())
                                    .setMin(bounds.min())
                                    .setDefaultValue(() -> Utils.getUnsafely(field, defaults))
                                    .setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
                                    .build()
                    );
                },
                field -> field.getType() == double.class || field.getType() == Double.class,
                ExtendedConfigEntry.BoundedDouble.class
        );
    }
}
