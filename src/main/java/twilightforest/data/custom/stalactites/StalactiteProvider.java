package twilightforest.data.custom.stalactites;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class StalactiteProvider implements DataProvider {

	private final DataGenerator generator;
	private final String modid;
	private final DataGenerator.PathProvider entryPath;
	protected final Map<Pair<ResourceLocation, Stalactite>, Stalactite.HollowHillType> builder = Maps.newLinkedHashMap();

	public StalactiteProvider(DataGenerator generator, String modid) {
		this.generator = generator;
		this.modid = modid;
		this.entryPath = generator.createPathProvider(DataGenerator.Target.DATA_PACK, "stalactites/entries");
	}

	@Override
	public void run(CachedOutput output) throws IOException {
		Map<ResourceLocation, Stalactite> map = Maps.newHashMap();

		Map<ResourceLocation, Stalactite> smallHillEntries = Maps.newHashMap();
		Map<ResourceLocation, Stalactite> medHillEntries = Maps.newHashMap();
		Map<ResourceLocation, Stalactite> largeHillEntries = Maps.newHashMap();

		this.builder.clear();
		this.createStalactites();
		this.builder.forEach((stalactiteMap, type) -> {
			if (type == Stalactite.HollowHillType.LARGE) {
				largeHillEntries.put(stalactiteMap.getFirst(), stalactiteMap.getSecond());
			} else if (type == Stalactite.HollowHillType.MEDIUM) {
				medHillEntries.put(stalactiteMap.getFirst(), stalactiteMap.getSecond());
			} else {
				smallHillEntries.put(stalactiteMap.getFirst(), stalactiteMap.getSecond());
			}
			map.put(stalactiteMap.getFirst(), stalactiteMap.getSecond());
		});

		map.forEach((resourceLocation, stalactite) -> {
			Path path = this.entryPath.json(resourceLocation);

			try {
				DataProvider.saveStable(output, StalactiteReloadListener.serialize(stalactite), path);
			} catch (IOException ioexception) {
				TwilightForestMod.LOGGER.error("Couldn't save stalactite entry {}", path, ioexception);
			}
		});

		Gson hillGson = new GsonBuilder().setPrettyPrinting().create();

		for (Stalactite.HollowHillType type : Stalactite.HollowHillType.values()) {
			Path hillPath = this.generator.getOutputFolder().resolve("data/twilightforest/stalactites/" + type.name().toLowerCase(Locale.ROOT) + "_hollow_hill.json");
			Map<ResourceLocation, Stalactite> mapToUse = type == Stalactite.HollowHillType.LARGE ? largeHillEntries : type == Stalactite.HollowHillType.MEDIUM ? medHillEntries : smallHillEntries;

			JsonObject object = new JsonObject();
			object.addProperty("replace", false);
			object.add("stalactites", hillGson.toJsonTree(mapToUse.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toList())));

			DataProvider.saveStable(output, object, hillPath);
		}
	}

	protected abstract void createStalactites();

	/**
	 * Add a new Stalactite entry for hollow hills! This will allow your ores (or even blocks, I don't care what you do) to show up in hollow hills.
	 *
	 * @param stalactite the stalactite to make. Takes in a block to use, the size variation (a lower number typically means longer stalactites), a max length, and the weight of the entry.
	 * @param type       the type of hill this stalactite appears in.
	 *                   <br>
	 *                   Do note that this entry is the lowest tier it appears in, meaning if you add a stalactite to a small hill it will show up in all 3 hills.
	 *                   <br>
	 *                   Pick your weights wisely! All weights from lower tiers are transferred up, so if you want to see your spike more often in a given hill, give it a high weight!
	 */

	protected void makeStalactite(Stalactite stalactite, Stalactite.HollowHillType type) {
		this.builder.putIfAbsent(Pair.of(new ResourceLocation(this.modid, ForgeRegistries.BLOCKS.getKey(stalactite.ore()).getPath() + "_stalactite"), stalactite), type);
	}

	@Override
	public String getName() {
		return this.modid + " Hollow Hill Stalactites";
	}
}
