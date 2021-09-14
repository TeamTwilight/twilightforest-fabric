package twilightforest.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import twilightforest.TwilightForestMod;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * A small network implementation/wrapper using AbstractPackets instead of IMessages.
 * Instantiate in your mod class and register your packets accordingly.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NetworkWrapper {
    private static final String PROTOCOL_VERSION = "1";
    private final ResourceLocation identifier;

    Map<Integer, Packet> packetIdMap = new HashMap<>();
    Map<Class<?>, Packet> packetClassMap = new HashMap<>();

    private int id;

    /**
     * Creates a new network wrapper
     */
    public NetworkWrapper(ResourceLocation identifer) {
        this.identifier = identifer;
        ServerPlayNetworking.registerGlobalReceiver(identifier, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int id = packetByteBuf.readInt();
            Packet packet = packetIdMap.get(id);
            ISimplePacket object = (ISimplePacket) packet.decoder.apply(packetByteBuf);
//            if(object instanceof IThreadsafePacket)
            TwilightForestMod.LOGGER.info(Thread.currentThread());
            PacketUtils.ensureRunningOnSameThread(object, serverPlayNetworkHandler, serverPlayerEntity.getLevel());
            TwilightForestMod.LOGGER.info(Thread.currentThread());
            packet.consumer.accept(object, serverPlayerEntity);
        });


    }

    @Environment(EnvType.CLIENT)
    public void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(identifier, (minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int id = packetByteBuf.readInt();
            Packet packet = packetIdMap.get(id);
            ISimplePacket object = (ISimplePacket) packet.decoder.apply(packetByteBuf);
//            if(object instanceof IThreadsafePacket)
            //TwilightForestMod.LOGGER.info(Thread.currentThread());
            //PacketUtils.ensureRunningOnSameThread(object, clientPlayNetworkHandler, minecraftClient);
            //TwilightForestMod.LOGGER.info(Thread.currentThread());
            packet.consumer.accept(object, minecraftClient.player);
        });
    }

    public  <T extends ISimplePacket> void registerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, EnvType side) {
        registerPacket(clazz, ISimplePacket::encode, decoder, ISimplePacket::onMessage, side);
    }

    /**
     * Registers a new generic packet
     * @param clazz      Packet class
     * @param encoder    Encodes a packet to the buffer
     * @param decoder    Packet decoder, typically the constructor
     * @param direction  Network direction for validation. Pass null for no direction
     */
    public <T extends ISimplePacket> void registerPacket(Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Player> consumer, EnvType direction) {
        Packet packet = Packet.of(id, clazz, encoder, decoder, consumer, direction);

        packetIdMap.put(id, packet);
        packetClassMap.put(clazz, packet);
        id++;
    }


    /* Sending packets */

    /**
     * Sends a packet to the server
     * @param msg  Packet to send
     */
    public void sendToServer(ISimplePacket msg) {
        Class<?> clazz = msg.getClass();
        Packet packet = packetClassMap.get(clazz);

        if(packet == null || packet.direction == EnvType.CLIENT) return;

        FriendlyByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(packet.id);
        packet.encoder.accept(msg, packetByteBuf);
        ClientPlayNetworking.send(identifier, packetByteBuf);
    }

    /**
     * Sends a packet to the given packet distributor
     * @param target   Packet target
     * @param msg  Packet to send
     */
    public void send(ServerPlayer target, ISimplePacket msg) {
        Class<?> clazz = msg.getClass();
        Packet packet = packetClassMap.get(clazz);

        if(packet == null || packet.direction == EnvType.SERVER) return;

        FriendlyByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(packet.id);

        packet.encoder.accept(msg, packetByteBuf);
        ServerPlayNetworking.send(target, identifier, packetByteBuf);
    }

    /**
     * Sends a vanilla packet to the given entity
     * @param player  Player receiving the packet
     * @param packet  Packet
     */
    public void sendVanillaPacket(net.minecraft.network.protocol.Packet<?> packet, Entity player) {
        if (player instanceof ServerPlayer && ((ServerPlayer) player).connection != null) {
            ((ServerPlayer) player).connection.send(packet);
        }
    }

    /**
     * Sends a packet to a player
     * @param msg     Packet
     * @param player  Player to send
     */
    public void sendTo(ISimplePacket msg, ServerPlayer player) {
        send(player, msg);
    }

    /**
     * Sends a packet to players near a location
     * @param msg          Packet to send
     * @param serverWorld  World instance
     * @param position     Position within range
     */
    public void sendToClientsAround(ISimplePacket msg, ServerLevel serverWorld, BlockPos position) {
        for (ServerPlayer playerEntity : PlayerLookup.around(serverWorld, position, 16)) {
            send(playerEntity, msg);
        }
    }

    public static class Packet<T extends ISimplePacket> {
        EnvType direction;
        BiConsumer<ISimplePacket, FriendlyByteBuf> encoder;
        Function<FriendlyByteBuf, ISimplePacket> decoder;
        BiConsumer<T, Player> consumer;
        Class<?> clazz;
        int id;

        public static <T extends ISimplePacket> Packet of(int id, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Player> consumer, EnvType side) {
            Packet packet = new Packet();
            packet.clazz = clazz;
            packet.encoder = encoder;
            packet.decoder = decoder;
            packet.consumer = consumer;
            packet.direction = side;
            packet.id = id;
            return packet;
        }

    }
}