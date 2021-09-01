package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

import java.util.function.Supplier;

public class StructureProtectionPacket extends ISimplePacket {

	private final BoundingBox sbb;

	public StructureProtectionPacket(BoundingBox sbb) {
		this.sbb = sbb;
	}

	public StructureProtectionPacket(FriendlyByteBuf buf) {
		sbb = new BoundingBox(
				buf.readInt(), buf.readInt(), buf.readInt(),
				buf.readInt(), buf.readInt(), buf.readInt()
		);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(sbb.minX());
		buf.writeInt(sbb.minY());
		buf.writeInt(sbb.minZ());
		buf.writeInt(sbb.maxX());
		buf.writeInt(sbb.maxY());
		buf.writeInt(sbb.maxZ());
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {
		public static boolean onMessage(StructureProtectionPacket message) {
			Minecraft.getInstance().execute(() -> {
				DimensionSpecialEffects info = DimensionSpecialEffects.EFFECTS.get(TwilightForestMod.prefix("renderer"));

				// add weather box if needed
				if (info instanceof TwilightForestRenderInfo twilightForestRenderInfo) {
					TFWeatherRenderer weatherRenderer = twilightForestRenderInfo.getWeatherRenderHandler();

					if (weatherRenderer instanceof TFWeatherRenderer) {
						weatherRenderer.setProtectedBox(message.sbb);
					}
				}
			});

			return true;
		}
	}
}
