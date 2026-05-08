package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.TwilightForestMod;

public final class TFMapDecorations {
    public static final Holder<MapDecorationType> AURORA_PALACE = decoration("aurora_palace");
    public static final Holder<MapDecorationType> DARK_TOWER = decoration("dark_tower");
    public static final Holder<MapDecorationType> FINAL_CASTLE = decoration("final_castle");
    public static final Holder<MapDecorationType> HEDGE_MAZE = decoration("hedge_maze");
    public static final Holder<MapDecorationType> HYDRA_LAIR = decoration("hydra_lair");
    public static final Holder<MapDecorationType> KNIGHT_STRONGHOLD = decoration("knight_stronghold");
    public static final Holder<MapDecorationType> LABYRINTH = decoration("labyrinth");
    public static final Holder<MapDecorationType> LICH_TOWER = decoration("lich_tower");
    public static final Holder<MapDecorationType> NAGA_COURTYARD = decoration("naga_courtyard");
    public static final Holder<MapDecorationType> QUEST_GROVE = decoration("quest_grove");
    public static final Holder<MapDecorationType> LARGE_HOLLOW_HILL = decoration("large_hollow_hill");
    public static final Holder<MapDecorationType> MEDIUM_HOLLOW_HILL = decoration("medium_hollow_hill");
    public static final Holder<MapDecorationType> SMALL_HOLLOW_HILL = decoration("small_hollow_hill");
    public static final Holder<MapDecorationType> TROLL_CAVES = decoration("troll_caves");
    public static final Holder<MapDecorationType> YETI_LAIR = decoration("yeti_lair");

    private TFMapDecorations() {
    }

    private static Holder<MapDecorationType> decoration(String path) {
        ResourceKey<MapDecorationType> key = ResourceKey.create(BuiltInRegistries.MAP_DECORATION_TYPE.key(), TwilightForestMod.prefix(path));
        Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, key.location(),
                new MapDecorationType(TwilightForestMod.prefix(path), true, -1, false, true));
        return BuiltInRegistries.MAP_DECORATION_TYPE.getHolderOrThrow(key);
    }
}
