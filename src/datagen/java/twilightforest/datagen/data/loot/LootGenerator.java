package twilightforest.datagen.data.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootGenerator extends LootTableProvider {
	public LootGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, TFLootTables.allBuiltin(), List.of(
			new SubProviderEntry(BlockInteractLootTables::new, LootContextParamSets.BLOCK_INTERACT),
			new SubProviderEntry(EntityInteractLootTables::new, LootContextParamSets.ENTITY_INTERACT),
			new SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST),
			new SubProviderEntry(SpecialLootTables::new, LootContextParamSets.EMPTY)
		), provider);
	}
}