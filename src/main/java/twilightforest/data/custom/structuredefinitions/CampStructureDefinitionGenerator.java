package twilightforest.data.custom.structuredefinitions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.world.components.processors.StateTransfiguringProcessor;
import twilightforest.world.components.structures.camp.CampPieces;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;
import twilightforest.world.components.structures.util.TemplatePoolInstance;
import twilightforest.util.TFBeanRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CampStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	private static CampPieces campPieces;

	private static CampPieces getCampPieces() {
		if (campPieces == null) {
			campPieces = TFBeanRegistry.get(CampPieces.class);
		}
		return campPieces;
	}

	public CampStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Camp");
	}

	@Override
	protected void generatePools() {
		this.lookupProvider.thenAccept(this::generatePoolsWithProvider);
	}

	private void generatePoolsWithProvider(HolderLookup.Provider provider) {
		Holder.Reference<TemplateMarkerHandlerList> campMarkers = this.getMarkers(provider, TemplateMarkerHandlers.CAMP_MARKER_HANDLERS);

		this.add("camp/campfire_east", getCampPieces().start, this.weightedRigidTemplate(100, 1, null, campMarkers, null, null));
		this.add("camp/campfire_south", getCampPieces().start, this.weightedRigidTemplate(100, 1, null, campMarkers, null, null));
		this.add("camp/campfire_west", getCampPieces().start, this.weightedRigidTemplate(100, 1, null, campMarkers, null, null));

		this.configureTents(provider);

		this.configureRackPaths();

		this.configureBigPaths();

		this.configureSmallPaths();

		this.configureDeco(campMarkers);
	}

	private void configureTents(HolderLookup.Provider provider) {
		Holder.Reference<TemplateMarkerHandlerList> campMarkers = this.getMarkers(provider, TemplateMarkerHandlers.CAMP_MARKER_HANDLERS);

		int defaultWeight = 10;
		TemplatePoolInstance.ChooseRandomProcessors randomizedProcessors = new TemplatePoolInstance.ChooseRandomProcessors(List.of(
			SimpleWeightedRandomList.<StructureProcessor>builder()
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.WHITE_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.LIGHT_GRAY_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.GRAY_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.BROWN_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.GREEN_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.BLUE_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.RED_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.LIME_WOOL.defaultBlockState()))), 1)
				.build(),
			SimpleWeightedRandomList.<StructureProcessor>builder()
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.RED_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.LIGHT_BLUE_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.LIME_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.ORANGE_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.LIGHT_GRAY_BED.defaultBlockState()))), defaultWeight)
				.build()
		));

		this.add("camp/tent/solo_tent", getCampPieces().tent, this.weightedRigidTemplate(100, 1, 0, campMarkers, randomizedProcessors, null));
		this.add("camp/tent/duo_tent", getCampPieces().tent, this.weightedRigidTemplate(75, 1, 0, campMarkers, randomizedProcessors, null));
		this.add("camp/tent/open_tent", getCampPieces().tent, this.weightedRigidTemplate(75, 1, 0, campMarkers, randomizedProcessors, null));
		this.add("camp/tent/luxury_tent", getCampPieces().tent, this.weightedRigidTemplate(50, 1, 0, campMarkers, randomizedProcessors, null));
	}

	private void configureRackPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(100, null, Map.of(getCampPieces().deco.toString(), getCampPieces().rack.toString()));

		this.add("camp/path/path_2x4", getCampPieces().rackPath, pathTemplateData);
		this.add("camp/path/path_3x4", getCampPieces().rackPath, pathTemplateData);
	}

	private void configureBigPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(25, null, null);

		this.addEmpty(getCampPieces().mainPath, 300);

		this.add("camp/path/intersection_left", getCampPieces().mainPath, pathTemplateData);
		this.add("camp/path/intersection_right", getCampPieces().mainPath, pathTemplateData);
		this.add("camp/path/intersection_short", getCampPieces().mainPath, pathTemplateData);
		this.add("camp/path/j_path", getCampPieces().mainPath, pathTemplateData);
		this.add("camp/path/l_path", getCampPieces().mainPath, pathTemplateData);
	}

	private void configureSmallPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(50, null, null);

		this.addEmpty(getCampPieces().path, 300);

		this.addToAllPools("camp/path/path_2x4", pathTemplateData, getCampPieces().mainPath, getCampPieces().path);
		this.addToAllPools("camp/path/path_2x6", pathTemplateData, getCampPieces().mainPath, getCampPieces().path);
		this.addToAllPools("camp/path/path_2x7", pathTemplateData, getCampPieces().mainPath, getCampPieces().path);
		this.addToAllPools("camp/path/path_3x4", pathTemplateData, getCampPieces().mainPath, getCampPieces().path);
	}

	private void configureDeco(Holder.Reference<TemplateMarkerHandlerList> campMarkers) {
		TemplatePoolInstance twoLayerTemplateData = this.weightedRigidTemplate(100, 2, 0, campMarkers, null, null);
		TemplatePoolInstance berryTemplateData = this.weightedRigidTemplate(75, 2, 0, campMarkers, null, null);

		this.addToAllPools("camp/deco/double_drying_rack", twoLayerTemplateData, getCampPieces().deco, getCampPieces().rack);
		this.addToAllPools("camp/deco/long_drying_rack", twoLayerTemplateData, getCampPieces().deco, getCampPieces().rack);

		this.add("camp/deco/garden_1x3", getCampPieces().deco, twoLayerTemplateData);
		this.add("camp/deco/garden_2x4", getCampPieces().deco, twoLayerTemplateData);
		this.add("camp/deco/garden_straight", getCampPieces().deco, twoLayerTemplateData);
		this.add("camp/deco/garden_u", getCampPieces().deco, twoLayerTemplateData);

		this.add("camp/deco/berries_staked", getCampPieces().deco, berryTemplateData);
		this.add("camp/deco/berries_trellis", getCampPieces().deco, berryTemplateData);
		this.add("camp/deco/blackberry_wall", getCampPieces().deco, berryTemplateData);
		this.add("camp/deco/blueberry_wall", getCampPieces().deco, berryTemplateData);
		this.add("camp/deco/raspberry_wall", getCampPieces().deco, berryTemplateData);
		this.add("camp/deco/berries_support", getCampPieces().deco, berryTemplateData);

		this.add("camp/deco/compost_large", getCampPieces().deco, twoLayerTemplateData);
		this.add("camp/deco/compost_small", getCampPieces().deco, twoLayerTemplateData);

		this.add("camp/deco/repair_station", getCampPieces().deco, twoLayerTemplateData);

		this.add("camp/deco/pen", getCampPieces().deco, twoLayerTemplateData);

		TemplatePoolInstance.ChooseRandomProcessors randomizedWoods = new TemplatePoolInstance.ChooseRandomProcessors(List.of(
			SimpleWeightedRandomList.<StructureProcessor>builder()
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, TFBlocks.TWILIGHT_OAK_LOG.value().defaultBlockState()))), 100)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, TFBlocks.CANOPY_LOG.value().defaultBlockState()))), 90)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, Blocks.OAK_LOG.defaultBlockState()))), 80)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, Blocks.BIRCH_LOG.defaultBlockState()))), 70)
				.build()
		));
		TemplatePoolInstance lumberTemplateData = this.weightedRigidTemplate(100, 1, 0, campMarkers, randomizedWoods, null);

		this.add("camp/deco/lumber1", getCampPieces().deco, lumberTemplateData);
		this.add("camp/deco/lumber2", getCampPieces().deco, lumberTemplateData);
		this.add("camp/deco/lumber3", getCampPieces().deco, lumberTemplateData);

		this.addEmpty(getCampPieces().deco, 1000);
	}
}
