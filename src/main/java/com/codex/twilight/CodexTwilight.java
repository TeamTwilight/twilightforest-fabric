package com.codex.twilight;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TFRegistries;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFDataSerializers;
import twilightforest.init.TFDensityFunctions;
import twilightforest.init.TFEntities;
import twilightforest.init.TFCaveCarvers;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFAttributes;
import twilightforest.init.TFFeatureModifiers;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFParticleTypes;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.TFStructurePlacementTypes;
import twilightforest.init.TFStructureProcessors;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

/**
 * Codex Twilight entry point.
 *
 * Scope (first iteration):
 *   - Dimension/noise tweaks live in resources/data/catty (carried over from the
 *     legacy catty_twilight_realm datapack; phase F will fold the rest in).
 *   - Worldgen content (configured/placed features, structures) lives under
 *     resources/data/codex_twilight; sub-bootstraps register Java-side hooks
 *     where datapack JSON is not enough.
 *   - Custom items, blocks, entities, sounds, and particles sync as real
 *     Twilight registry ids. The paired client loads the bundled assets from
 *     this same mod jar.
 *
 * Twilight entity migration is incremental: official registry IDs are owned by
 * Java EntityTypes here, with individual classes promoted from compatibility
 * stand-ins as their server-side dependency chains are ported.
 */
public final class CodexTwilight implements ModInitializer {

    public static final String MOD_ID = "codex_twilight";

    public static String id(String path) {
        return MOD_ID + ":" + path;
    }

    @Override
    public void onInitialize() {
        DynamicRegistries.register(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC);
        DynamicRegistries.register(TFRegistries.Keys.RESTRICTIONS, twilightforest.util.Restriction.CODEC);
        DynamicRegistries.register(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS,
            twilightforest.init.custom.ChunkBlanketProcessors.DISPATCH_CODEC);
        DynamicRegistries.register(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER,
            twilightforest.init.custom.TemplateMarkerHandlers.DISPATCH_CODEC);
        DynamicRegistries.register(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST,
            twilightforest.world.components.structures.util.TemplateMarkerHandlerList.CODEC);
        DynamicRegistries.register(TFRegistries.Keys.WOOD_PALETTES,
            twilightforest.util.WoodPalette.CODEC);
        DynamicRegistries.registerSynced(TFRegistries.Keys.TRAVELLERS_MODIFIERS,
            twilightforest.item.travellers_gear.modifiers.TravellersModifier.CODEC);
        // TINY_BIRD_VARIANT and DWARF_RABBIT_VARIANT are referenced by TFDataSerializers via
        // ByteBufCodecs.holderRegistry(...) in entity SynchedEntityData. The registry must therefore
        // exist on the client too — register them as SYNCED so fabric-registry-sync ships the registry
        // (with all data-pack entries) to the connecting client during play handshake.
        DynamicRegistries.registerSynced(TFRegistries.Keys.TINY_BIRD_VARIANT,
            twilightforest.entity.passive.TinyBirdVariant.DIRECT_CODEC);
        DynamicRegistries.registerSynced(TFRegistries.Keys.DWARF_RABBIT_VARIANT,
            twilightforest.entity.passive.DwarfRabbitVariant.DIRECT_CODEC);
        TFDataSerializers.bootstrap();
        twilightforest.config.TFConfig.load(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir());
        twilightforest.config.ConfigSetup.loadConfigs();
        twilightforest.util.TFRemapper.addRegistryAliases();
        twilightforest.network.ModUpdateURLInterceptor.bootstrap();
        twilightforest.init.TFGameRules.register();
        twilightforest.init.custom.ChunkBlanketProcessors.bootstrapTypes();
        twilightforest.init.custom.TemplateMarkerHandlers.bootstrapTypes();
        // F2.8 — register S2C payload type early so it's available before any TF mob hits arrive.
        com.codex.twilight.network.CodexNetworking.bootstrapServer();
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
            twilightforest.config.ConfigSetup.syncUncraftingConfig(handler.player));
        TFAttributes.bootstrap();
        TFEntities.ARMORED_GIANT.get();
        TFEntities.addEntityAttributes();
        twilightforest.init.TFRecipes.bootstrap();
        twilightforest.init.custom.Enforcements.bootstrap();
        twilightforest.init.custom.TravellersModifierTypes.bootstrap();
        twilightforest.init.custom.ItemDisplays.bootstrap();
        twilightforest.init.TFMenuTypes.bootstrap();
        twilightforest.events.CapabilityEvents.bootstrap();
        twilightforest.events.CharmEvents.bootstrap();
        twilightforest.events.HostileMountEvents.bootstrap();
        twilightforest.events.MiscEvents.bootstrap();
        twilightforest.events.ProgressionEvents.bootstrap();
        twilightforest.events.TravellersGearEvents.bootstrap();
        twilightforest.events.ToolEvents.bootstrap();
        twilightforest.events.EntityEvents.bootstrap();
        twilightforest.init.TFAdvancements.bootstrap();
        twilightforest.init.TFStats.bootstrap();
        twilightforest.init.TFItemSubPredicates.bootstrap();
        twilightforest.init.TFLoot.bootstrap();
        twilightforest.init.TFLootModifiers.bootstrap();
        twilightforest.init.TFMobEffects.bootstrap();
        twilightforest.init.TFEnchantmentEffects.bootstrap();
        twilightforest.events.SkullCandleEvents.bootstrap();
        TFDataComponents.TRANSLATABLE_BOOK.toString();
        TFMapDecorations.AURORA_PALACE.value();
        TFSounds.bootstrap();
        twilightforest.init.TFItems.bootstrap();
        twilightforest.compat.curios.CuriosCompat.bootstrap();
        twilightforest.init.TFCreativeTabs.bootstrap();
        twilightforest.events.RegistrationEvents.bootstrap();
        twilightforest.init.TFPOITypes.bootstrap();
        twilightforest.init.TFBlockEntities.bootstrap();
        twilightforest.dispenser.TFDispenserBehaviors.init();
        TFParticleTypes.bootstrap();
        TFFeatureModifiers.bootstrap();
        TFDensityFunctions.bootstrap();
        TFCaveCarvers.bootstrap();
        TFStructurePlacementTypes.bootstrap();
        TFStructureTypes.bootstrap();
        TFStructurePieceTypes.bootstrap();
        TFStructureProcessors.bootstrap();

