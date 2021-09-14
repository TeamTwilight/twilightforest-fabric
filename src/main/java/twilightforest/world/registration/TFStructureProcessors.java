package twilightforest.world.registration;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.DruidHutTemplateProcessor;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.MossyCobbleTemplateProcessor;
import twilightforest.world.components.processors.StoneBricksTemplateProcessor;
import twilightforest.world.components.structures.courtyard.CourtyardStairsTemplateProcessor;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;
import twilightforest.world.components.structures.courtyard.CourtyardWallTemplateProcessor;
import twilightforest.world.components.processors.BoxCuttingProcessor;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final StructureProcessorType<?> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", CourtyardTerraceTemplateProcessor.codecTerraceProcessor);
	public static final StructureProcessorType<?> COURTYARD_STAIRS = registerProcessor("courtyard_stairs", CourtyardStairsTemplateProcessor.codecStairsProcessor);
	public static final StructureProcessorType<?> COURTYARD_WALL = registerProcessor("courtyard_wall", CourtyardWallTemplateProcessor.codecWallProcessor);
	public static final StructureProcessorType<?> MOSSY_COBBLE = registerProcessor("mossy_cobble", MossyCobbleTemplateProcessor.codecMossyProcessor);
	public static final StructureProcessorType<?> STONE_BRICKS = registerProcessor("stone_bricks", StoneBricksTemplateProcessor.codecBricksProcessor);
	public static final StructureProcessorType<?> HUT = registerProcessor("hut", DruidHutTemplateProcessor.CODEC);
	public static final StructureProcessorType<?> WEB = registerProcessor("web", GraveyardFeature.WebTemplateProcessor.codecWebProcessor);
	public static final StructureProcessorType<?> BOX_CUTTING_PROCESSOR = registerProcessor("box_cutting", BoxCuttingProcessor.CODEC);

	public static <P extends StructureProcessor> StructureProcessorType<P> registerProcessor(String name, Codec<P> processor) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, TwilightForestMod.prefix(name), () -> processor);
	}
}
