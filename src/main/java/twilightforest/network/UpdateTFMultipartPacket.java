package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record UpdateTFMultipartPacket(int entityId, @Nullable Entity entity, @Nullable Map<Integer, PartDataHolder> data) implements CustomPacketPayload {

    public static final Type<UpdateTFMultipartPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_multipart_entity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTFMultipartPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), UpdateTFMultipartPacket::new);

    public UpdateTFMultipartPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), null, new HashMap<>());
        int id;
        while ((id = buf.readInt()) > 0) {
            this.data.put(id, PartDataHolder.decode(buf));
        }
    }

    public UpdateTFMultipartPacket(Entity entity) {
        this(entity.getId(), entity, entity instanceof TFPart.Owner owner
                ? Arrays.stream(owner.getParts()).filter(part -> part instanceof TFPart<?>).map(part -> (TFPart<?>) part).collect(Collectors.toMap(TFPart::getId, TFPart::writeData))
                : Map.of());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        if (this.entity == null && this.entityId <= 0) {
            throw new IllegalStateException("No entity id while encoding UpdateTFMultipartPacket");
        }
        if (this.data == null) {
            throw new IllegalStateException("Null data while encoding UpdateTFMultipartPacket");
        }
        buf.writeInt(this.entity == null ? this.entityId : this.entity.getId());
        this.data.forEach((id, data) -> {
            buf.writeInt(id);
            data.encode(buf);
        });
        buf.writeInt(-1);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record PartDataHolder(double x, double y, double z,
                                 float yRot, float xRot,
                                 float width, float height,
                                 boolean fixed,
                                 @Nullable List<SynchedEntityData.DataValue<?>> data) {

        public void encode(RegistryFriendlyByteBuf buffer) {
            buffer.writeDouble(this.x());
            buffer.writeDouble(this.y());
            buffer.writeDouble(this.z());
            buffer.writeFloat(this.yRot());
            buffer.writeFloat(this.xRot());
            buffer.writeFloat(this.width());
            buffer.writeFloat(this.height());
            buffer.writeBoolean(this.fixed());
            if (this.data() != null) {
                for (SynchedEntityData.DataValue<?> dataValue : this.data()) {
                    dataValue.write(buffer);
                }
            }
            buffer.writeByte(255);
        }

        static PartDataHolder decode(RegistryFriendlyByteBuf buffer) {
            return new PartDataHolder(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readFloat(), buffer.readFloat(),
                    buffer.readFloat(), buffer.readFloat(),
                    buffer.readBoolean(),
                    unpack(buffer));
        }

        private static List<SynchedEntityData.DataValue<?>> unpack(RegistryFriendlyByteBuf buf) {
            java.util.ArrayList<SynchedEntityData.DataValue<?>> list = new java.util.ArrayList<>();
            int id;
            while ((id = buf.readUnsignedByte()) != 255) {
                list.add(SynchedEntityData.DataValue.read(buf, id));
            }
            return list;
        }
    }
}
