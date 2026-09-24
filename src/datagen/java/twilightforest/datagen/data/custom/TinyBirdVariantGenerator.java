package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import twilightforest.TFCommon;
import twilightforest.entity.passive.TinyBirdVariant;
import twilightforest.init.custom.TinyBirdVariants;

public class TinyBirdVariantGenerator {
	public static void bootstrap(BootstrapContext<TinyBirdVariant> context) {
		TFCommon.LOGGER.info("Bootstrap called for tiny bird variants...");
		context.register(TinyBirdVariants.BLUE, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdblue.png")));
		context.register(TinyBirdVariants.BROWN, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdbrown.png")));
		context.register(TinyBirdVariants.GOLD, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdgold.png")));
		context.register(TinyBirdVariants.RED, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdred.png")));
	}
}