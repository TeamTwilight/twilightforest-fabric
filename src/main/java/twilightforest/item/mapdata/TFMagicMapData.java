package twilightforest.item.mapdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.*;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.network.MagicMapPacket;
import twilightforest.network.PacketDistributor;
import twilightforest.util.Codecs;

import java.util.*;

public class TFMagicMapData extends MapItemSavedData {
	private static final Map<String, TFMagicMapData> CLIENT_DATA = new HashMap<>();
	public final List<String> conqueredStructures = new ArrayList<>();

	public TFMagicMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
	}

	public static TFMagicMapData load(CompoundTag nbt, HolderLookup.Provider provider) {
		MapItemSavedData data = MapItemSavedData.load(nbt, provider);
		final boolean trackingPosition = !nbt.contains("trackingPosition", 1) || nbt.getBoolean("trackingPosition");
		final boolean unlimitedTracking = nbt.getBoolean("unlimitedTracking");
		final boolean locked = nbt.getBoolean("locked");
		TFMagicMapData tfdata = new TFMagicMapData(data.centerX, data.centerZ, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.frameMarkers.putAll(data.frameMarkers);

		if (nbt.contains("decorations", Tag.TAG_LIST)) {
			for (DecorationHolder decoration : DecorationHolder.CODEC.listOf()
				.parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt.get("decorations"))
				.resultOrPartial(error -> TwilightForestMod.LOGGER.warn("Failed to parse map decoration: '{}'", error))
				.orElse(List.of())) {
				MapDecoration mapdecoration1 = decoration.decoration();
				MapDecoration mapdecoration = tfdata.decorations.put(decoration.id(), mapdecoration1);
				if (!mapdecoration1.equals(mapdecoration)) {
					if (mapdecoration != null && mapdecoration.type().value().trackCount()) {
						tfdata.trackedDecorationCount--;
					}

					if (decoration.decoration().type().value().trackCount()) {
						tfdata.trackedDecorationCount++;
					}
					tfdata.setDecorationsDirty();
				}
			}
		}

		if (nbt.contains("conquered_structures", Tag.TAG_LIST)) {
			tfdata.conqueredStructures.clear();
			ListTag tag = nbt.getList("conquered_structures", Tag.TAG_STRING);
			tag.forEach(tag1 -> tfdata.conqueredStructures.add(tag1.getAsString()));
		}

		return tfdata;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		tag = super.save(tag, provider);

		List<DecorationHolder> holders = new ArrayList<>();
		this.decorations.forEach((s, decoration) -> {
			if (decoration.type().value().showOnItemFrame()) {
				holders.add(new DecorationHolder(s, decoration));
			}
		});
		tag.put("decorations", DecorationHolder.CODEC.listOf().encodeStart(NbtOps.INSTANCE, holders).getOrThrow());

		if (!this.conqueredStructures.isEmpty()) {
			ListTag conqueredTag = new ListTag();
			for (String structure : this.conqueredStructures) {
				conqueredTag.add(StringTag.valueOf(structure));
			}
			tag.put("conquered_structures", conqueredTag);
		}

		return tag;
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMagicMapData getMagicMapData(Level level, String name) {
		if (level instanceof ServerLevel serverLevel) {
			DimensionDataStorage storage = serverLevel.getServer().overworld().getDataStorage();
			TFMagicMapData data = (TFMagicMapData) storage.get(TFMagicMapData.factory(), name);
			if (data == null) {
				// Fall back to the vanilla key ("map_<id>") written by level.setMapData().
				// Both keys point at the same SavedData instance, and SavedData.save() only
				// writes when isDirty(), so only one of the two files survives a save and
				// the TF key can be missing after a restart. Loading through the TF factory
				// still yields a TFMagicMapData since the vanilla NBT layout is compatible.
				int id = Integer.parseInt(name.substring(MagicMapItem.STR_ID.length() + 1));
				MapItemSavedData vanilla = storage.get(TFMagicMapData.factory(), "map_" + id);
				if (vanilla instanceof TFMagicMapData tf) {
					data = tf;
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
	public static TFMagicMapData getClientMagicMapData(String name) {
		return CLIENT_DATA.get(name);
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMagicMapData(Level level, TFMagicMapData data, String id) {
		if (level instanceof ServerLevel serverLevel) serverLevel.getServer().overworld().getDataStorage().set(id, data);
		else CLIENT_DATA.put(id, data);
	}

	public static Factory<MapItemSavedData> factory() {
		return new Factory<>(() -> {
			throw new IllegalStateException("Should never create an empty map saved data");
		}, TFMagicMapData::load, DataFixTypes.SAVED_DATA_MAP_DATA);
	}

	private byte[] lastSyncedColors;
	private long lastFullSyncTick = Long.MIN_VALUE;

	@Nullable
	@Override
	public Packet<?> getUpdatePacket(MapId mapId, Player player) {
		// Always send a full map snapshot. The vanilla HoldingPlayer dirty flags are
		// runtime-only (not persisted), so a freshly re-joined client never receives
		// color patches and ends up with a blank, unusable map. Fabric sends this as a
		// custom payload and its handler rebuilds the TFMagicMapData, so we must include
		// the complete colors array every time data changes (plus a periodic fallback
		// resync in case nothing changed but a client is missing the data).
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
			PacketDistributor.sendToPlayer(serverPlayer, new MagicMapPacket(packet, this.conqueredStructures));
		}
		// Return null to prevent the vanilla ClientboundMapItemDataPacket from being sent.
		// If the vanilla packet arrives after our custom MagicMapPacket, it would overwrite
		// the TFMagicMapData in the renderer with a vanilla MapItemSavedData, causing the map
		// to not display properly after re-entering the game.
		return null;
	}

	public void addTFDecoration(Holder<MapDecorationType> decorationType, @Nullable LevelAccessor level, String id, double x, double z, double yRot, boolean conquered) {
		// In 1.21.1, MapItemSavedData.addDecoration was removed/renamed.
		// Add the decoration directly by computing map pixel coordinates.
		if (!this.decorations.containsKey(id)) {
			// Magic map uses 16 blocks per pixel regardless of vanilla scale.
			// Using 1 << this.scale (which is 1 for scale 0) would produce
			// coordinates that overflow the byte range, causing edge decorations
			// to disappear or appear at wrong positions.
			int blocksPerPixel = 16;
			float f = (float)(x - this.centerX) / (float)blocksPerPixel;
			float g = (float)(z - this.centerZ) / (float)blocksPerPixel;
			byte b = (byte) Mth.clamp((int)((double)(f * 2.0F) + 0.5D), -128, 127);
			byte c = (byte) Mth.clamp((int)((double)(g * 2.0F) + 0.5D), -128, 127);
			byte rotation = 8; // 180 degrees: (int)(180.0 * 16.0 / 360.0) = 8
			if (yRot != 180.0F) {
				rotation = (byte)((int)(yRot * 16.0D / 360.0D));
			}
			MapDecoration decoration = new MapDecoration(decorationType, b, c, rotation, Optional.empty());
			this.decorations.put(id, decoration);
			this.setDecorationsDirty();
			if (decorationType.value().trackCount()) {
				this.trackedDecorationCount++;
			}
		}
		MapDecoration deco = this.decorations.get(id);
		if (deco != null) {
			String conqueredID = MagicMapItem.makeName(decorationType, deco.x(), deco.y());
			if (conquered && !this.conqueredStructures.contains(conqueredID)) {
				this.conqueredStructures.add(conqueredID);
			} else if (!conquered) {
				this.conqueredStructures.remove(conqueredID);
			}
		}
	}

	public record DecorationHolder(String id, MapDecoration decoration) {
		public static final Codec<DecorationHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("id").forGetter(DecorationHolder::id),
			Codecs.DECORATION_CODEC.fieldOf("decoration").forGetter(DecorationHolder::decoration)
		).apply(instance, DecorationHolder::new));
	}
}
