package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import twilightforest.TFCommon;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.init.custom.DwarfRabbitVariants;

public class DwarfRabbitVariantGenerator {
	public static void bootstrap(BootstrapContext<DwarfRabbitVariant> context) {
		TFCommon.LOGGER.info("Bootstrap called for dwarf rabbit variants...");
		context.register(DwarfRabbitVariants.BROWN, new DwarfRabbitVariant(TFCommon.getModelTexture("bunnybrown.png")));
		context.register(DwarfRabbitVariants.DUTCH, new DwarfRabbitVariant(TFCommon.getModelTexture("bunnydutch.png")));
		context.register(DwarfRabbitVariants.WHITE, new DwarfRabbitVariant(TFCommon.getModelTexture("bunnywhite.png")));
	}
}