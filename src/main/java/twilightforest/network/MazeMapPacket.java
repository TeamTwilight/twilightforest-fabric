package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.entity.player.Player;
import twilightforest.TFMazeMapData;
import twilightforest.item.MazeMapItem;

/**
 * Vanilla's SPacketMaps handler looks for and loads the vanilla MapData instances.
 * We rewrap the packet here in order to load our own MapData instances properly.
 */
public class MazeMapPacket extends ISimplePacket {

	private final ClientboundMapItemDataPacket inner;

	public MazeMapPacket(ClientboundMapItemDataPacket inner) {
		this.inner = inner;
	}

	public MazeMapPacket(FriendlyByteBuf buf) {
		inner = new ClientboundMapItemDataPacket(buf);
	}

	public void encode(FriendlyByteBuf buf) {
		inner.write(buf);
	}

	@Override
	public void onMessage(Player playerEntity) {
		Minecraft.getInstance().execute(() -> {
			Handler.onMessage(this);
		});
	}

	public static class Handler {
		public static boolean onMessage(MazeMapPacket message) {
			// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
			MapRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
			String s = MazeMapItem.getMapName(message.inner.getMapId());
			TFMazeMapData mapdata = TFMazeMapData.getMazeMapData(Minecraft.getInstance().level, s);
			if (mapdata == null) {
				mapdata = new TFMazeMapData(0, 0, message.inner.getScale(), false, false, message.inner.isLocked(), Minecraft.getInstance().level.dimension());
				//if (mapitemrenderer.getMapInstanceIfExists(s) != null) {
				//	MapItemSavedData mapdata1 = mapitemrenderer.getData(mapitemrenderer.getMapInstanceIfExists(s));
				//	if (mapdata1 instanceof TFMazeMapData) {
				//		mapdata = (TFMazeMapData) mapdata1;
				//	}
				//}

				TFMazeMapData.registerMazeMapData(Minecraft.getInstance().level, mapdata, s);
			}

			message.inner.applyToMap(mapdata);
			mapitemrenderer.update(message.inner.getMapId(), mapdata);
			return true;
		}
	}
}
