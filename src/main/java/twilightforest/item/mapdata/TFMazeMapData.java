package twilightforest.item.mapdata;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructures;
import twilightforest.item.MazeMapItem;
import twilightforest.network.MazeMapPacket;
import twilightforest.network.PacketDistributor;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TFMazeMapData extends MapItemSavedData {
	private static final Map<String, TFMazeMapData> CLIENT_DATA = new HashMap<>();

	public int yCenter;
	public boolean ore;

	public TFMazeMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
	}

	public static TFMazeMapData load(CompoundTag nbt, HolderLookup.Provider provider) {
		MapItemSavedData data = MapItemSavedData.load(nbt, provider);
		final boolean trackingPosition = !nbt.contains("trackingPosition", 1) || nbt.getBoolean("trackingPosition");
		final boolean unlimitedTracking = nbt.getBoolean("unlimitedTracking");
		final boolean locked = nbt.getBoolean("locked");
		TFMazeMapData tfdata = new TFMazeMapData(data.centerX, data.centerZ, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.decorations.putAll(data.decorations);
		tfdata.frameMarkers.putAll(data.frameMarkers);
		tfdata.trackedDecorationCount = data.trackedDecorationCount;

		tfdata.yCenter = nbt.getInt("yCenter");
		tfdata.ore = nbt.getBoolean("mapOres");

		return tfdata;
	}

	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
		CompoundTag ret = super.save(nbt, provider);
		ret.putInt("yCenter", this.yCenter);
		ret.putBoolean("mapOres", this.ore);
		return ret;
	}

	public void calculateMapCenter(Level world, int x, int y, int z) {
		this.yCenter = y;

		// when we are in a labyrinth, snap to the LABYRINTH
		if (world instanceof ServerLevel level) {
			if (LegacyLandmarkPlacements.pickLandmarkForChunk(x >> 4, z >> 4, level) == TFStructures.LABYRINTH) {
				BlockPos mc = LegacyLandmarkPlacements.getNearestCenterXZ(x >> 4, z >> 4);
				this.centerX = mc.getX();
				this.centerZ = mc.getZ();
			}
		}
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMazeMapData getMazeMapData(Level level, String name) {
		if (level instanceof ServerLevel serverLevel) {
			DimensionDataStorage storage = serverLevel.getServer().overworld().getDataStorage();
			TFMazeMapData data = (TFMazeMapData) storage.get(TFMazeMapData.factory(), name);
			if (data == null) {
				// Fall back to the vanilla key ("map_<id>") written by level.setMapData().
				// Both keys point at the same SavedData instance, and SavedData.save() only
				// writes when isDirty(), so only one of the two files survives a save and
				// the TF key can be missing after a restart.
				int id = Integer.parseInt(name.substring(MazeMapItem.STR_ID.length() + 1));
				MapItemSavedData vanilla = storage.get(TFMazeMapData.factory(), "map_" + id);
				if (vanilla instanceof TFMazeMapData maze) {
					data = maze;
					storage.set(name, data); // migrate to the TF key so future reads hit directly
					data.setDirty();
				}
			}
			return data;
		}
		return CLIENT_DATA.get(name);
	}

	// Like the method above, but if we know we're on client
	@Nullable
	public static TFMazeMapData getClientMagicMapData(String name) {
		return CLIENT_DATA.get(name);
	}

	public static Factory<MapItemSavedData> factory() {
		return new Factory<>(() -> {
			throw new IllegalStateException("Should never create an empty map saved data");
		}, TFMazeMapData::load, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMazeMapData(Level level, TFMazeMapData data, String id) {
		if (level.isClientSide()) CLIENT_DATA.put(id, data);
		else ((ServerLevel) level).getServer().overworld().getDataStorage().set(id, data);
	}

	private byte[] lastSyncedColors;
	private long lastFullSyncTick = Long.MIN_VALUE;

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		// Always send a full map snapshot. The vanilla HoldingPlayer dirty flags are
		// runtime-only (not persisted), so a freshly re-joined client never receives
		// color patches and ends up with a blank, unusable map.
		if (player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel serverLevel) {
			long tick = serverLevel.getGameTime();
			boolean colorsChanged = !Arrays.equals(this.lastSyncedColors, this.colors);
			if (!colorsChanged && tick - this.lastFullSyncTick < 20) {
				return null; // throttle fallback resyncs to once per second
			}
			this.lastFullSyncTick = tick;
			if (colorsChanged) {
				this.lastSyncedColors = Arrays.copyOf(this.colors, this.colors.length);
			}

			MapPatch fullPatch = new MapPatch(0, 0, 128, 128, this.colors);
			ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket(mapId, this.scale, this.locked, new ArrayList<>(this.decorations.values()), fullPatch);
			PacketDistributor.sendToPlayer(serverPlayer, new MazeMapPacket(packet, this.ore, this.yCenter, this.centerX, this.centerZ));
		}
		// Return null to prevent the vanilla packet from overwriting our custom map data
		return null;
	}
}
