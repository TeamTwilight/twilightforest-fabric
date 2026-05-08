package com.codex.twilight.network;

import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.network.CycleMapSlotPacket;
import twilightforest.network.GogglesZoomPacket;
import twilightforest.network.GradualGlidePacket;
import twilightforest.network.MagicMapPacket;
import twilightforest.network.MazeMapPacket;
import twilightforest.network.MovePlayerPacket;
import twilightforest.network.TFBossBarPacket;
import twilightforest.network.PerformDoubleJumpPacket;
import twilightforest.network.PerformSidestepPacket;
import twilightforest.network.SetMasonJarItemPacket;
import twilightforest.network.SwapHotbarPacket;
import twilightforest.network.TravellersWingsStatePacket;
import twilightforest.network.UncraftingGuiPacket;
import twilightforest.network.UpdateThrownPacket;
import twilightforest.network.WipeOreMeterPacket;

/**
 * F2.8 — central wiring for codex-twilight S2C packets.
 *
 * <p>{@link #bootstrapServer()} runs once during the main mod initializer
 * (server side) and registers payload codecs + the damage event hook that
 * broadcasts {@link CodexHitFlashPayload} when a Twilight Forest entity
 * takes damage. The client-side mirror lives in
 * {@code com.codex.twilight.client.CodexTwilightClient}.
 *
 * <p>Detection rule: any living entity whose registered type id belongs to the
 * {@code twilightforest} namespace is treated as a TF mob. Vanilla mobs are not
 * matched because paired-client mode no longer uses server-side disguise types.
 */
public final class CodexNetworking {

    private static final Logger LOGGER = LoggerFactory.getLogger("CodexTwilight/network");

    private CodexNetworking() {
    }

