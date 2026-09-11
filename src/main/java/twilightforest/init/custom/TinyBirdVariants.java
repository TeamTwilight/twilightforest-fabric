package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;
import twilightforest.init.TFRegistries;
import twilightforest.entity.passive.TinyBirdVariant;

public class TinyBirdVariants {

	public static final ResourceKey<TinyBirdVariant> BLUE = makeKey(TFCommon.prefix("blue"));
	public static final ResourceKey<TinyBirdVariant> BROWN = makeKey(TFCommon.prefix("brown"));
	public static final ResourceKey<TinyBirdVariant> GOLD = makeKey(TFCommon.prefix("gold"));
	public static final ResourceKey<TinyBirdVariant> RED = makeKey(TFCommon.prefix("red"));

	private static ResourceKey<TinyBirdVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.TINY_BIRD_VARIANT, name);
	}

	public static void bootstrap(BootstrapContext<TinyBirdVariant> context) {
		context.register(BLUE, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdblue.png")));
		context.register(BROWN, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdbrown.png")));
		context.register(GOLD, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdgold.png")));
		context.register(RED, new TinyBirdVariant(TFCommon.getModelTexture("tinybirdred.png")));
	}
}
