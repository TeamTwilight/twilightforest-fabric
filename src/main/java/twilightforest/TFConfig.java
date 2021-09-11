package twilightforest;

import com.google.common.collect.ImmutableList;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import twilightforest.world.components.feature.TFGenCaveStalactite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TFConfig {

	public static Common COMMON_CONFIG = new Common(ConfigBuilder.create());
	public static Client CLIENT_CONFIG = new Client();

	public static class Common {

		public static class Buider {
			private ConfigEntryBuilder builder;
			private String translationKey;
			private String desc;

			public Buider(ConfigEntryBuilder configBuilder) {
				this.builder = configBuilder;
			}

			public Buider translation(String lang) {
				this.translationKey = translationKey;
				return this;
			}

			public Buider comment(String desc) {
				this.desc = desc;
				return this;
			}

			public void define(String name, boolean bool, ConfigCategory category, Boolean ref) {
				this.desc = desc;
				category.addEntry(builder.startBooleanToggle(new TranslatableComponent(translationKey), ref).setDefaultValue(bool).setTooltip(new TextComponent(desc)).build());
			}
		}

		public Common(ConfigBuilder configBuilder) {
			ConfigCategory temp = configBuilder.getOrCreateCategory(new TextComponent("Dimension Settings"));
			ConfigEntryBuilder thing = configBuilder.entryBuilder();
			Buider buider = new Buider(thing);
			{
//				temp.addEntry(thing.startBooleanToggle(new TranslatableComponent(config + "spawn_in_tf"), DIMENSION.newPlayersSpawnInTF)
//						.setTooltip(new TextComponent("If true, players spawning for the first time will spawn in the Twilight Forest."))
//						.setDefaultValue(false)
//						.build());
//				temp.addEntry(thing.startBooleanToggle(new TranslatableComponent(config + "skylight_forest"), DIMENSION.skylightForest)
//						.setTooltip(new TextComponent("If true, Twilight Forest will generate as a void except for Major Structures"))
//						.setDefaultValue(false)
//						.build());
//				temp.addEntry(thing.startBooleanToggle(new TranslatableComponent(config + "skylight_oaks"), DIMENSION.skylightOaks)
//						.setTooltip(new TextComponent("If true, giant Twilight Oaks will also spawn in void worlds"))
//						.setDefaultValue(true)
//						.build());
				//temp.addEntry(builder.startStrList())
				DIMENSION.portalDestinationID = "twilightforest:twilight_forest";
//				DIMENSION.portalDestinationID = builder.
//						translation(config + "portal_destination_id").
//						worldRestart().
//						comment("Marked dimension ID for Twilight Portals and some other Twilight mod logic as well").
//						define("portalDestinationID", "twilightforest:twilight_forest");
//				ConfigCategory cat = configBuilder.getOrCreateCategory(new TextComponent("Custom Hollow Hill Stalactites"));
//						cat.setDescription((Supplier<Optional<FormattedText[]>>) new TextComponent("""
//								Defines custom stalactites generated in hollow hills.
//								Format is "modid:block size maxLength minHeight weight", where the properties are:
//								Size - the maximum length of the stalactite relative to the space between hill floor and ceiling,
//								Max length - maximum length of a stalactite in blocks,
//								Min height - minimum space between the hill floor and the stalactite to let it generate,
//								Weight - how often it generates.
//
//								For example: "minecraft:iron_ore 0.7 8 1 24" would add a stalactite equal to the default iron ore stalactite."""));
//
				{
//					cat.addEntry(thing.startStrList(new TranslatableComponent(config + "large_hill"), DIMENSION.hollowHillStalactites.largeHill)
//							.setTooltip(new TextComponent("Blocks generating as stalactites in large hills only"))
//							.setDefaultValue(new ArrayList<>())
//							.build());
//					cat.addEntry(thing.startStrList(new TranslatableComponent(config + "medium_hill"), DIMENSION.hollowHillStalactites.mediumHill)
//							.setTooltip(new TextComponent("Blocks generating as stalactites in medium and large hills"))
//							.setDefaultValue(new ArrayList<>())
//							.build());
//					cat.addEntry(thing.startStrList(new TranslatableComponent(config + "small_hill"), DIMENSION.hollowHillStalactites.smallHill)
//							.setTooltip(new TextComponent("Blocks generating as stalactites in all hills"))
//							.setDefaultValue(new ArrayList<>())
//							.build());
//					cat.addEntry(thing.startBooleanToggle(new TranslatableComponent(config + "stalactite_config_only"), DIMENSION.hollowHillStalactites.useConfigOnly)
//							.setTooltip(new TextComponent("If true, default stalactites and stalactites defined by other mods will not be used."))
//							.setDefaultValue(false)
//							.build());
				}
			}
//			ConfigCategory misc = configBuilder.getOrCreateCategory(new TextComponent("Misc"));
//			misc.addEntry(thing.startBooleanToggle(new TextComponent("doCompat"), doCompat)
//					.setTooltip(new TextComponent("Should TF Compatibility load? Turn off if TF's Compatibility is causing crashes or if not desired."))
//					.setDefaultValue(true)
//					.build());
//			ConfigCategory pre = configBuilder.getOrCreateCategory(new TextComponent("Performance Tweaks"));
//			pre.setDescription((FormattedText[]) List.of(new TextComponent("Lets you sacrifice various things to improve world performance.")).toArray());
			{
//				PERFORMANCE.canopyCoverage = builder.
//						translation(config + "canopy_coverage").
//						comment("Amount of canopy coverage. Lower numbers improve chunk generation speed at the cost of a thinner forest.").
//						defineInRange("canopyCoverage", 1.7F, 0, Double.MAX_VALUE);
//				PERFORMANCE.twilightOakChance = builder.
//						translation(config + "twilight_oaks").
//						comment("Chance that a chunk in the Twilight Forest will contain a twilight oak tree. Higher numbers reduce the number of trees, increasing performance.").
//						defineInRange("twilightOakChance", 48, 1, Integer.MAX_VALUE);
//				PERFORMANCE.leavesLightOpacity = builder.
//						translation(config + "leaves_light_opacity").
//						comment("This controls the opacity of leaves, changing the amount of light blocked. Can be used to decrease complexity in some lighting checks.").
//						defineInRange("leavesLightOpacity", 1, 0, 255);
//				PERFORMANCE.glacierPackedIce = builder.
//						translation(config + "glacier_packed_ice").
//						comment("Setting this true will make Twilight Glaciers generate with Packed Ice instead of regular translucent Ice, decreasing amount of light checking calculations.").
//						define("glacierPackedIce", false);
//				PERFORMANCE.enableSkylight = builder.
//						translation(config + "enable_skylight").
//						comment("If the dimension has per-block skylight values. Disabling this will significantly improve world generation performance, at the cost of flat lighting everywhere." +
//
//								"\nWARNING: Once chunks are loaded without skylight, that data is lost and cannot easily be regenerated. Be careful!").
//						worldRestart().
//						define("enableSkylight", true);
			}
//			builder.pop();
//			originDimension = builder.
//					translation(config + "origin_dimension").
//					comment("The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).").
//					define("originDimension", "minecraft:overworld");
//			allowPortalsInOtherDimensions = builder.
//					translation(config + "portals_in_other_dimensions").
//					comment("Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.").
//					define("allowPortalsInOtherDimensions", false);
//			adminOnlyPortals = builder.
//					translation(config + "admin_portals").
//					comment("Allow portals only for admins (Operators). This severely reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.").
//					define("adminOnlyPortals", false);
//			disablePortalCreation = builder.
//					translation(config + "portals").
//					comment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.").
//					define("disablePortalCreation", false);
//			checkPortalDestination = builder.
//					translation(config + "check_portal_destination").
//					comment("Determines if new portals should be pre-checked for safety. If enabled, portals will fail to form rather than redirect to a safe alternate destination." +
//
//							"\nNote that enabling this also reduces the rate at which portal formation checks are performed.").
//					define("checkPortalDestination", false);
//			portalLightning = builder.
//					translation(config + "portal_lighting").
//					comment("Set this true if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.").
//					define("portalLightning", false);
//			shouldReturnPortalBeUsable = builder.
//					translation(config + "portal_return").
//					comment("If false, the return portal will require the activation item.").
//					define("shouldReturnPortalBeUsable", true);
//			progressionRuleDefault = builder.
//					translation(config + "progression_default").
//					comment("Sets the default value of the game rule controlling enforced progression.").
//					define("progressionRuleDefault", true);
//			disableUncrafting = builder.
//					worldRestart().
//					translation(config + "uncrafting").
//					comment("Disable the uncrafting function of the uncrafting table. Provided as an option when interaction with other mods produces exploitable recipes.").
//					define("disableUncrafting", false);
//			casketUUIDLocking = builder.
//					worldRestart().
//					translation(config + "casket_uuid_locking").
//					comment("If true, Keepsake Caskets that are spawned when a player dies will not be accessible by other players. Use this if you dont want people taking from other people's death caskets. NOTE: server operators will still be able to open locked caskets.")
//					.define("uuid_locking", false);
//			builder.
//					comment("We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.").
//					push("Shield Parrying");
			{
//				SHIELD_INTERACTIONS.parryNonTwilightAttacks = builder.
//						translation(config + "parry_non_twilight").
//						comment("Set to true to parry non-Twilight projectiles.").
//						define("parryNonTwilightAttacks", false);
//				SHIELD_INTERACTIONS.shieldParryTicksArrow = builder.
//						translation(config + "parry_window_arrow").
//						comment("The amount of ticks after raising a shield that makes it OK to parry an arrow.").
//						defineInRange("shieldParryTicksArrow", 40, 0, Integer.MAX_VALUE);
//				SHIELD_INTERACTIONS.shieldParryTicksFireball = builder.
//						translation(config + "parry_window_fireball").
//						comment("The amount of ticks after raising a shield that makes it OK to parry a fireball.").
//						defineInRange("shieldParryTicksFireball", 40, 0, Integer.MAX_VALUE);
//				SHIELD_INTERACTIONS.shieldParryTicksThrowable = builder.
//						translation(config + "parry_window_throwable").
//						comment("The amount of ticks after raising a shield that makes it OK to parry a thrown item.").
//						defineInRange("shieldParryTicksThrowable", 40, 0, Integer.MAX_VALUE);
//				SHIELD_INTERACTIONS.shieldParryTicksBeam = builder.
//						translation(config + "parry_window_beam").
//						defineInRange("shieldParryTicksBeam", 10, 0, Integer.MAX_VALUE);
			}
			//builder.pop();
		}

		public Dimension DIMENSION = new Dimension();

		public static class Dimension {

			public boolean newPlayersSpawnInTF;
			public boolean skylightForest;
			public boolean skylightOaks;

			// Find a different way to validate if a world is passible as a "Twilight Forest" instead of hardcoding Dim ID (Instanceof check for example) before strictly using this
			// Reason this is needed is so users can reconfig portals to use Skylight Forest or a Void Forest or another dimension entirely
			public String portalDestinationID;

			public HollowHillStalactites hollowHillStalactites = new HollowHillStalactites();

			public static class HollowHillStalactites {

				public List<String> largeHill = new ArrayList<>();
				public List<String> mediumHill = new ArrayList<>();
				public List<String> smallHill = new ArrayList<>();
				public boolean useConfigOnly;

				public void load() {
					registerHill(smallHill, 1);
					registerHill(mediumHill, 2);
					registerHill(largeHill, 3);
				}

				private void registerHill(List<? extends String> definitions, int tier) {
					for (String definition : definitions) {
						if (!parseStalactite(definition, tier)) {
							TwilightForestMod.LOGGER.warn("Invalid hollow hill stalactite definition: {}", definition);
						}
					}
				}

				private boolean parseStalactite(String definition, int tier) {
					String[] split = definition.split(" ");
					if (split.length != 5) return false;

					Optional<Block> block = parseBlock(split[0]);
					if (!block.isPresent()) return false;

					try {
						TFGenCaveStalactite.registerStalactite(tier, block.get().defaultBlockState(),
								Float.parseFloat(split[1]),
								Integer.parseInt(split[2]),
								Integer.parseInt(split[3]),
								Integer.parseInt(split[4])
						);
					} catch (NumberFormatException e) {
						return false;
					}
					return true;
				}
			}
		}

		public boolean doCompat;

		public Performance PERFORMANCE = new Performance();

		public static class Performance {
			public double canopyCoverage;
			public int twilightOakChance;
			public int leavesLightOpacity;
			public boolean glacierPackedIce;
			public boolean enableSkylight;

			public boolean shadersSupported = true;
		}

		public String originDimension = "minecraft:overworld";
		public boolean allowPortalsInOtherDimensions;
		public boolean adminOnlyPortals;
		public boolean disablePortalCreation;
		public boolean checkPortalDestination;
		public boolean portalLightning;
		public boolean shouldReturnPortalBeUsable;
		public boolean progressionRuleDefault;
		public boolean disableUncrafting;
		public boolean casketUUIDLocking;

		public ShieldInteractions SHIELD_INTERACTIONS = new ShieldInteractions();

		public static class ShieldInteractions {

			public boolean parryNonTwilightAttacks;
			public int shieldParryTicksArrow;
			public int shieldParryTicksFireball;
			public int shieldParryTicksThrowable;
			public int shieldParryTicksBeam;
		}

	}

	public static class Client {

		public Client() {
//			silentCicadas = builder.
//					translation(config + "silent_cicadas").
//					comment("Make cicadas silent for those having sound library problems, or otherwise finding them annoying.").
//					define("silentCicadas", false);
//			firstPersonEffects = builder.
//					translation(config + "first_person_effects").
//					comment("Controls whether various effects from the mod are rendered while in first-person view. Turn this off if you find them distracting.").
//					define("firstPersonEffects", true);
//			rotateTrophyHeadsGui = builder.
//					translation(config + "animate_trophyitem").
//					comment("Rotate trophy heads on item model. Has no performance impact at all. For those who don't like fun.").
//					define("rotateTrophyHeadsGui", true);
//			disableOptifineNagScreen = builder.
//					translation(config + "optifine").
//					comment("Disable the nag screen when Optifine is installed.").
//					define("disableOptifineNagScreen", false);
//			disableHereBeDragons = builder.
//					translation(config + "dragons").
//					comment("Disable the Here Be Dragons experimental warning screen.").
//					define("disableHereBeDragons", false);
//			builder.
//					comment("Client only: Controls for the Loading screen").
//					push("Loading Screen");
//			{
//				LOADING_SCREEN.enable = builder.
//						translation(config + "loading_icon_enable").
//						comment("Wobble the Loading icon. Has no performance impact at all. For those who don't like fun.").
//						define("enable", true);
//				LOADING_SCREEN.cycleLoadingScreenFrequency = builder.
//						translation(config + "loading_screen_swap_frequency").
//						comment("How many ticks between each loading screen change. Set to 0 to not cycle at all.").
//						defineInRange("cycleLoadingScreenFrequency", 0, 0, Integer.MAX_VALUE);
//				LOADING_SCREEN.frequency = builder.
//						translation(config + "loading_icon_wobble_bounce_frequency").
//						comment("Frequency of wobble and bounce.").
//						defineInRange("frequency", 5F, 0F, Double.MAX_VALUE);
//				LOADING_SCREEN.scale = builder.
//						translation(config + "loading_icon_scale").
//						comment("Scale of whole bouncy loading icon.").
//						defineInRange("scale", 3F, 0F, Double.MAX_VALUE);
//				LOADING_SCREEN.scaleDeviation = builder.
//						translation(config + "loading_icon_bounciness").
//						comment("How much the loading icon bounces.").
//						defineInRange("scaleDeviation", 5.25F, 0F, Double.MAX_VALUE);
//				LOADING_SCREEN.tiltRange = builder.
//						translation(config + "loading_icon_tilting").
//						comment("How far the loading icon wobbles.").
//						defineInRange("tiltRange", 11.25F, 0F, 360F);
//				LOADING_SCREEN.tiltConstant = builder.
//						translation(config + "loading_icon_tilt_pushback").
//						comment("Pushback value to re-center the wobble of loading icon.").
//						defineInRange("tiltConstant", 22.5F, 0F, 360F);
//				LOADING_SCREEN.loadingIconStacks = builder.
//						translation(config + "loading_icon_stacks").
//						comment("List of items to be used for the wobbling Loading Icon. (domain:item).").
//						defineList("loadingIconStacks", Arrays.asList(
//								"twilightforest:experiment_115",
//								"twilightforest:magic_map",
//								"twilightforest:charm_of_life_2",
//								"twilightforest:charm_of_keeping_3",
//								"twilightforest:phantom_helmet",
//								"twilightforest:lamp_of_cinders",
//								"twilightforest:carminite",
//								"twilightforest:block_and_chain",
//								"twilightforest:yeti_helmet",
//								"twilightforest:hydra_chop",
//								"twilightforest:magic_beans",
//								"twilightforest:ironwood_raw",
//								"twilightforest:naga_scale",
//								"twilightforest:experiment_115{think:1}",
//								"twilightforest:twilight_portal_miniature_structure",
//								"twilightforest:lich_tower_miniature_structure",
//								"twilightforest:knightmetal_block",
//								"twilightforest:ghast_trap",
//								"twilightforest:time_sapling",
//								"twilightforest:transformation_sapling",
//								"twilightforest:mining_sapling",
//								"twilightforest:sorting_sapling",
//								"twilightforest:rainboak_sapling",
//								"twilightforest:borer_essence"
//						), s -> s instanceof String && ResourceLocation.tryParse((String) s) != null);
//			}
//			builder.pop();
		}

		public boolean silentCicadas = false;
		public boolean firstPersonEffects;
		public boolean rotateTrophyHeadsGui = true;
		public boolean disableOptifineNagScreen = false;
		public boolean disableHereBeDragons = false;

		public final LoadingScreen LOADING_SCREEN = new LoadingScreen();

		public static class LoadingScreen {

			public boolean enable;
			public int cycleLoadingScreenFrequency;
			public double frequency;
			public double scale;
			public double scaleDeviation;
			public double tiltRange;
			public double tiltConstant;
			public List<? extends String> loadingIconStacks;

			private ImmutableList<ItemStack> loadingScreenIcons;

			private ImmutableList<ItemStack> getItems(String ...itemList) {
				ImmutableList.Builder<ItemStack> items = new ImmutableList.Builder<>();
				Arrays.stream(itemList).toList().forEach((item) -> items.add(new ItemStack(Registry.ITEM.get(new ResourceLocation(item)))));
				return items.build();
			}

			public ImmutableList<ItemStack> getLoadingScreenIcons() {
				return getItems("twilightforest:experiment_115", "twilightforest:magic_map", "twilightforest:charm_of_life_2", "twilightforest:charm_of_keeping_3", "twilightforest:phantom_helmet", "twilightforest:lamp_of_cinders", "twilightforest:carminite", "twilightforest:block_and_chain", "twilightforest:yeti_helmet", "twilightforest:hydra_chop", "twilightforest:magic_beans", "twilightforest:ironwood_raw", "twilightforest:naga_scale", "twilightforest:twilight_portal_miniature_structure", "twilightforest:lich_tower_miniature_structure", "twilightforest:knightmetal_block", "twilightforest:ghast_trap", "twilightforest:time_sapling", "twilightforest:transformation_sapling", "twilightforest:mining_sapling", "twilightforest:sorting_sapling", "twilightforest:rainboak_sapling", "twilightforest:borer_essence");//loadingScreenIcons;
			}

			void loadLoadingScreenIcons() {
				ImmutableList.Builder<ItemStack> iconList = ImmutableList.builder();

				iconList.addAll(IMCHandler.getLoadingIconStacks());

//				for (String s : loadingIconStacks) {
//					parseItemStack(s).ifPresent(iconList::add);
//				}

				//loadingScreenIcons = iconList.build();
			}
		}

	}

	private static final String config = TwilightForestMod.ID + ".config.";

	/*@SubscribeEvent //FIXME Replace
	public static void onConfigChanged(ModConfig.Reloading event) {
		if (event.getConfig().getModId().equals(TwilightForestMod.ID)) {
//			TFDimensions.checkOriginDimension();
			build();
		}
	}*/

	public static void build() {
		CLIENT_CONFIG.LOADING_SCREEN.loadLoadingScreenIcons();
	}

	private static Optional<ItemStack> parseItemStack(String string) {
		ResourceLocation id = ResourceLocation.tryParse(string);
		if (id == null || !Registry.ITEM.containsKey(id)) {
			return Optional.empty();
		} else {
			return Optional.of(new ItemStack(Registry.ITEM.get(id)));
		}
	}

	private static Optional<Block> parseBlock(String string) {
		ResourceLocation id = ResourceLocation.tryParse(string);
		if (id == null || !Registry.BLOCK.containsKey(id)) {
			return Optional.empty();
		} else {
			return Optional.of(Registry.BLOCK.get(id));
		}
	}
}
