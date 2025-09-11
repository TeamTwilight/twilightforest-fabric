package twilightforest.data.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.world.components.structures.camp.CampPieces;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;
import twilightforest.world.components.structures.util.TemplatePoolInstance;

import java.util.concurrent.CompletableFuture;

public class CampStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	@Autowired
	private static CampPieces campPieces;

	public CampStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Camp");
	}

	@Override
	protected void generatePools() {
		this.lookupProvider.thenAccept(this::generatePoolsWithProvider);
	}

	private void generatePoolsWithProvider(HolderLookup.Provider provider) {
		@SuppressWarnings("unchecked")
		Holder.Reference<TemplateMarkerHandlerList> campMarkers = Holder.Reference.createStandAlone((HolderOwner<TemplateMarkerHandlerList>) provider.asGetterLookup().lookupOrThrow(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST), TemplateMarkerHandlers.CAMP_MARKER_HANDLERS);

		this.add("camp/campfire", campPieces.start, this.weightedRigidTemplate(100, 1, null, campMarkers));

		this.configureTents();

		this.configureBigPaths();

		this.configureSmallPaths();

		this.configureDeco();
	}

	private void configureTents() {
		this.add("camp/tent/solo_tent", campPieces.tent, this.weightedRigidTemplate(100, 1, 0, null));
		this.add("camp/tent/duo_tent", campPieces.tent, this.weightedRigidTemplate(75, 1, 0, null));
		this.add("camp/tent/luxury_tent", campPieces.tent, this.weightedRigidTemplate(50, 1, 0, null));
	}

	private void configureBigPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(25, null);

		this.addEmpty(campPieces.mainPath, 400);

		this.add("camp/path/intersection_left", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/intersection_right", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/intersection_short", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/j_path", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/l_path", campPieces.mainPath, pathTemplateData);
	}

	private void configureSmallPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(50, null);

		this.addEmpty(campPieces.path, 400);

		this.addToAllPools("camp/path/path_2x4", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x6", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x7", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_3x4", pathTemplateData, campPieces.mainPath, campPieces.path);
	}

	private void configureDeco() {
		TemplatePoolInstance rigidTemplateDataAnyDiff = this.weightedRigidTemplate(100, 1, null, null);
		TemplatePoolInstance rigidTemplateDataNoDiff = this.weightedRigidTemplate(100, 1, 0, null);
		TemplatePoolInstance berryTemplateData = this.weightedRigidTemplate(75, 1, 0, null);

		this.add("camp/deco/double_drying_rack", campPieces.deco, rigidTemplateDataAnyDiff);
		this.add("camp/deco/long_drying_rack", campPieces.deco, rigidTemplateDataAnyDiff);

		this.add("camp/deco/garden_1x2", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/garden_2x4", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/garden_straight", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/garden_u", campPieces.deco, rigidTemplateDataNoDiff);

		this.add("camp/deco/berries_staked", campPieces.deco, berryTemplateData);
		this.add("camp/deco/berries_trellis", campPieces.deco, berryTemplateData);
		this.add("camp/deco/blackberry_wall", campPieces.deco, berryTemplateData);
		this.add("camp/deco/blueberry_wall", campPieces.deco, berryTemplateData);
		this.add("camp/deco/raspberry_wall", campPieces.deco, berryTemplateData);

		this.add("camp/deco/lumber1", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/lumber2", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/lumber3", campPieces.deco, rigidTemplateDataNoDiff);

		this.add("camp/deco/water_basin", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/wooden_basin", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/repair_station", campPieces.deco, rigidTemplateDataNoDiff);
		this.addEmpty(campPieces.deco, 1000);
	}
}
