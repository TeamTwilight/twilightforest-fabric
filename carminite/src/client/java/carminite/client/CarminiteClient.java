package carminite.client;

import carminite.entity.IEntityWithComplexSpawn;
import carminite.entity.IMultiPartEntity;
import carminite.entity.PartEntity;
import carminite.mixin.BlockableEventLoopAccessor;
import carminite.network.internal.AdvancedAddEntityPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class CarminiteClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(AdvancedAddEntityPacket.TYPE, CarminiteClient::handleAddComplexEntity);
		ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof IMultiPartEntity partEntity && partEntity.isMultipartEntity()) {
				for (PartEntity<?> part : partEntity.getParts()) {
					world.carminite$getPartEntityMap().put(part.getId(), part);
				}
			}
		});
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if (entity instanceof IMultiPartEntity partEntity && partEntity.isMultipartEntity()) {
				for (PartEntity<?> part : partEntity.getParts()) {
					world.carminite$getPartEntityMap().remove(part.getId());
				}
			}
		});
	}

	public static void handleAddComplexEntity(AdvancedAddEntityPacket advancedAddEntityPacket, ClientPlayNetworking.Context context) {
		try {
			Entity entity = context.player().level().getEntity(advancedAddEntityPacket.entityId());
			if (entity instanceof IEntityWithComplexSpawn entityAdditionalSpawnData) {
				final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(advancedAddEntityPacket.customPayload()), entity.registryAccess());
				try {
					execute(context.client(), () -> entityAdditionalSpawnData.readSpawnData(buf));
				} finally {
					buf.release();
				}
			}
		} catch (Throwable t) {
			context.responseSender().disconnect(Component.literal("Failed to handle advanced entity packet."));
		}
	}

	private static void execute(Minecraft minecraft, Runnable task) {
		if (minecraft.isSameThread()) {
			task.run();
		} else {
			((BlockableEventLoopAccessor) minecraft).carminite$callSubmitAsync(task);
		}
	}
}