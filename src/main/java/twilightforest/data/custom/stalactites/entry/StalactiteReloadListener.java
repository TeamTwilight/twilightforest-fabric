package twilightforest.data.custom.stalactites.entry;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.BlockSpikeFeature;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StalactiteReloadListener extends SimpleJsonResourceReloadListener {

	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Stalactite.class, new Stalactite.Serializer()).create();

	private final Map<ResourceLocation, Stalactite> smallStalactites = new HashMap<>();
	private final Map<ResourceLocation, Stalactite> mediumStalactites = new HashMap<>();
	private final Map<ResourceLocation, Stalactite> largeStalactites = new HashMap<>();

	public StalactiteReloadListener() {
		super(GSON, "stalactites");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {

		for (Stalactite.HollowHillType type : Stalactite.HollowHillType.values()) {
			ResourceLocation resourcelocation = new ResourceLocation(TwilightForestMod.ID, "stalactites/" + type.name().toLowerCase(Locale.ROOT) + "_hollow_hill.json");
			List<ResourceLocation> finalLocations = new ArrayList<>();
			Map<ResourceLocation, Stalactite> mapToUse =
					type == Stalactite.HollowHillType.LARGE ? this.largeStalactites :
							type == Stalactite.HollowHillType.MEDIUM ? this.mediumStalactites :
									this.smallStalactites;

			for (Resource resource : resourceManager.getResourceStack(resourcelocation)) {
				try (InputStream inputstream = resource.open();
					 Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))) {
					JsonObject jsonobject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
					boolean replace = jsonobject.get("replace").getAsBoolean();
					if (replace)
						finalLocations.clear();
					JsonArray entryList = jsonobject.get("stalactites").getAsJsonArray();
					for (JsonElement entry : entryList) {
						ResourceLocation loc = new ResourceLocation(entry.getAsString().replace(":", ":entries/"));
						finalLocations.remove(loc);
						finalLocations.add(loc);
					}
				} catch (RuntimeException | IOException ioexception) {
					TwilightForestMod.LOGGER.error("Couldn't read Hollow Hill list {} in data pack {}", resourcelocation, resource.sourcePackId(), ioexception);
				}

				if (finalLocations.isEmpty()) {
					TwilightForestMod.LOGGER.warn("Hollow Hill list {} was empty, adding default stone entry so things don't break!", resourcelocation);
					mapToUse.put(new ResourceLocation(TwilightForestMod.ID, "default_fallback_stone"), BlockSpikeFeature.STONE_STALACTITE);
					break;
				}

				for (ResourceLocation location : finalLocations) {
					JsonElement json = object.get(location);
					mapToUse.put(location, GSON.fromJson(json, Stalactite.class));
				}
			}
		}

		TwilightForestMod.LOGGER.info("Loaded {} Stalactite configs!", this.getLargeStalactites().size());
	}

	public static JsonElement serialize(Stalactite stalactite) {
		return GSON.toJsonTree(stalactite);
	}

	public Map<ResourceLocation, Stalactite> getLargeStalactites() {
		Map<ResourceLocation, Stalactite> finalMap = this.largeStalactites;
		finalMap.putAll(this.getMediumStalactites());
		return finalMap;
	}

	public Map<ResourceLocation, Stalactite> getMediumStalactites() {
		Map<ResourceLocation, Stalactite> finalMap = this.mediumStalactites;
		finalMap.putAll(this.getSmallStalactites());
		return finalMap;
	}

	public Map<ResourceLocation, Stalactite> getSmallStalactites() {
		return this.smallStalactites;
	}

	public Stalactite getRandomStalactiteFromList(RandomSource random, Map<ResourceLocation, Stalactite> map) {
		Stalactite[] list = map.values().toArray(new Stalactite[0]);
		return list[random.nextInt(list.length)];
	}
}
