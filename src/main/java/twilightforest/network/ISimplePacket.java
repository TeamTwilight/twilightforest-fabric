package twilightforest.network;

import java.io.IOException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;

/**
 * Packet interface to add common methods for registration
 */
public abstract class ISimplePacket<T extends ISimplePacket> implements Packet<PacketListener> {

    /**
     * Encodes a packet for the buffer
     *
     * @param buf Buffer instance
     */
    public abstract void encode(FriendlyByteBuf buf);

    /**
     * Handles receiving the packet
     *
     * @param playerEntity the packet sender
     */
    public abstract void onMessage(Player playerEntity);

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(buf);
    }

    @Override
    public void handle(PacketListener listener) { }

}