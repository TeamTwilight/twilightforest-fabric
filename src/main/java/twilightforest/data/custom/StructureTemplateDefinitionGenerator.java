package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerPieces;

import java.util.concurrent.CompletableFuture;

public class StructureTemplateDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public StructureTemplateDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void generatePools() {
		this.addAllTemplatesToPool(LichTowerPieces.MOB_BRIDGE, 1,
			"lich_tower/bridge_spawner",
			"lich_tower/bridge_spawner_bend",
			"lich_tower/bridge_spawner_ropes",
			"lich_tower/bridge_spawner_wide",
			"lich_tower/bridge_spawner_zag",
			"lich_tower/bridge_spawner_zig"
		);
	}
}
