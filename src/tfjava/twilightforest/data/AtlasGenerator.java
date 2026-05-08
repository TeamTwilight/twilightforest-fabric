package twilightforest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.MagicPaintingVariant;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AtlasGenerator implements DataProvider {
	public static final Map<ResourceLocation, MagicPaintingVariant> MAGIC_PAINTING_HELPER = new HashMap<>();
	private static final ResourceLocation SHIELD_PATTERNS_ATLAS = ResourceLocation.withDefaultNamespace("shield_patterns");
	private static final String MAGIC_PAINTING_PATH = "magic_paintings";
	private static final ResourceLocation MAGIC_PAINTING_ATLAS = ResourceLocation.withDefaultNamespace(MAGIC_PAINTING_PATH);
	private static final ResourceLocation MAGIC_PAINTING_BACK = TwilightForestMod.prefix(MAGIC_PAINTING_PATH + "/back");

	private final PackOutput.PathProvider pathProvider;

	public AtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, Object helper) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		Map<ResourceLocation, List<ResourceLocation>> atlases = new HashMap<>();
		this.gather(atlases);

		List<CompletableFuture<?>> futures = new ArrayList<>();
		atlases.forEach((atlas, sources) -> {
			JsonObject json = new JsonObject();
			JsonArray array = new JsonArray();
			sources.forEach(source -> {
				JsonObject entry = new JsonObject();
				entry.addProperty("type", "minecraft:single");
				entry.addProperty("resource", source.toString());
				array.add(entry);
			});
			json.add("sources", array);
			Path path = this.pathProvider.json(atlas);
			futures.add(DataProvider.saveStable(output, json, path));
		});
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Twilight Forest atlas sources";
	}

	protected void gather(Map<ResourceLocation, List<ResourceLocation>> atlases) {
		this.add(atlases, SHIELD_PATTERNS_ATLAS, TwilightForestMod.prefix("entity/knightmetal_shield"));
		this.add(atlases, MAGIC_PAINTING_ATLAS, MAGIC_PAINTING_BACK);

		MAGIC_PAINTING_HELPER.forEach((location, parallaxVariant) -> {
			ResourceLocation prefixed = location.withPrefix(MAGIC_PAINTING_PATH + "/");
			for (MagicPaintingVariant.Layer layer : parallaxVariant.layers()) {
				this.add(atlases, MAGIC_PAINTING_ATLAS, prefixed.withSuffix("/" + layer.path()));
			}
		});
	}

	private void add(Map<ResourceLocation, List<ResourceLocation>> atlases, ResourceLocation atlas, ResourceLocation source) {
		atlases.computeIfAbsent(atlas, key -> new ArrayList<>()).add(source);
	}
}
