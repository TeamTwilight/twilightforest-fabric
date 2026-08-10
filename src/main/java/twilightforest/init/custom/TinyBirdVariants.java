package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFMain;
import twilightforest.TFRegistries;
import twilightforest.entity.passive.TinyBirdVariant;

public class TinyBirdVariants {

	public static final ResourceKey<TinyBirdVariant> BLUE = makeKey(TFMain.prefix("blue"));
	public static final ResourceKey<TinyBirdVariant> BROWN = makeKey(TFMain.prefix("brown"));
	public static final ResourceKey<TinyBirdVariant> GOLD = makeKey(TFMain.prefix("gold"));
	public static final ResourceKey<TinyBirdVariant> RED = makeKey(TFMain.prefix("red"));

	private static ResourceKey<TinyBirdVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.TINY_BIRD_VARIANT, name);
	}

	public static void bootstrap(BootstrapContext<TinyBirdVariant> context) {
		context.register(BLUE, new TinyBirdVariant(TFMain.getModelTexture("tinybirdblue.png")));
		context.register(BROWN, new TinyBirdVariant(TFMain.getModelTexture("tinybirdbrown.png")));
		context.register(GOLD, new TinyBirdVariant(TFMain.getModelTexture("tinybirdgold.png")));
		context.register(RED, new TinyBirdVariant(TFMain.getModelTexture("tinybirdred.png")));
	}
}
