package twilightforest.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructures;

import java.util.concurrent.CompletableFuture;

public class StructureTagGenerator extends StructureTagsProvider {
	public static final TagKey<Structure> LANDMARK = TagKey.create(Registries.STRUCTURE, TwilightForestMod.prefix("landmark"));

	public StructureTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider p_256087_) {
		this.tag(LANDMARK).add(
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
		);
	}
}
