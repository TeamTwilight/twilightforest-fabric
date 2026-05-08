package twilightforest.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.dimension.DimensionType;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.util.WoodPalette;

public final class CustomTagGenerator {
    private CustomTagGenerator() {
    }

    public static final class DimensionTypeTagGenerator {
        public static final TagKey<DimensionType> ALLOWS_MAGIC_MAP_CHARTING =
            TagKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("allows_magic_map_charting"));

        private DimensionTypeTagGenerator() {
        }
    }

    public static final class PaintingVariantTagGenerator {
        public static final TagKey<PaintingVariant> LICH_TOWER_PAINTINGS =
            TagKey.create(Registries.PAINTING_VARIANT, TwilightForestMod.prefix("tower_paintings"));
        public static final TagKey<PaintingVariant> LICH_BOSS_PAINTINGS =
            TagKey.create(Registries.PAINTING_VARIANT, TwilightForestMod.prefix("tower_boss_paintings"));

        private PaintingVariantTagGenerator() {
        }
    }

    public static final class BannerPatternTagGenerator {
        public static final TagKey<BannerPattern> NAGA_BANNER_PATTERN = create("pattern_item/naga");
        public static final TagKey<BannerPattern> LICH_BANNER_PATTERN = create("pattern_item/lich");
        public static final TagKey<BannerPattern> MINOSHROOM_BANNER_PATTERN = create("pattern_item/minoshroom");
        public static final TagKey<BannerPattern> HYDRA_BANNER_PATTERN = create("pattern_item/hydra");
        public static final TagKey<BannerPattern> KNIGHT_PHANTOM_BANNER_PATTERN = create("pattern_item/knight_phantom");
        public static final TagKey<BannerPattern> UR_GHAST_BANNER_PATTERN = create("pattern_item/ur_ghast");
        public static final TagKey<BannerPattern> ALPHA_YETI_BANNER_PATTERN = create("pattern_item/alpha_yeti");
        public static final TagKey<BannerPattern> SNOW_QUEEN_BANNER_PATTERN = create("pattern_item/snow_queen");
        public static final TagKey<BannerPattern> QUEST_RAM_BANNER_PATTERN = create("pattern_item/quest_ram");

        private BannerPatternTagGenerator() {
        }

        private static TagKey<BannerPattern> create(String name) {
            return TagKey.create(Registries.BANNER_PATTERN, TwilightForestMod.prefix(name));
        }
    }

    public static final class WoodPaletteTagGenerator {
        public static final TagKey<WoodPalette> WELL_SWIZZLE_MASK = create("well_swizzle_mask");
        public static final TagKey<WoodPalette> DRUID_HUT_SWIZZLE_MASK = create("druid_hut_swizzle_mask");
        public static final TagKey<WoodPalette> COMMON_PALETTES = create("common");
        public static final TagKey<WoodPalette> UNCOMMON_PALETTES = create("uncommon");
        public static final TagKey<WoodPalette> RARE_PALETTES = create("rare");
        public static final TagKey<WoodPalette> TREASURE_PALETTES = create("treasure");

        private WoodPaletteTagGenerator() {
        }

        private static TagKey<WoodPalette> create(String name) {
            return TagKey.create(TFRegistries.Keys.WOOD_PALETTES, TwilightForestMod.prefix(name));
        }
    }
}
