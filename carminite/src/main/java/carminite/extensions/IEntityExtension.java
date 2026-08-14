package carminite.extensions;

import carminite.entity.IEntityWithComplexSpawn;
import carminite.network.internal.AdvancedAddEntityPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public interface IEntityExtension {
	default void carminite$sendPairingData(ServerPlayer serverPlayer, Consumer<CustomPacketPayload> bundleBuilder) {
		if (this instanceof IEntityWithComplexSpawn) {
			bundleBuilder.accept(new AdvancedAddEntityPacket((Entity) this));
		}
	}
}