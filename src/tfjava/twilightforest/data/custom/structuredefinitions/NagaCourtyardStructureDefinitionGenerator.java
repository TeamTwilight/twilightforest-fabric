package twilightforest.data.custom.structuredefinitions;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class NagaCourtyardStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	private static final ResourceLocation CENTER_POOL = TwilightForestMod.prefix("courtyard/center");

	public NagaCourtyardStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, Object existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Naga Courtyard");
	}

	@Override
	protected void generatePools() {
		this.add("courtyard/spawner", CENTER_POOL, 100);
	}
}
