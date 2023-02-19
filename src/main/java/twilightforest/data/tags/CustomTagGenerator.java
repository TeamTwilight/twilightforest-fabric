package twilightforest.data.tags;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.data.PortingLibTagsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBannerPatterns;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.WoodPalette;

import java.util.concurrent.CompletableFuture;

//a place to hold all custom tags, since I imagine we wont have a lot of them
public class CustomTagGenerator {

	public static class EnchantmentTagGenerator extends FabricTagProvider.EnchantmentTagProvider {

		public static final TagKey<Enchantment> PHANTOM_ARMOR_BANNED_ENCHANTS = TagKey.create(Registries.ENCHANTMENT, TwilightForestMod.prefix("phantom_armor_banned_enchants"));

		public EnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
			super(output, provider);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(PHANTOM_ARMOR_BANNED_ENCHANTS).add(BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).get(), BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BINDING_CURSE).get());
		}

		@Override
		public String getName() {
			return "Twilight Forest Enchantment Tags";
		}
	}

	public static class BannerPatternTagGenerator extends FabricTagProvider<BannerPattern> {

		public static final TagKey<BannerPattern> NAGA_BANNER_PATTERN = create("pattern_item/naga");
		public static final TagKey<BannerPattern> LICH_BANNER_PATTERN = create("pattern_item/lich");
		public static final TagKey<BannerPattern> MINOSHROOM_BANNER_PATTERN = create("pattern_item/minoshroom");
		public static final TagKey<BannerPattern> HYDRA_BANNER_PATTERN = create("pattern_item/hydra");
		public static final TagKey<BannerPattern> KNIGHT_PHANTOM_BANNER_PATTERN = create("pattern_item/knight_phantom");
		public static final TagKey<BannerPattern> UR_GHAST_BANNER_PATTERN = create("pattern_item/ur_ghast");
		public static final TagKey<BannerPattern> ALPHA_YETI_BANNER_PATTERN = create("pattern_item/alpha_yeti");
		public static final TagKey<BannerPattern> SNOW_QUEEN_BANNER_PATTERN = create("pattern_item/snow_queen");
		public static final TagKey<BannerPattern> QUEST_RAM_BANNER_PATTERN = create("pattern_item/quest_ram");

		public BannerPatternTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
			super(output, Registries.BANNER_PATTERN, provider);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(NAGA_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.NAGA.get()).get());
			tag(LICH_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.LICH.get()).get());
			tag(MINOSHROOM_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.MINOSHROOM.get()).get());
			tag(HYDRA_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.HYDRA.get()).get());
			tag(KNIGHT_PHANTOM_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.KNIGHT_PHANTOM.get()).get());
			tag(UR_GHAST_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.UR_GHAST.get()).get());
			tag(ALPHA_YETI_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.ALPHA_YETI.get()).get());
			tag(SNOW_QUEEN_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.SNOW_QUEEN.get()).get());
			tag(QUEST_RAM_BANNER_PATTERN).add(BuiltInRegistries.BANNER_PATTERN.getResourceKey(TFBannerPatterns.QUEST_RAM.get()).get());
		}

		private static TagKey<BannerPattern> create(String name) {
			return TagKey.create(Registries.BANNER_PATTERN, TwilightForestMod.prefix(name));
		}

		@Override
		public String getName() {
			return "Twilight Forest Banner Pattern Tags";
		}
	}

	public static class WoodPaletteTagGenerator extends PortingLibTagsProvider<WoodPalette> {
		public static final TagKey<WoodPalette> WELL_SWIZZLE_MASK = WoodPalettes.WOOD_PALETTES.createTagKey(TwilightForestMod.prefix("well_swizzle_mask"));
		public static final TagKey<WoodPalette> DRUID_HUT_SWIZZLE_MASK = WoodPalettes.WOOD_PALETTES.createTagKey(TwilightForestMod.prefix("druid_hut_swizzle_mask"));
		public static final TagKey<WoodPalette> COMMON_PALETTES = WoodPalettes.WOOD_PALETTES.createTagKey(TwilightForestMod.prefix("common"));
		public static final TagKey<WoodPalette> UNCOMMON_PALETTES = WoodPalettes.WOOD_PALETTES.createTagKey(TwilightForestMod.prefix("uncommon"));
		public static final TagKey<WoodPalette> RARE_PALETTES = WoodPalettes.WOOD_PALETTES.createTagKey(TwilightForestMod.prefix("rare"));
		public static final TagKey<WoodPalette> TREASURE_PALETTES = WoodPalettes.WOOD_PALETTES.createTagKey(TwilightForestMod.prefix("treasure"));

		public WoodPaletteTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
			super(output, WoodPalettes.WOOD_PALETTE_TYPE_KEY, provider, helper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(WELL_SWIZZLE_MASK).add(WoodPalettes.OAK);
			tag(DRUID_HUT_SWIZZLE_MASK).add(WoodPalettes.OAK, WoodPalettes.SPRUCE, WoodPalettes.BIRCH);

			tag(COMMON_PALETTES).add(WoodPalettes.SPRUCE, WoodPalettes.CANOPY);
			tag(UNCOMMON_PALETTES).add(WoodPalettes.OAK, WoodPalettes.DARKWOOD, WoodPalettes.TWILIGHT_OAK);
			tag(RARE_PALETTES).add(WoodPalettes.BIRCH, WoodPalettes.JUNGLE, WoodPalettes.MANGROVE);
			tag(TREASURE_PALETTES).add(WoodPalettes.TIMEWOOD, WoodPalettes.TRANSWOOD, WoodPalettes.MINEWOOD, WoodPalettes.SORTWOOD);
		}

		@Override
		public String getName() {
			return "Twilight Forest Wood Palette Tags";
		}
	}
}
