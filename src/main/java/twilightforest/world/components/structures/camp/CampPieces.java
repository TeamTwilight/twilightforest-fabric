package twilightforest.world.components.structures.camp;

import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.util.TFBeanRegistry;

public final class CampPieces {
	public static final CampPieces INSTANCE = new CampPieces();

	static {
		TFBeanRegistry.register(CampPieces.class, INSTANCE);
	}

	public final ResourceLocation start = TwilightForestMod.prefix("camp/structure_start");
	public final ResourceLocation tent = TwilightForestMod.prefix("camp/tent");
	public final ResourceLocation rackPath = TwilightForestMod.prefix("camp/rack_path");
	public final ResourceLocation mainPath = TwilightForestMod.prefix("camp/main_path");
	public final ResourceLocation path = TwilightForestMod.prefix("camp/path");
	public final ResourceLocation deco = TwilightForestMod.prefix("camp/deco");
	public final ResourceLocation rack = TwilightForestMod.prefix("camp/rack");
}
