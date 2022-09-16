package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import twilightforest.client.MovingCicadaSoundInstance;

import java.util.function.Supplier;

public class CreateMovingCicadaSoundPacket {

	private final int entityID;

	public CreateMovingCicadaSoundPacket(int id) {
		this.entityID = id;
	}

	public CreateMovingCicadaSoundPacket(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID);
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static boolean onMessage(CreateMovingCicadaSoundPacket message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
					if (entity instanceof LivingEntity living) {
						Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingCicadaSoundInstance(living));
					}
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
