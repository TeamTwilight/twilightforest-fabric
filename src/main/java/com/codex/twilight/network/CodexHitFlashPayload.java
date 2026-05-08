package com.codex.twilight.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * F2.8 — server → client S2C payload announcing that a Twilight Forest entity
 * just took damage at a given world position. The client mod uses this to
 * spawn a "hit-spark" particle burst ({@code CRIT} / {@code CRIT_MAGIC}) so
 * SlashBlade-style attacks on TF mobs feel visually meaty without needing the
 * server to send raw vanilla particle packets (those are recipient-blind).
 *
 * <p>Vanilla clients without codex-twilight installed never decode this
 * payload — fabric-networking-api-v1 silently drops unrecognized custom payloads
 * for them, so the disguise path is unaffected.
 *
 * <p>Payload layout: three doubles for world position. Light enough to broadcast
 * to all nearby tracking players without measurable bandwidth cost.
 */
public record CodexHitFlashPayload(double x, double y, double z) implements CustomPacketPayload {

    public static final Type<CodexHitFlashPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("codex_twilight", "hit_flash"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CodexHitFlashPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, CodexHitFlashPayload::x,
            ByteBufCodecs.DOUBLE, CodexHitFlashPayload::y,
            ByteBufCodecs.DOUBLE, CodexHitFlashPayload::z,
            CodexHitFlashPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
