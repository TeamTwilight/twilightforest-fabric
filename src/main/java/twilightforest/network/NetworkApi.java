package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.TFConstants;

import java.util.function.Consumer;

import net.minecraft.network.FriendlyByteBuf;

public class NetworkApi {
    public PacketInfo messageBuilder(Class<ChangeBiomePacket> changeBiomePacketClass, int i) {

        return new PacketInfo(changeBiomePacketClass);
    }

    public static class PacketInfo {
        private Class<ChangeBiomePacket> changeBiomePacketClass;
        private Consumer<FriendlyByteBuf> encoder;
        private Consumer<FriendlyByteBuf> decoder;
        private ClientPlayNetworking.PlayChannelHandler handler;

        public PacketInfo(Class<ChangeBiomePacket> changeBiomePacketClass) {
        }

        public PacketInfo encoder(Consumer<FriendlyByteBuf> encode) {
            this.encoder = encode;
            return this;
        }

        public PacketInfo decoder(Consumer<FriendlyByteBuf> decode) {
            this.decoder = decode;
            return this;
        }

        public PacketInfo consumer(ClientPlayNetworking.PlayChannelHandler handler) {
            this.handler = handler;
            return this;
        }

        public void add() {
            ClientPlayNetworking.registerGlobalReceiver(TFConstants.prefix("channel"), handler);
        }
    }
}
