package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.TFMain;
import twilightforest.init.TFDataAttachments;

public record UpdateThrownPacket(int entityID, boolean thrown, int thrower, int throwCooldown) implements CustomPacketPayload {

	public static final Type<UpdateThrownPacket> TYPE = new Type<>(TFMain.prefix("update_thrown_attachment"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateThrownPacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateThrownPacket::write, UpdateThrownPacket::new);

	public UpdateThrownPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean(), buf.readInt(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
		buf.writeBoolean(this.thrown());
		buf.writeInt(this.thrower());
		buf.writeInt(this.throwCooldown());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateThrownPacket message, ClientPlayNetworking.Context ctx) {
		ClientLevel level = ctx.client().level;
		Entity entity = level.getEntity(message.entityID());
		if (entity instanceof Player player) {
			var attachment = player.getAttached(TFDataAttachments.YETI_THROWING);
			LivingEntity thrower = message.thrower() != 0 ? (LivingEntity) level.getEntity(message.thrower()) : null;
			attachment.setThrown(player, message.thrown(), thrower);
			attachment.setThrowCooldown(player, message.throwCooldown());
		}
	}
}