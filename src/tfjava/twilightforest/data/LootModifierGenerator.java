package twilightforest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootModifierGenerator implements DataProvider {
	private final PackOutput output;

	public LootModifierGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		futures.add(this.save(output, "fiery_pick_smelting", this.fieryPickSmelting()));
		futures.add(this.save(output, "giant_pick_grouping", this.giantPickGrouping()));
		futures.add(this.saveGlobalList(output));
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Twilight Forest Fabric loot modifiers";
	}

	private CompletableFuture<?> save(CachedOutput output, String name, JsonObject json) {
		Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
			.resolve(TwilightForestMod.ID)
			.resolve("loot_modifiers")
			.resolve(name + ".json");
		return DataProvider.saveStable(output, json, path);
	}

	private CompletableFuture<?> saveGlobalList(CachedOutput output) {
		JsonObject json = new JsonObject();
		JsonArray entries = new JsonArray();
		entries.add(TwilightForestMod.prefix("fiery_pick_smelting").toString());
		entries.add(TwilightForestMod.prefix("giant_pick_grouping").toString());
		json.add("entries", entries);
		json.addProperty("replace", false);

		Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
			.resolve("neoforge")
			.resolve("loot_modifiers")
			.resolve("global_loot_modifiers.json");
		return DataProvider.saveStable(output, json, path);
	}

	private JsonObject fieryPickSmelting() {
		JsonObject json = new JsonObject();
		json.addProperty("type", TwilightForestMod.prefix("fiery_pick_smelting").toString());
		json.add("conditions", this.conditions(
			this.matchTool(BuiltInIds.item(TFItems.FIERY_PICKAXE.get()))
		));
		return json;
	}

	private JsonObject giantPickGrouping() {
		JsonObject json = new JsonObject();
		json.addProperty("type", TwilightForestMod.prefix("giant_block_grouping").toString());
		json.add("conditions", this.conditions(this.giantPickUsed()));
		return json;
	}

	private JsonArray conditions(JsonObject... conditions) {
		JsonArray array = new JsonArray();
		for (JsonObject condition : conditions) {
			array.add(condition);
		}
		return array;
	}

	private JsonObject matchTool(ResourceLocation item) {
		JsonObject condition = new JsonObject();
		condition.addProperty("condition", "minecraft:match_tool");

		JsonObject predicate = new JsonObject();
		JsonArray items = new JsonArray();
		items.add(item.toString());
		predicate.add("items", items);

		condition.add("predicate", predicate);
		return condition;
	}

	private JsonObject giantPickUsed() {
		JsonObject condition = new JsonObject();
		condition.addProperty("condition", TwilightForestMod.prefix("giant_pick_used").toString());
		condition.addProperty("entity", "this");
		return condition;
	}

	private static final class BuiltInIds {
		private BuiltInIds() {
		}

		private static ResourceLocation item(net.minecraft.world.item.Item item) {
			ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
			if (id == null) {
				throw new IllegalStateException("Unknown item " + item);
			}
			return id;
		}
	}
}
