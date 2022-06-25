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
import twilightforest.capabilities.shield.IShieldCapability;

import java.util.concurrent.Executor;

public class UpdateShieldPacket implements S2CPacket {

	private final int entityID;
	private final int temporaryShields;
	private final int permanentShields;

	public UpdateShieldPacket(int id, IShieldCapability cap) {
		this.entityID = id;
		this.temporaryShields = cap.temporaryShieldsLeft();
		this.permanentShields = cap.permanentShieldsLeft();
	}

	public UpdateShieldPacket(Entity entity, IShieldCapability cap) {
		this(entity.getId(), cap);
	}

	public UpdateShieldPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
		this.temporaryShields = buf.readInt();
		this.permanentShields = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
		buf.writeInt(this.temporaryShields);
		buf.writeInt(this.permanentShields);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		public static boolean onMessage(UpdateShieldPacket message, Executor ctx) {
			ctx.execute(() -> {
				Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
				if (entity instanceof LivingEntity) {
					CapabilityList.SHIELDS.maybeGet(entity).ifPresent(cap -> {
						cap.setShields(message.temporaryShields, true);
						cap.setShields(message.permanentShields, false);
					});
				}
			});

			return true;
		}
	}
}
