package twilightforest.data.custom.stalactites;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class StalactiteProvider implements DataProvider {

	private final PackOutput generator;
	private final String modid;
	private final PackOutput.PathProvider entryPath;
	protected final Map<Pair<ResourceLocation, Stalactite>, Stalactite.HollowHillType> builder = Maps.newLinkedHashMap();

	public StalactiteProvider(PackOutput generator, String modid) {
		this.generator = generator;
		this.modid = modid;
		this.entryPath = generator.createPathProvider(PackOutput.Target.DATA_PACK, "stalactites/entries");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		Map<ResourceLocation, Stalactite> map = Maps.newHashMap();

		Map<ResourceLocation, Stalactite> smallHillEntries = Maps.newHashMap();
		Map<ResourceLocation, Stalactite> medHillEntries = Maps.newHashMap();
		Map<ResourceLocation, Stalactite> largeHillEntries = Maps.newHashMap();

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

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
			futuresBuilder.add(DataProvider.saveStable(output, StalactiteReloadListener.serialize(stalactite), path));

		});

		Gson hillGson = new GsonBuilder().setPrettyPrinting().create();

		for (Stalactite.HollowHillType type : Stalactite.HollowHillType.values()) {
			Path hillPath = this.generator.getOutputFolder().resolve("data/twilightforest/stalactites/" + type.name().toLowerCase(Locale.ROOT) + "_hollow_hill.json");
			Map<ResourceLocation, Stalactite> mapToUse = type == Stalactite.HollowHillType.LARGE ? largeHillEntries : type == Stalactite.HollowHillType.MEDIUM ? medHillEntries : smallHillEntries;

			JsonObject object = new JsonObject();
			object.addProperty("replace", false);
			object.add("stalactites", hillGson.toJsonTree(mapToUse.keySet().stream().map(ResourceLocation::toString).sorted().collect(Collectors.toList())));

			futuresBuilder.add(DataProvider.saveStable(output, object, hillPath));
		}
		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	protected abstract void createStalactites();

	/**
	 * Add a new Stalactite entry for hollow hills! This will allow your ores (or even blocks, I don't care what you do) to show up in hollow hills.
	 *
	 * @param stalactite the stalactite to make. Takes in list of blocks to use + their weights, the size variation (a lower number typically means longer stalactites), a max length, and the weight of the entry.
	 * @param type       the type of hill this stalactite appears in.
	 *                   <br>
	 *                   Do note that this entry is the lowest tier it appears in, meaning if you add a stalactite to a small hill it will show up in all 3 hills.
	 *                   <br>
	 *                   Pick your weights wisely! All weights from lower tiers are transferred up, so if you want to see your spike more often in a given hill, give it a high weight!
	 */
	protected void makeStalactite(Stalactite stalactite, Stalactite.HollowHillType type) {
		var nameBuilder = new StringJoiner("_", "", "_stalactite");

		for (ResourceLocation entry : stalactite.ores().keySet().stream().map(BuiltInRegistries.BLOCK::getKey).sorted().toList())
			nameBuilder.add(entry.getPath());

		this.builder.putIfAbsent(Pair.of(new ResourceLocation(this.modid, nameBuilder.toString()), stalactite), type);
	}

	@Override
	public String getName() {
		return this.modid + " Hollow Hill Stalactites";
	}
}
