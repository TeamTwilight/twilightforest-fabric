package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.fan.FeatherFanCapabilityHandler;
import twilightforest.capabilities.fan.FeatherFanFallCapability;

public class UpdateFeatherFanFallPacket implements S2CPacket {
	private final int entityID;
	private final boolean falling;

	public UpdateFeatherFanFallPacket(int id, FeatherFanFallCapability cap) {
		this.entityID = id;
		this.falling = cap.getFalling();
	}

	public UpdateFeatherFanFallPacket(Entity entity, FeatherFanCapabilityHandler cap) {
		this(entity.getId(), cap);
	}

	public UpdateFeatherFanFallPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
		this.falling = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
		buf.writeBoolean(this.falling);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
		Entity entity = Minecraft.getInstance().level.getEntity(entityID);
		if (entity instanceof LivingEntity) {
			CapabilityList.FEATHER_FAN_FALLING.maybeGet(entity).ifPresent(cap -> cap.setFalling(falling));
		}
	}
}
