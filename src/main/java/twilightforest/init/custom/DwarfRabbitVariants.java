package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFRegistries;
import twilightforest.TFMain;
import twilightforest.entity.passive.DwarfRabbitVariant;

public class DwarfRabbitVariants {
	public static final ResourceKey<DwarfRabbitVariant> BROWN = makeKey(TFMain.prefix("brown"));
	public static final ResourceKey<DwarfRabbitVariant> DUTCH = makeKey(TFMain.prefix("dutch"));
	public static final ResourceKey<DwarfRabbitVariant> WHITE = makeKey(TFMain.prefix("white"));

	private static ResourceKey<DwarfRabbitVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, name);
	}


	public static void bootstrap(BootstrapContext<DwarfRabbitVariant> context) {
		context.register(BROWN, new DwarfRabbitVariant(TFMain.getModelTexture("bunnybrown.png")));
		context.register(DUTCH, new DwarfRabbitVariant(TFMain.getModelTexture("bunnydutch.png")));
		context.register(WHITE, new DwarfRabbitVariant(TFMain.getModelTexture("bunnywhite.png")));
	}
}
