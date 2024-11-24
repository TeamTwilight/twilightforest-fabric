package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;

import java.util.function.Supplier;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final LazyRegistrar<StructureProcessorType<?>> STRUCTURE_PROCESSORS = LazyRegistrar.create(Registries.STRUCTURE_PROCESSOR, TwilightForestMod.ID);

	public static final RegistryObject<StructureProcessorType<CobbleVariants>> COBBLE_VARIANTS = registerProcessor("cobble_variants", () -> () -> CobbleVariants.CODEC);
	public static final RegistryObject<StructureProcessorType<SmoothStoneVariants>> SMOOTH_STONE_VARIANTS = registerProcessor("smooth_stone_variants", () -> () -> SmoothStoneVariants.CODEC);
	public static final RegistryObject<StructureProcessorType<StoneBricksVariants>> STONE_BRICK_VARIANTS = registerProcessor("stone_brick_variants", () -> () -> StoneBricksVariants.CODEC);
	public static final RegistryObject<StructureProcessorType<NagastoneVariants>> NAGASTONE_VARIANTS = registerProcessor("nagastone_variants", () -> () -> NagastoneVariants.CODEC);

	public static final RegistryObject<StructureProcessorType<StateTransfiguringProcessor>> STATE_TRANSFIGURING = registerProcessor("state_transfiguring", () -> () -> StateTransfiguringProcessor.CODEC);

	public static final RegistryObject<StructureProcessorType<WoodPaletteSwizzle>> PLANK_SWIZZLE = registerProcessor("wood_swizzle", () -> () -> WoodPaletteSwizzle.CODEC);
	public static final RegistryObject<StructureProcessorType<SmartGrassProcessor>> SMART_GRASS = registerProcessor("smart_grass", () -> () -> SmartGrassProcessor.CODEC);
	public static final RegistryObject<StructureProcessorType<BoxCuttingProcessor>> BOX_CUTTING_PROCESSOR = registerProcessor("box_cutting", () -> () -> BoxCuttingProcessor.CODEC);
	public static final RegistryObject<StructureProcessorType<TargetedRotProcessor>> TARGETED_ROT = registerProcessor("targeted_rot", () -> () -> TargetedRotProcessor.CODEC);

	public static final RegistryObject<StructureProcessorType<GraveyardFeature.WebTemplateProcessor>> WEB = registerProcessor("web", () -> () -> GraveyardFeature.WebTemplateProcessor.CODEC);
	public static final RegistryObject<StructureProcessorType<CourtyardTerraceTemplateProcessor>> COURTYARD_TERRACE = registerProcessor("courtyard_terrace", () -> () -> CourtyardTerraceTemplateProcessor.CODEC);

	//goofy but needed
	public static <P extends StructureProcessor> RegistryObject<StructureProcessorType<P>> registerProcessor(String name, Supplier<StructureProcessorType<P>> processor) {
		return STRUCTURE_PROCESSORS.register(name, processor);
	}
}
