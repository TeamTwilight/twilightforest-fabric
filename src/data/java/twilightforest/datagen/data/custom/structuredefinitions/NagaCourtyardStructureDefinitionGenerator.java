package twilightforest.datagen.data.custom.structuredefinitions;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import twilightforest.TFMain;
import twilightforest.world.components.structures.courtyard.CourtyardMain;

import java.util.concurrent.CompletableFuture;

public class NagaCourtyardStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public NagaCourtyardStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TFMain.ID, "Naga Courtyard");
	}

	@Override
	protected void generatePools() {
		this.add("courtyard/spawner", CourtyardMain.CENTER_POOL, 100);
	}
}
