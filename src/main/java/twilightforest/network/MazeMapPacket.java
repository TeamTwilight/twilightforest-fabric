package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import twilightforest.TFMazeMapData;
import twilightforest.item.MazeMapItem;

import java.util.concurrent.Executor;

/**
 * Vanilla's SPacketMaps handler looks for and loads the vanilla MapData instances.
 * We rewrap the packet here in order to load our own MapData instances properly.
 */
public class MazeMapPacket implements S2CPacket {

	private final ClientboundMapItemDataPacket inner;

	public MazeMapPacket(ClientboundMapItemDataPacket inner) {
		this.inner = inner;
	}

	public MazeMapPacket(FriendlyByteBuf buf) {
		this.inner = new ClientboundMapItemDataPacket(buf);
	}

	public void encode(FriendlyByteBuf buf) {
		this.inner.write(buf);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {
		public static boolean onMessage(MazeMapPacket message, Executor ctx) {
			ctx.execute(() -> {
				// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
				MapRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
				String s = MazeMapItem.getMapName(message.inner.getMapId());
				TFMazeMapData mapdata = TFMazeMapData.getMazeMapData(Minecraft.getInstance().level, s);
				if (mapdata == null) {
					mapdata = new TFMazeMapData(0, 0, message.inner.getScale(), false, false, message.inner.isLocked(), Minecraft.getInstance().level.dimension());
					TFMazeMapData.registerMazeMapData(Minecraft.getInstance().level, mapdata, s);
				}

				message.inner.applyToMap(mapdata);
				mapitemrenderer.update(message.inner.getMapId(), mapdata);
			});
			return true;
		}
	}
}
