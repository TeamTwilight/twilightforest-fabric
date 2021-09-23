package twilightforest.world.registration;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import twilightforest.TFConstants;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.structures.courtyard.CourtyardStairsTemplateProcessor;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;
import twilightforest.world.components.structures.courtyard.CourtyardWallTemplateProcessor;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final StructureProcessorType<CourtyardTerraceTemplateProcessor> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", CourtyardTerraceTemplateProcessor.codecTerraceProcessor);
	public static final StructureProcessorType<CourtyardStairsTemplateProcessor> COURTYARD_STAIRS = registerProcessor("courtyard_stairs", CourtyardStairsTemplateProcessor.codecStairsProcessor);
	public static final StructureProcessorType<CourtyardWallTemplateProcessor> COURTYARD_WALL = registerProcessor("courtyard_wall", CourtyardWallTemplateProcessor.codecWallProcessor);
	public static final StructureProcessorType<MossyCobbleTemplateProcessor> MOSSY_COBBLE = registerProcessor("mossy_cobble", MossyCobbleTemplateProcessor.codecMossyProcessor);
	public static final StructureProcessorType<StoneBricksTemplateProcessor> STONE_BRICKS = registerProcessor("stone_bricks", StoneBricksTemplateProcessor.codecBricksProcessor);
	public static final StructureProcessorType<CobblePlankSwizzler> COBBLE_PLANK_SWIZZLER = registerProcessor("cobble_plank_swizzler", CobblePlankSwizzler.CODEC);
	public static final StructureProcessorType<GraveyardFeature.WebTemplateProcessor> WEB = registerProcessor("web", GraveyardFeature.WebTemplateProcessor.codecWebProcessor);
	public static final StructureProcessorType<BoxCuttingProcessor> BOX_CUTTING_PROCESSOR = registerProcessor("box_cutting", BoxCuttingProcessor.CODEC);

	public static <P extends StructureProcessor> StructureProcessorType<P> registerProcessor(String name, Codec<P> processor) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, TFConstants.prefix(name), () -> processor);
	}

	public static void init() {}
}
