package twilightforest.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class LootGenerator extends LootTableProvider {
	public LootGenerator(DataGenerator generator) {
		super(generator);
	}

	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(
					Pair.of(BlockLootTables::new, LootContextParamSets.BLOCK),
					Pair.of(ChestLootTables::new, LootContextParamSets.CHEST),
					Pair.of(EntityLootTables::new, LootContextParamSets.ENTITY)
	);

//	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		// [VanillaCopy] super, but remove call that checks that all vanilla tables are accounted for, because we aren't vanilla.
		// Except validation issues occur when attempting to generate loot tables from other loot tables (see: EntityLootTables)
		//map.forEach((id, builder) -> LootTableManager.validate(validationtracker, id, builder));
	}

//	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return tables;
	}

	@Override
	public String getName() {
		return "TwilightForest loot tables";
	}

	@Override
	public void run(HashCache p_124444_) {
		Path path = this.generator.getOutputFolder();
		Map<ResourceLocation, LootTable> map = Maps.newHashMap();
		this.getTables().forEach((p_124458_) -> {
			p_124458_.getFirst().get().accept((p_176077_, p_176078_) -> {
				if (map.put(p_176077_, p_176078_.setParamSet(p_124458_.getSecond()).build()) != null) {
					throw new IllegalStateException("Duplicate loot table " + p_176077_);
				}
			});
		});
		ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, (p_124465_) -> {
			return null;
		}, map::get);

		validate(map, validationcontext);

		Multimap<String, String> multimap = validationcontext.getProblems();
		if (!multimap.isEmpty()) {
			multimap.forEach((p_124446_, p_124447_) -> {
				LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
			});
			throw new IllegalStateException("Failed to validate loot tables, see logs");
		} else {
			map.forEach((p_124451_, p_124452_) -> {
				Path path1 = createPath(path, p_124451_);

				try {
					DataProvider.save(GSON, p_124444_, LootTables.serialize(p_124452_), path1);
				} catch (IOException ioexception) {
					LOGGER.error("Couldn't save loot table {}", path1, ioexception);
				}

			});
		}
	}
}
