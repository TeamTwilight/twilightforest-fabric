package twilightforest.data.helpers;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class TFLangProvider implements DataProvider {
	private final Map<String, String> TF_TIPS = new HashMap<>();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final PackOutput output;
	public final Map<String, String> upsideDownEntries = new HashMap<>();
	private final Map<String, String> data = new TreeMap<>();
	private final CompletableFuture<HolderLookup.Provider> registries;

	public TFLangProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.output = output;
		this.registries = registries;
	}

	public void add(String key, String value) {
		if (this.data.put(key, value) != null) {
			throw new IllegalStateException("Duplicate translation key " + key);
		}
		List<LangFormatSplitter.Component> splitEnglish = LangFormatSplitter.split(value);
		this.upsideDownEntries.put(key, LangConversionHelper.convertComponents(splitEnglish));
	}

	public void addAttribute(Supplier<? extends Attribute> attribute, String name) {
		this.add(attribute.get().getDescriptionId(), name);
	}

	public void addBiome(ResourceKey<Biome> biome, String name) {
		this.add("biome.twilightforest." + biome.location().getPath(), name);
	}

	public void addSapling(String woodPrefix, String saplingName) {
		this.add("block.twilightforest." + woodPrefix + "_sapling", saplingName);
		this.add("block.twilightforest.potted_" + woodPrefix + "_sapling", "Potted " + saplingName);
	}

	public void createLogs(String woodPrefix, String woodName) {
		this.add("block.twilightforest." + woodPrefix + "_log", woodName + " Log");
		this.add("block.twilightforest." + woodPrefix + "_wood", woodName + " Wood");
		this.add("block.twilightforest.stripped_" + woodPrefix + "_log", "Stripped " + woodName + " Log");
		this.add("block.twilightforest.stripped_" + woodPrefix + "_wood", "Stripped " + woodName + " Wood");
		this.createHollowLogs(woodPrefix, woodName, false);
	}

	public void createHollowLogs(String woodPrefix, String woodName, boolean stem) {
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_horizontal", "Hollow " + woodName + (stem ? " Stem" : " Log"));
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_vertical", "Hollow " + woodName + (stem ? " Stem" : " Log"));
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_climbable", "Hollow " + woodName + (stem ? " Stem" : " Log"));
	}

	public void createWoodSet(String woodPrefix, String woodName) {
		this.add("block.twilightforest." + woodPrefix + "_planks", woodName + " Planks");
		this.add("block.twilightforest." + woodPrefix + "_slab", woodName + " Slab");
		this.add("block.twilightforest." + woodPrefix + "_stairs", woodName + " Stairs");
		this.add("block.twilightforest." + woodPrefix + "_button", woodName + " Button");
		this.add("block.twilightforest." + woodPrefix + "_fence", woodName + " Fence");
		this.add("block.twilightforest." + woodPrefix + "_fence_gate", woodName + " Fence Gate");
		this.add("block.twilightforest." + woodPrefix + "_pressure_plate", woodName + " Pressure Plate");
		this.add("block.twilightforest." + woodPrefix + "_trapdoor", woodName + " Trapdoor");
		this.add("block.twilightforest." + woodPrefix + "_door", woodName + " Door");
		this.add("block.twilightforest." + woodPrefix + "_sign", woodName + " Sign");
		this.add("block.twilightforest." + woodPrefix + "_wall_sign", woodName + " Wall Sign");
		this.add("block.twilightforest." + woodPrefix + "_banister", woodName + " Banister");
		this.add("block.twilightforest." + woodPrefix + "_chest", woodName + " Chest");
		this.add("block.twilightforest." + woodPrefix + "_trapped_chest", "Trapped " + woodName + " Chest");
		this.add("item.twilightforest." + woodPrefix + "_boat", woodName + " Boat");
		this.add("item.twilightforest." + woodPrefix + "_chest_boat", woodName + " Boat with Chest");
		this.add("block.twilightforest." + woodPrefix + "_hanging_sign", woodName + " Hanging Sign");
		this.add("block.twilightforest." + woodPrefix + "_wall_hanging_sign", woodName + " Wall Hanging Sign");
		this.add("block.twilightforest." + woodPrefix + "_drying_rack", woodName + " Drying Rack");
	}

	public void addBannerPattern(String patternPrefix, String patternName) {
		this.add("item.twilightforest." + patternPrefix + "_banner_pattern", "Banner Pattern");
		this.add("item.twilightforest." + patternPrefix + "_banner_pattern.desc", patternName);
		for (DyeColor color : DyeColor.values()) {
			this.add("block.minecraft.banner.twilightforest." + patternPrefix + "." + color.getName(), capitalize(color.getName().replace('_', ' ')) + " " + patternName);
		}
	}

	public <T extends GameRules.Value<T>> void addGameRule(Supplier<GameRules.Key<T>> gameRule, String gameRuleName) {
		this.add("gamerule." + gameRule.get().getId(), gameRuleName);
	}

	public <T extends GameRules.Value<T>> void addGameRuleDescription(Supplier<GameRules.Key<T>> gameRule, String gameRuleDescription) {
		this.add("gamerule." + gameRule.get().getId() + ".description", gameRuleDescription);
	}

	public void addBlock(Supplier<? extends ItemLike> block, String name) {
		this.add(block.get().asItem().getDescriptionId(), name);
	}

	public void add(Block block, String name) {
		this.add(block.asItem().getDescriptionId(), name);
	}

	public void addItem(Supplier<? extends Item> item, String name) {
		this.add(item.get().getDescriptionId(), name);
	}

	public void add(Item item, String name) {
		this.add(item.getDescriptionId(), name);
	}

	public void addEntityType(Supplier<? extends EntityType<?>> entity, String name) {
		this.add(entity.get().getDescriptionId(), name);
	}

	public void add(EntityType<?> entity, String name) {
		this.add(entity.getDescriptionId(), name);
	}

	public void addStoneVariants(String blockKey, String blockName) {
		this.add("block.twilightforest." + blockKey, blockName);
		this.add("block.twilightforest.cracked_" + blockKey, "Cracked " + blockName);
		this.add("block.twilightforest.mossy_" + blockKey, "Mossy " + blockName);
	}

	public void addArmor(String itemKey, String item) {
		this.add("item.twilightforest." + itemKey + "_helmet", item + " Helmet");
		this.add("item.twilightforest." + itemKey + "_chestplate", item + " Chestplate");
		this.add("item.twilightforest." + itemKey + "_leggings", item + " Leggings");
		this.add("item.twilightforest." + itemKey + "_boots", item + " Boots");
	}

	public void addTools(String itemKey, String item) {
		this.add("item.twilightforest." + itemKey + "_sword", item + " Sword");
		this.add("item.twilightforest." + itemKey + "_pickaxe", item + " Pickaxe");
		this.add("item.twilightforest." + itemKey + "_axe", item + " Axe");
		this.add("item.twilightforest." + itemKey + "_shovel", item + " Shovel");
		this.add("item.twilightforest." + itemKey + "_hoe", item + " Hoe");
	}

	public void addMusicDisc(Supplier<? extends Item> disc, String description) {
		this.addItem(disc, "Music Disc");
		Item item = disc.get();
		if (item.components().has(DataComponents.JUKEBOX_PLAYABLE)) {
			this.add(Util.makeDescriptionId("jukebox_song", item.components().get(DataComponents.JUKEBOX_PLAYABLE).song().key().location()), description);
		}
	}

	public void addStructure(ResourceKey<Structure> structure, String name) {
		this.add("structure.twilightforest." + structure.location().getPath(), name);
	}

	public void addAdvancement(String key, String title, String desc) {
		this.add("advancement.twilightforest." + key, title);
		this.add("advancement.twilightforest." + key + ".desc", desc);
	}

	public void addEnchantment(String key, String title, String desc) {
		this.add("enchantment.twilightforest." + key, title);
		this.add("enchantment.twilightforest." + key + ".desc", desc);
	}

	public void addEntityAndEgg(Supplier<? extends EntityType<?>> entity, String name) {
		this.addEntityType(entity, name);
		this.add("item.twilightforest." + BuiltInRegistries.ENTITY_TYPE.getKey(entity.get()).getPath() + "_spawn_egg", name + " Spawn Egg");
	}

	public void addDeathMessage(String key, String name) {
		this.add("death.attack.twilightforest." + key, name);
	}

	public void addStat(String key, String name) {
		this.add("stat.twilightforest." + key, name);
	}

	public void addMessage(String key, String name) {
		this.add("misc.twilightforest." + key, name);
	}

	public void addCommand(String key, String name) {
		this.add("commands.tffeature." + key, name);
	}

	public void addTrim(String key, String name) {
		this.add("trim_material.twilightforest." + key, name + " Material");
	}

	public void addBookAndContents(String bookKey, String bookTitle, String... pages) {
		this.add("twilightforest.book." + bookKey, bookTitle);
		for (int i = 0; i < pages.length; i++) {
			this.add("twilightforest.book." + bookKey + "." + (i + 1), pages[i]);
		}
	}

	public void addScreenMessage(String key, String name) {
		this.add("gui.twilightforest." + key, name);
	}

	public void addKeyBindCategory(Object category, String name) {
		this.add(invokeString(category, "internalName"), name);
	}

	public void addKeyMapping(Object keyMapping, String name) {
		this.add(invokeString(keyMapping, "getName"), name);
	}

	public void addTravellersModifier(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifier, String name) {
		this.add(modifier.location().toLanguageKey(registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).getOrThrow(modifier).value().getPrefix()), name);
	}

	public void addTravellersDescription(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifier, String description) {
		this.add(modifier.location().toLanguageKey(registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).getOrThrow(modifier).value().getPrefix(), "description"), description);
	}

	public void createTip(String key, String translation) {
		String fullKey = "twilightforest.tips." + key;
		this.add(fullKey, translation);
		this.TF_TIPS.put(fullKey, key);
	}

	public void translateTag(TagKey<?> tag, String name) {
		this.add(String.format("tag.%s.%s.%s", tag.registry().location().getPath(), tag.location().getNamespace(), tag.location().getPath().replace('/', '.')), name);
	}

	public void configEntry(String key, String name, String description) {
		this.configEntry(key, name, description, null);
	}

	public void configEntry(String key, String name, String description, @Nullable String button) {
		this.add(TFConfig.CONFIG_ID + key, name);
		this.add(TFConfig.CONFIG_ID + key + ".tooltip", description);
		if (button != null) {
			this.add(TFConfig.CONFIG_ID + key + ".button", button);
		}
	}

	public void configCategory(String key, String name, String description) {
		this.add(TFConfig.CONFIG_ID + key, name);
		this.add(TFConfig.CONFIG_ID + key + ".tooltip", description);
	}

	protected abstract void addTranslations(HolderLookup.Provider registries);

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		CompletableFuture<?> languageGen = this.registries.thenCompose(provider -> {
			this.addTranslations(provider);
			if (!this.data.isEmpty()) {
				return this.save(cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(TwilightForestMod.ID).resolve("lang").resolve("en_us.json"));
			}
			return CompletableFuture.completedFuture(null);
		});

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
		futuresBuilder.add(languageGen);

		JsonObject upsideDownFile = new JsonObject();
		this.upsideDownEntries.forEach(upsideDownFile::addProperty);
		futuresBuilder.add(DataProvider.saveStable(cache, upsideDownFile, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(TwilightForestMod.ID).resolve("lang").resolve("en_ud.json")));

		for (Map.Entry<String, String> entry : this.TF_TIPS.entrySet()) {
			JsonObject object = new JsonObject();
			object.addProperty("type", "tipsmod:simple");
			Component tooltipText = Component.translatable(entry.getKey()).withStyle(ChatFormatting.GREEN);
			object.add("text", ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, tooltipText).getOrThrow());
			futuresBuilder.add(DataProvider.saveStable(cache, GSON.toJsonTree(object), this.output.getOutputFolder().resolve("assets/twilightforest/tips/" + entry.getValue() + ".json")));
		}
		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	private CompletableFuture<?> save(CachedOutput cache, Path target) {
		JsonObject json = new JsonObject();
		this.data.forEach(json::addProperty);
		return DataProvider.saveStable(cache, json, target);
	}

	@Override
	public String getName() {
		return "Twilight Forest Languages";
	}

	private static String invokeString(Object target, String methodName) {
		try {
			Method method = target.getClass().getMethod(methodName);
			return String.valueOf(method.invoke(target));
		} catch (ReflectiveOperationException exception) {
			throw new IllegalArgumentException("Unable to read " + methodName + " from " + target, exception);
		}
	}

	private static String capitalize(String input) {
		String[] words = input.split(" ");
		StringBuilder builder = new StringBuilder(input.length());
		for (String word : words) {
			if (!builder.isEmpty()) {
				builder.append(' ');
			}
			builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
		}
		return builder.toString();
	}
}
