package com.codex.twilight.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server -> paired client request to draw the Travellers' Goggles survey flash.
 */
public record CodexGogglesSurveyPayload(double x, double y, double z, float yaw, float pitch)
        implements CustomPacketPayload {

    public static final Type<CodexGogglesSurveyPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("codex_twilight", "goggles_survey"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CodexGogglesSurveyPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.DOUBLE, CodexGogglesSurveyPayload::x,
                    ByteBufCodecs.DOUBLE, CodexGogglesSurveyPayload::y,
                    ByteBufCodecs.DOUBLE, CodexGogglesSurveyPayload::z,
                    ByteBufCodecs.FLOAT, CodexGogglesSurveyPayload::yaw,
                    ByteBufCodecs.FLOAT, CodexGogglesSurveyPayload::pitch,
                    CodexGogglesSurveyPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
