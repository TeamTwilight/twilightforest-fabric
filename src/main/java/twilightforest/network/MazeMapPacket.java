package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import twilightforest.TFMain;
import twilightforest.item.mapdata.MapDataManager;
import twilightforest.item.mapdata.TFMazeMapData;

import java.util.stream.StreamSupport;

// Rewraps vanilla ClientboundMapItemDataPacket to properly add our own data
public record MazeMapPacket(ClientboundMapItemDataPacket inner, boolean ore, int yCenter) implements CustomPacketPayload {

	public static final Type<MazeMapPacket> TYPE = new Type<>(TFMain.prefix("maze_map"));

	public static final StreamCodec<RegistryFriendlyByteBuf, MazeMapPacket> STREAM_CODEC = StreamCodec.composite(
		ClientboundMapItemDataPacket.STREAM_CODEC, MazeMapPacket::inner,
		ByteBufCodecs.BOOL, MazeMapPacket::ore,
		ByteBufCodecs.INT, MazeMapPacket::yCenter,
		MazeMapPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(MazeMapPacket message, ClientPlayNetworking.Context ctx) {
		ClientLevel clientLevel = ctx.client().level;
		MapId mapId = message.inner.mapId();
		TFMazeMapData mapdata = MapDataManager.getClientMazeMapData(mapId);
		if (mapdata == null) {
			mapdata = new TFMazeMapData(
				0, 0,
				message.inner().scale(),
				false,
				false,
				message.inner().locked(),
				clientLevel.dimension()
			);
			MapDataManager.saveClientMazeMapData(mapId, mapdata);
		}

		mapdata.ore = message.ore();
		mapdata.yCenter = message.yCenter();
		message.inner().applyToMap(mapdata);

		MapItemSavedData saved = clientLevel.getMapData(message.inner().mapId());

		if (saved != null) {
			saved.addClientSideDecorations(
				StreamSupport.stream(mapdata.getDecorations().spliterator(), false).toList()
			);
		}
	}
}