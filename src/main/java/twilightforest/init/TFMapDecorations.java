package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.TFMain;

public class TFMapDecorations {

	public static final MapDecorationType HEDGE_MAZE = register("hedge_maze");
	public static final MapDecorationType SMALL_HOLLOW_HILL = register("small_hollow_hill");
	public static final MapDecorationType MEDIUM_HOLLOW_HILL = register("medium_hollow_hill");
	public static final MapDecorationType LARGE_HOLLOW_HILL = register("large_hollow_hill");
	public static final MapDecorationType QUEST_GROVE = register("quest_grove");
	public static final MapDecorationType NAGA_COURTYARD = register("naga_courtyard");
	public static final MapDecorationType LICH_TOWER = register("lich_tower");
	public static final MapDecorationType LABYRINTH = register("labyrinth");
	public static final MapDecorationType HYDRA_LAIR = register("hydra_lair");
	public static final MapDecorationType KNIGHT_STRONGHOLD = register("knight_stronghold");
	public static final MapDecorationType DARK_TOWER = register("dark_tower");
	public static final MapDecorationType YETI_LAIR = register("yeti_lair");
	public static final MapDecorationType AURORA_PALACE = register("aurora_palace");
	public static final MapDecorationType TROLL_CAVES = register("troll_caves");
	public static final MapDecorationType FINAL_CASTLE = register("final_castle");

	private static MapDecorationType register(String name) {
		return Registry.register(
			BuiltInRegistries.MAP_DECORATION_TYPE,
			TFMain.prefix(name),
			new MapDecorationType(TFMain.prefix(name), true, -1, false, true)
		);
	}
}