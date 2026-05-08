package twilightforest.util.multiparts;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Iterator;

public final class MultipartEntityUtil {
    private MultipartEntityUtil() {
    }

    public static Iterator<Entity> injectTFPartEntities(Iterator<Entity> iter) {
        return new MultipartEntityIteratorWrapper(iter);
    }

    public static Entity sendDirtyMultipartEntityData(Entity entity) {
        if (!(entity instanceof TFPart.Owner)) {
            return entity;
        }
        UpdateTFMultipartPacket packet = new UpdateTFMultipartPacket(entity);
        if (packet.data() == null || packet.data().isEmpty()) {
            return entity;
        }
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            if (ServerPlayNetworking.canSend(player, UpdateTFMultipartPacket.TYPE)) {
                ServerPlayNetworking.send(player, packet);
            }
        }
        return entity;
    }
}
