package twilightforest.datagen.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootGenerator extends LootTableProvider {
	public LootGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, TFLootTables.allBuiltin(), List.of(
			new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK),
			new SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST),
			new SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY),
			new SubProviderEntry(SpecialLootTables::new, LootContextParamSets.EMPTY)
		), provider);
	}
}