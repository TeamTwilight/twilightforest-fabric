package twilightforest.world.components.structures.camp;

import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;

public final class CampPieces {
	public static final CampPieces INSTANCE = new CampPieces();

	public final Identifier start = TFCommon.prefix("camp/structure_start");
	public final Identifier tent = TFCommon.prefix("camp/tent");
	public final Identifier rackPath = TFCommon.prefix("camp/rack_path");
	public final Identifier mainPath = TFCommon.prefix("camp/main_path");
	public final Identifier path = TFCommon.prefix("camp/path");
	public final Identifier deco = TFCommon.prefix("camp/deco");
	public final Identifier rack = TFCommon.prefix("camp/rack");
}
