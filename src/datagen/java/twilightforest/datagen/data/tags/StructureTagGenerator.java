package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.StructureTagsProvider;
import twilightforest.init.TFStructures;
import twilightforest.tags.TFStructureTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StructureTagGenerator extends StructureTagsProvider {

	public StructureTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFStructureTags.LANDMARK).addAll(List.of(
			TFStructures.HEDGE_MAZE,
			TFStructures.QUEST_GROVE,
			TFStructures.MUSHROOM_TOWER,
			TFStructures.HOLLOW_HILL_SMALL,
			TFStructures.HOLLOW_HILL_MEDIUM,
			TFStructures.HOLLOW_HILL_LARGE,
			TFStructures.NAGA_COURTYARD,
			TFStructures.LICH_TOWER,
			TFStructures.LABYRINTH,
			TFStructures.HYDRA_LAIR,
			TFStructures.KNIGHT_STRONGHOLD,
			TFStructures.DARK_TOWER,
			TFStructures.YETI_CAVE,
			TFStructures.AURORA_PALACE,
			TFStructures.TROLL_CAVE,
			TFStructures.FINAL_CASTLE
		));
	}
}
