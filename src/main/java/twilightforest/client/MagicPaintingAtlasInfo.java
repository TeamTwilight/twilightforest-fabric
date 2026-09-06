package twilightforest.client;

import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;

public class MagicPaintingAtlasInfo {
	public final static String MAGIC_PAINTING_PATH = "magic_paintings";
	public static final Identifier ATLAS_LOCATION = TFCommon.prefix("textures/atlas/magic_paintings.png");
	public static final Identifier ATLAS_INFO_LOCATION = Identifier.withDefaultNamespace(MAGIC_PAINTING_PATH);
	public static final Identifier BACK_SPRITE_LOCATION = TFCommon.prefix(MAGIC_PAINTING_PATH + "/back");
}