    public static void bootstrapServer() {
        // Register payload type once — must run before any send / receive.
        PayloadTypeRegistry.playS2C().register(CodexHitFlashPayload.TYPE, CodexHitFlashPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(CodexGogglesSurveyPayload.TYPE, CodexGogglesSurveyPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.UpdateTFMultipartPacket.TYPE, twilightforest.network.UpdateTFMultipartPacket.STREAM_CODEC);
        // P5.b — 12 additional 1:1 ports of upstream packets (data-only, handlers register elsewhere).
        PayloadTypeRegistry.playS2C().register(twilightforest.network.SyncQuestsPacket.TYPE, twilightforest.network.SyncQuestsPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.EnforceProgressionStatusPacket.TYPE, twilightforest.network.EnforceProgressionStatusPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.UpdateDeathTimePacket.TYPE, twilightforest.network.UpdateDeathTimePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.MissingAdvancementToastPacket.TYPE, twilightforest.network.MissingAdvancementToastPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.CreateMovingCicadaSoundPacket.TYPE, twilightforest.network.CreateMovingCicadaSoundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.LifedrainParticlePacket.TYPE, twilightforest.network.LifedrainParticlePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.SpawnFallenLeafFromPacket.TYPE, twilightforest.network.SpawnFallenLeafFromPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.SyncUncraftingTableConfigPacket.TYPE, twilightforest.network.SyncUncraftingTableConfigPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.SpawnCharmPacket.TYPE, twilightforest.network.SpawnCharmPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.ParticlePacket.TYPE, twilightforest.network.ParticlePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.AreaProtectionPacket.TYPE, twilightforest.network.AreaProtectionPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(twilightforest.network.StructureProtectionPacket.TYPE, twilightforest.network.StructureProtectionPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(PerformSidestepPacket.TYPE, PerformSidestepPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(SwapHotbarPacket.TYPE, SwapHotbarPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(CycleMapSlotPacket.TYPE, CycleMapSlotPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(UncraftingGuiPacket.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                if (!(player.containerMenu instanceof UncraftingMenu menu)) {
                    return;
                }
                switch (payload.operationType()) {
                    case 0 -> menu.unrecipeInCycle++;
                    case 1 -> menu.unrecipeInCycle--;
                    case 2 -> menu.ingredientsInCycle++;
                    case 3 -> menu.ingredientsInCycle--;
                    case 4 -> menu.recipeInCycle++;
                    case 5 -> menu.recipeInCycle--;
                    default -> {
                        return;
                    }
                }
                menu.slotsChanged(payload.operationType() < 4 ? menu.tinkerInput : menu.assemblyMatrix);
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(PerformDoubleJumpPacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer player = context.player();
                    if (!TravellersGearLogic.performDoubleJump(player)) {
                        TravellersGearLogic.handleDoubleJumpAbuse(player);
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(PerformSidestepPacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer player = context.player();
                    if (!TravellersGearLogic.tryPerformSidestep(player, payload.isLeftStepSide())) {
                        TravellersGearLogic.handleSidestepAbuse(player);
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(SwapHotbarPacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer player = context.player();
                    TravellersArmorBeltItem.travellersTrySwapHotbar(player);
                    player.getInventory().setChanged();
                    player.containerMenu.broadcastChanges();
                }));
        ServerPlayNetworking.registerGlobalReceiver(CycleMapSlotPacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer player = context.player();
                    ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
                    ItemDisplayContents contents = headStack.get(TFDataComponents.ITEM_DISPLAY);
                    if (contents == null || contents.isEmpty()
                            || !TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER)) {
                        return;
                    }
                    ItemDisplayContents.Mutable mutable = new ItemDisplayContents.Mutable(contents);
                    int oldIndex = mutable.chosenMapSlot();
                    int newIndex = mutable.cycleChosenMapSlot();
                    if (oldIndex != newIndex) {
                        headStack.set(TFDataComponents.ITEM_DISPLAY, mutable.toImmutable());
                        player.getInventory().setChanged();
                        player.playNotifySound(newIndex == -1 ? TFSounds.CYCLE_MAPS_EMPTY : TFSounds.CYCLE_MAPS, player.getSoundSource(), 1.0F, 1.0F);
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer sender = context.player();
                    Player player = sender.level().getPlayerByUUID(payload.playerUUID());
                    if (player == null || player != sender) {
                        return;
                    }
                    TFDataAttachments.set(player, TFDataAttachments.IS_GRADUALLY_GLIDING, payload.isGraduallyGliding());
                    GradualGlidePacket update = new GradualGlidePacket(payload.isGraduallyGliding(), player.getUUID());
                    for (ServerPlayer viewer : PlayerLookup.tracking(player)) {
                        if (ServerPlayNetworking.canSend(viewer, GradualGlidePacket.TYPE)) {
                            ServerPlayNetworking.send(viewer, update);
                        }
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer sender = context.player();
                    Player player = sender.level().getPlayerByUUID(payload.playerUUID());
                    if (player == null || player != sender) {
                        return;
                    }
                    if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ZOOM_ABILITY)) {
                        return;
                    }
                    TFDataAttachments.set(player, TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, payload.isUsingZoom());
                    player.level().playSound(null, player.blockPosition(), payload.isUsingZoom() ? TFSounds.GOGGLES_ZOOM_IN : TFSounds.GOGGLES_ZOOM_OUT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    GogglesZoomPacket update = new GogglesZoomPacket(payload.isUsingZoom(), player.getUUID());
                    for (ServerPlayer viewer : PlayerLookup.tracking(player)) {
                        if (ServerPlayNetworking.canSend(viewer, GogglesZoomPacket.TYPE)) {
                            ServerPlayNetworking.send(viewer, update);
                        }
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(WipeOreMeterPacket.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    ServerPlayer player = context.player();
                    ItemStack heldStack = player.getItemInHand(payload.hand());
                    if (!heldStack.is(TFItems.ORE_METER.get())) {
                        return;
                    }
                    heldStack.remove(TFDataComponents.ORE_DATA);
                    heldStack.remove(TFDataComponents.ORE_SCANNER_DATA);
                    heldStack.remove(TFDataComponents.ORE_FILTER);
                    player.level().playSound(null, player.blockPosition(), TFSounds.ORE_METER_CLEAR, SoundSource.PLAYERS, 0.75F, 1.0F);
                }));

        // Server-side hit hook: fabric-entity-events-v1 fires AFTER_DAMAGE per damage event.
        // We broadcast the hit-flash payload to every tracking player so paired clients can
        // spawn vanilla CRIT particles at the impact position.
        net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DAMAGE.register(
                (LivingEntity entity, DamageSource source, float baseDamage, float damageTaken, boolean blocked) -> {
                    ResourceLocation typeId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                    if (typeId == null || !"twilightforest".equals(typeId.getNamespace())) return;
                    if (entity.level().isClientSide()) return;
                    if (!(entity.level() instanceof ServerLevel)) return;
                    if (damageTaken <= 0.0F || blocked) return;

                    CodexHitFlashPayload payload = new CodexHitFlashPayload(
                            entity.getX(),
                            entity.getY() + entity.getBbHeight() * 0.5,
                            entity.getZ());
                    for (ServerPlayer viewer : PlayerLookup.tracking(entity)) {
                        try {
                            ServerPlayNetworking.send(viewer, payload);
                        } catch (Throwable ignored) {
                            // Defensive — never let a bad recipient stall damage processing.
                        }
                    }
                    if (source.getEntity() instanceof Player attacker && attacker instanceof ServerPlayer self) {
                        try {
                            ServerPlayNetworking.send(self, payload);
                        } catch (Throwable ignored) {
                        }
                    }
                });
        LOGGER.info("CodexNetworking bootstrap complete (Codex visual payloads + TF traveller/yeti/jar/ore payloads registered).");
    }

    public static boolean canSendPairedClientPayload(ServerPlayer player) {
        try {
            return ServerPlayNetworking.canSend(player, CodexHitFlashPayload.TYPE)
                    || ServerPlayNetworking.canSend(player, CodexGogglesSurveyPayload.TYPE);
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void sendGogglesSurvey(ServerPlayer player,
                                         double x, double y, double z,
                                         float yaw, float pitch) {
        try {
            if (!ServerPlayNetworking.canSend(player, CodexGogglesSurveyPayload.TYPE)) return;
            ServerPlayNetworking.send(player, new CodexGogglesSurveyPayload(x, y, z, yaw, pitch));
        } catch (Throwable ignored) {
        }
    }

    /**
     * Run on server stop / startup events that need to flush state. Currently
     * a no-op; reserved so the entry point exists for future packet kinds.
     */
    public static void registerLifecycle() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {});
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {});
    }
}
