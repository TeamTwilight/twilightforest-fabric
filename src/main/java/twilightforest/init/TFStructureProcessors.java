package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import twilightforest.TFMain;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final StructureProcessorType<CobbleVariants> COBBLE_VARIANTS = registerProcessor("cobble_variants", () -> CobbleVariants.CODEC);
	public static final StructureProcessorType<SmoothStoneVariants> SMOOTH_STONE_VARIANTS = registerProcessor("smooth_stone_variants", () -> SmoothStoneVariants.CODEC);
	public static final StructureProcessorType<StoneBricksVariants> STONE_BRICK_VARIANTS = registerProcessor("stone_brick_variants", () -> StoneBricksVariants.CODEC);
	public static final StructureProcessorType<InfestBlocksProcessor> INFEST_BLOCKS = registerProcessor("infest_blocks", () -> InfestBlocksProcessor.CODEC);
	public static final StructureProcessorType<NagastoneVariants> NAGASTONE_VARIANTS = registerProcessor("nagastone_variants", () -> NagastoneVariants.CODEC);

	public static final StructureProcessorType<StateTransfiguringProcessor> STATE_TRANSFIGURING = registerProcessor("state_transfiguring", () -> StateTransfiguringProcessor.CODEC);

	public static final StructureProcessorType<WoodPaletteSwizzle> PLANK_SWIZZLE = registerProcessor("wood_swizzle", () -> WoodPaletteSwizzle.CODEC);
	public static final StructureProcessorType<SmartGrassProcessor> SMART_GRASS = registerProcessor("smart_grass", () -> SmartGrassProcessor.CODEC);
	public static final StructureProcessorType<BoxCuttingProcessor> BOX_CUTTING_PROCESSOR = registerProcessor("box_cutting", () -> BoxCuttingProcessor.CODEC);
	public static final StructureProcessorType<TargetedRotProcessor> TARGETED_ROT = registerProcessor("targeted_rot", () -> TargetedRotProcessor.CODEC);

	public static final StructureProcessorType<GraveyardFeature.WebTemplateProcessor> WEB = registerProcessor("web", () -> GraveyardFeature.WebTemplateProcessor.CODEC);
	public static final StructureProcessorType<CourtyardTerraceTemplateProcessor> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", () -> CourtyardTerraceTemplateProcessor.CODEC);

	public static final StructureProcessorType<SoftReplaceProcessor> SOFT_REPLACE = registerProcessor("soft_replace", () -> SoftReplaceProcessor.CODEC);

	public static final StructureProcessorType<SpawnerProcessor> SPAWNER_PROCESSOR = registerProcessor("spawner_processor", () -> SpawnerProcessor.CODEC);
	public static final StructureProcessorType<UpdateMarkingProcessor> UPDATE_MARKING_PROCESSOR = registerProcessor("update_marking", () -> UpdateMarkingProcessor.CODEC);

	public static final StructureProcessorType<VerticalDecayProcessor> VERTICAL_DECAY = registerProcessor("vertical_decay", () -> VerticalDecayProcessor.CODEC);
	public static final StructureProcessorType<WoodMultiPaletteSwizzle> PLANK_MULTISWIZZLE = registerProcessor("wood_multiswizzle", () -> WoodMultiPaletteSwizzle.CODEC);

	public static <P extends StructureProcessor> StructureProcessorType<P> registerProcessor(String name, StructureProcessorType<P> processor) {
		return Registry.register(
			BuiltInRegistries.STRUCTURE_PROCESSOR,
			TFMain.prefix(name),
			processor
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing structure processor types...");
	}
}