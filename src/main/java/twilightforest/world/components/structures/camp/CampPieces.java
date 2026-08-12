package twilightforest.world.components.structures.camp;

import net.minecraft.resources.Identifier;
import twilightforest.TFMain;

public final class CampPieces {
	public static final CampPieces INSTANCE = new CampPieces();

	public final Identifier start = TFMain.prefix("camp/structure_start");
	public final Identifier tent = TFMain.prefix("camp/tent");
	public final Identifier rackPath = TFMain.prefix("camp/rack_path");
	public final Identifier mainPath = TFMain.prefix("camp/main_path");
	public final Identifier path = TFMain.prefix("camp/path");
	public final Identifier deco = TFMain.prefix("camp/deco");
	public final Identifier rack = TFMain.prefix("camp/rack");
}
