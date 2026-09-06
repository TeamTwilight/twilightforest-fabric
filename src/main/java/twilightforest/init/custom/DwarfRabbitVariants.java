package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;
import twilightforest.TFRegistries;
import twilightforest.entity.passive.DwarfRabbitVariant;

public class DwarfRabbitVariants {
	public static final ResourceKey<DwarfRabbitVariant> BROWN = makeKey(TFCommon.prefix("brown"));
	public static final ResourceKey<DwarfRabbitVariant> DUTCH = makeKey(TFCommon.prefix("dutch"));
	public static final ResourceKey<DwarfRabbitVariant> WHITE = makeKey(TFCommon.prefix("white"));

	private static ResourceKey<DwarfRabbitVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, name);
	}


	public static void bootstrap(BootstrapContext<DwarfRabbitVariant> context) {
		context.register(BROWN, new DwarfRabbitVariant(TFCommon.getModelTexture("bunnybrown.png")));
		context.register(DUTCH, new DwarfRabbitVariant(TFCommon.getModelTexture("bunnydutch.png")));
		context.register(WHITE, new DwarfRabbitVariant(TFCommon.getModelTexture("bunnywhite.png")));
	}
}