        // ===== LANE_A_BOOTSTRAP (Claude — terrain/density/layer system) =====
        // Add density router registrations, datapack layer-stack registry, etc. here.
        // Owned per AGENTS.md. Do NOT modify if you are Lane B.
        twilightforest.init.custom.BiomeLayerTypes.bootstrap();
        twilightforest.init.TFBiomeSources.bootstrap();
        twilightforest.init.TFFeatures.bootstrap();
        DynamicRegistries.register(TFRegistries.Keys.BIOME_STACK,
                twilightforest.init.custom.BiomeLayerStack.DISPATCH_CODEC);
        DynamicRegistries.register(TFRegistries.Keys.BIOME_TERRAIN_DATA,
                twilightforest.world.components.layer.BiomeDensitySource.CODEC);
        // ===== END LANE_A_BOOTSTRAP =====

        // ===== LANE_B_BOOTSTRAP (Copilot — entities + paired-client renderers) =====
        // Add SpawnPlacements.register() calls and entity wiring here.
        // Owned per AGENTS.md. Do NOT modify if you are Lane A.
        TFBlocks.CICADA.get();
        TFBlocks.FIREFLY.get();
        // ===== END LANE_B_BOOTSTRAP =====

        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleHooks::setCurrentServer);
        // Phase F1.4 — /codex ops command
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register(
                twilightforest.command.CodexCommand::register);
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register(
                twilightforest.command.TFCommand::register);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> ServerLifecycleHooks.setCurrentServer(null));

        System.out.println("[CodexTwilight] initialized (v0.1.0 scaffold)");
    }
}
