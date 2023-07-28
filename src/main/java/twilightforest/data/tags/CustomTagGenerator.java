package twilightforest.data.tags;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.data.PortingLibTagsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBannerPatterns;
import twilightforest.init.TFBlockEntities;
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

	public static class BlockEntityTagGenerator extends TagsProvider<BlockEntityType<?>> {

		public static final TagKey<BlockEntityType<?>> RELOCATION_NOT_SUPPORTED = TagKey.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("c", "relocation_not_supported"));
		public static final TagKey<BlockEntityType<?>> IMMOVABLE = TagKey.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("c", "immovable"));

		public BlockEntityTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
			super(output, Registries.BLOCK_ENTITY_TYPE, provider);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(RELOCATION_NOT_SUPPORTED).add(
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.ANTIBUILDER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.BEANSTALK_GROWER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.NAGA_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.LICH_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.MINOSHROOM_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.HYDRA_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.UR_GHAST_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.ALPHA_YETI_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.SNOW_QUEEN_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.FINAL_BOSS_SPAWNER.get()).get());

			this.tag(IMMOVABLE).add(
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.ANTIBUILDER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.BEANSTALK_GROWER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.NAGA_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.LICH_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.MINOSHROOM_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.HYDRA_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.UR_GHAST_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.ALPHA_YETI_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.SNOW_QUEEN_SPAWNER.get()).get(),
					BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(TFBlockEntities.FINAL_BOSS_SPAWNER.get()).get());
		}

		@Override
		public String getName() {
			return "Twilight Forest Block Entity Tags";
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
			tag(NAGA_BANNER_PATTERN).add(TFBannerPatterns.NAGA.getKey());
			tag(LICH_BANNER_PATTERN).add(TFBannerPatterns.LICH.getKey());
			tag(MINOSHROOM_BANNER_PATTERN).add(TFBannerPatterns.MINOSHROOM.getKey());
			tag(HYDRA_BANNER_PATTERN).add(TFBannerPatterns.HYDRA.getKey());
			tag(KNIGHT_PHANTOM_BANNER_PATTERN).add(TFBannerPatterns.KNIGHT_PHANTOM.getKey());
			tag(UR_GHAST_BANNER_PATTERN).add(TFBannerPatterns.UR_GHAST.getKey());
			tag(ALPHA_YETI_BANNER_PATTERN).add(TFBannerPatterns.ALPHA_YETI.getKey());
			tag(SNOW_QUEEN_BANNER_PATTERN).add(TFBannerPatterns.SNOW_QUEEN.getKey());
			tag(QUEST_RAM_BANNER_PATTERN).add(TFBannerPatterns.QUEST_RAM.getKey());
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
