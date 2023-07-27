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
import twilightforest.capabilities.thrown.YetiThrowCapability;

public class UpdateThrownPacket implements S2CPacket {

	private final int entityID;
	private final boolean thrown;
	private int thrower = 0;
	private final int throwCooldown;

	public UpdateThrownPacket(int id, YetiThrowCapability cap) {
		this.entityID = id;
		this.thrown = cap.getThrown();
		this.throwCooldown = cap.getThrowCooldown();
		if(cap.getThrower() != null) {
			this.thrower = cap.getThrower().getId();
		}
	}

	public UpdateThrownPacket(Entity entity, YetiThrowCapability cap) {
		this(entity.getId(), cap);
	}

	public UpdateThrownPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
		this.thrown = buf.readBoolean();
		this.thrower = buf.readInt();
		this.throwCooldown = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
		buf.writeBoolean(this.thrown);
		buf.writeInt(this.thrower);
		buf.writeInt(this.throwCooldown);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
		Handler.onMessage(this);
	}

	public static class Handler {

		public static void onMessage(UpdateThrownPacket message) {
//			ctx.get().enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
				if (entity instanceof LivingEntity) {
					CapabilityList.YETI_THROWN.maybeGet(entity).ifPresent(cap -> {
						LivingEntity thrower = message.thrower != 0 ? (LivingEntity) Minecraft.getInstance().level.getEntity(message.thrower) : null;
						if (entity instanceof Player)
							cap.setThrown(message.thrown, thrower);
						cap.setThrowCooldown(message.throwCooldown);
					});
				}
//			});
		}
	}
}
