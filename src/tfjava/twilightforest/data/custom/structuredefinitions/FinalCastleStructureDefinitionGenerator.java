package twilightforest.data.custom.structuredefinitions;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class FinalCastleStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	private static final ResourceLocation LARGE_TOWER_TEMP_POOL = TwilightForestMod.prefix("final_castle/temp/large_tower");
	private static final ResourceLocation BELL_TOWER_TEMP_POOL = TwilightForestMod.prefix("final_castle/temp/bell_tower");
	private static final ResourceLocation GAZEBO_TEMP_POOL = TwilightForestMod.prefix("final_castle/temp/gazebo");

	public FinalCastleStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, Object existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Final Castle");
	}

	@Override
	protected void generatePools() {
		this.add("final_castle/temp/large_tower", LARGE_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/bell_tower", BELL_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/gazebo", GAZEBO_TEMP_POOL, 100);
	}
}
