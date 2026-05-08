package twilightforest.data;

import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataMaps;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataMapGenerator implements DataProvider {
	private final PackOutput output;

	public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		futures.add(this.save(output, "crumble_horn", this.crumbleHorn()));
		futures.add(this.save(output, "transformation_powder", this.transformationPowder()));
		futures.add(this.save(output, "ominous_fire", this.ominousFire()));
		futures.add(this.save(output, "magic_map_biome_color", this.magicMapBiomeColor()));
		futures.add(this.save(output, "ore_map_ore_color", this.oreMapOreColor()));
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Twilight Forest Fabric data maps";
	}

	private CompletableFuture<?> save(CachedOutput output, String name, JsonObject json) {
		Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
			.resolve(TwilightForestMod.ID)
			.resolve("twilight")
			.resolve("datamaps")
			.resolve(name + ".json");
		return DataProvider.saveStable(output, json, path);
	}

	private JsonObject crumbleHorn() {
		JsonObject values = new JsonObject();
		TFDataMaps.crumbleHornEntries().entrySet().stream()
			.sorted((left, right) -> blockId(left.getKey()).toString().compareTo(blockId(right.getKey()).toString()))
			.forEach(entry -> {
				CrumbledBlock value = entry.getValue();
				JsonObject json = new JsonObject();
				json.addProperty("crumble_to", blockId(value.result()).toString());
				json.addProperty("crumble_chance", value.chanceToCrumble());
				values.add(blockId(entry.getKey()).toString(), json);
			});
		return wrapped(values);
	}

	private JsonObject transformationPowder() {
		JsonObject values = new JsonObject();
		TFDataMaps.transformationPowderEntries().entrySet().stream()
			.sorted((left, right) -> entityId(left.getKey()).toString().compareTo(entityId(right.getKey()).toString()))
			.forEach(entry -> values.add(entityId(entry.getKey()).toString(), entityTransformation(entry.getValue())));
		return wrapped(values);
	}

	private JsonObject ominousFire() {
		JsonObject values = new JsonObject();
		TFDataMaps.ominousFireEntries().entrySet().stream()
			.sorted((left, right) -> entityId(left.getKey()).toString().compareTo(entityId(right.getKey()).toString()))
			.forEach(entry -> values.add(entityId(entry.getKey()).toString(), entityTransformation(entry.getValue())));
		return wrapped(values);
	}

	private JsonObject magicMapBiomeColor() {
		JsonObject values = new JsonObject();
		TFDataMaps.magicMapBiomeColorEntries().entrySet().stream()
			.sorted((left, right) -> left.getKey().location().toString().compareTo(right.getKey().location().toString()))
			.forEach(entry -> values.add(entry.getKey().location().toString(), mapColor(entry.getValue())));
		return wrapped(values);
	}

	private JsonObject oreMapOreColor() {
		JsonObject values = new JsonObject();
		values.add("#" + BlockTags.COPPER_ORES.location(), mapColor(new OreMapOreColor(MapColor.COLOR_ORANGE)));
		values.add("#" + BlockTags.COAL_ORES.location(), mapColor(new OreMapOreColor(MapColor.TERRACOTTA_BLACK)));
		values.add("#" + BlockTags.IRON_ORES.location(), mapColor(new OreMapOreColor(MapColor.RAW_IRON)));
		values.add("#" + BlockTags.LAPIS_ORES.location(), mapColor(new OreMapOreColor(MapColor.LAPIS)));
		values.add("#" + BlockTags.GOLD_ORES.location(), mapColor(new OreMapOreColor(MapColor.GOLD)));
		values.add("#" + BlockTags.REDSTONE_ORES.location(), mapColor(new OreMapOreColor(MapColor.TERRACOTTA_RED)));
		values.add("#" + BlockTags.DIAMOND_ORES.location(), mapColor(new OreMapOreColor(MapColor.DIAMOND)));
		values.add("#" + BlockTags.EMERALD_ORES.location(), mapColor(new OreMapOreColor(MapColor.EMERALD)));
		values.add(blockId(Blocks.ANCIENT_DEBRIS).toString(), mapColor(new OreMapOreColor(MapColor.TERRACOTTA_BROWN)));
		return wrapped(values);
	}

	private static JsonObject wrapped(JsonObject values) {
		JsonObject root = new JsonObject();
		root.add("values", values);
		return root;
	}

	private static JsonObject entityTransformation(EntityTransformation value) {
		JsonObject json = new JsonObject();
		json.addProperty("transform_to", entityId(value.result()).toString());
		return json;
	}

	private static JsonObject mapColor(MagicMapBiomeColor value) {
		JsonObject json = mapColor(new OreMapOreColor(value.color()));
		json.addProperty("brightness", value.brightness());
		return json;
	}

	private static JsonObject mapColor(OreMapOreColor value) {
		JsonObject json = new JsonObject();
		json.addProperty("id", value.color().id);
		json.addProperty("color", value.color().col);
		return json;
	}

	private static ResourceLocation blockId(Block block) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		if (id == null) {
			throw new IllegalStateException("Unknown block " + block);
		}
		return id;
	}

	private static ResourceLocation entityId(EntityType<?> entityType) {
		ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
		if (id == null) {
			throw new IllegalStateException("Unknown entity type " + entityType);
		}
		return id;
	}
}
