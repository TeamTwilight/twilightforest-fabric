package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.client.MovingCicadaSoundInstance;

import java.util.concurrent.Executor;

public class CreateMovingCicadaSoundPacket implements S2CPacket {

	private final int entityID;

	public CreateMovingCicadaSoundPacket(int id) {
		this.entityID = id;
	}

	public CreateMovingCicadaSoundPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static boolean onMessage(CreateMovingCicadaSoundPacket message, Executor ctx) {
			ctx.execute(new Runnable() {
				@Override
				public void run() {
					Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
					if (entity instanceof LivingEntity living) {
						Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingCicadaSoundInstance(living));
					}
				}
			});

			return true;
		}
	}
}
