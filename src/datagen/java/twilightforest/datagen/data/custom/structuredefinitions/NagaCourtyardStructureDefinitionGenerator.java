package twilightforest.datagen.data.custom.structuredefinitions;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import twilightforest.world.components.structures.courtyard.CourtyardMain;

import java.util.concurrent.CompletableFuture;

public class NagaCourtyardStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public NagaCourtyardStructureDefinitionGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, "Naga Courtyard");
	}

	@Override
	protected void generatePools(HolderLookup.Provider provider) {
		this.add("courtyard/spawner", CourtyardMain.CENTER_POOL, 100);
	}
}
