package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TFMain;
import twilightforest.components.item.*;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.UUID;

public class TFDataComponents {

	public static final DataComponentType<Unit> EMPERORS_CLOTH = registerSynced("emperors_cloth", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<PotionFlaskComponent> POTION_FLASK_CONTENTS = registerSynced("flask_contents", PotionFlaskComponent.CODEC, PotionFlaskComponent.STREAM_CODEC);
	public static final DataComponentType<Unit> INFINITE_GLASS_SWORD = registerSynced("infinite_glass_sword", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<UUID> THROWN_PROJECTILE = registerSynced("thrown_projectile", UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);
	public static final DataComponentType<String> EXPERIMENT_115_VARIANTS = registerSynced("e115_variant", Codec.STRING, ByteBufCodecs.STRING_UTF8);
	public static final DataComponentType<SkullCandles> SKULL_CANDLES = registerSynced("skull_candles", SkullCandles.CODEC, SkullCandles.STREAM_CODEC);
	public static final DataComponentType<CandelabraData> CANDELABRA_DATA = register("candelabra_data", CandelabraData.CODEC);
	public static final DataComponentType<Holder<MagicPaintingVariant>> MAGIC_PAINTING_VARIANT = registerSynced("magic_painting_variant", MagicPaintingVariants.CODEC, MagicPaintingVariants.STREAM_CODEC);
	public static final DataComponentType<Unit> TRANSLATABLE_BOOK = registerSynced("translatable_book", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<JarLid> JAR_LID = register("jar_lid", JarLid.CODEC);
	public static final DataComponentType<Integer> CASKET_DAMAGE = registerSynced("casket_damage", Codec.INT, ByteBufCodecs.INT);

	public static final DataComponentType<OreScannerComponent> ORE_SCANNING = register("ore_scanner", OreScannerComponent.CODEC);
	public static final DataComponentType<OreScannerData> ORE_DATA = registerSynced("ore_data", OreScannerData.CODEC, OreScannerData.STREAM_CODEC);
	public static final DataComponentType<Integer> ORE_LOADING = registerSyncedCached("ore_loading", ExtraCodecs.NON_NEGATIVE_INT.orElse(0), ByteBufCodecs.VAR_INT);
	public static final DataComponentType<Integer> ORE_RANGE = registerSyncedCached("ore_range", ExtraCodecs.NON_NEGATIVE_INT.orElse(1), ByteBufCodecs.VAR_INT);
	public static final DataComponentType<Block> ORE_FILTER = registerSyncedCached("ore_filter", BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR), ByteBufCodecs.registry(Registries.BLOCK));

	public static final DataComponentType<Unit> IS_TRAVELLERS_GEAR = registerSyncedCached("travellers_armor", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<ItemAttributeModifiers> STORED_BROKEN_ATTRIBUTES = registerSyncedCached("stored_broken_attributes", ItemAttributeModifiers.CODEC, ItemAttributeModifiers.STREAM_CODEC);

	public static final DataComponentType<Unit> TRAVELLERS_HAS_CHESTPLATE = registerSyncedCached("has_travellers_chestplate", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> TRAVELLERS_HAS_GLOVES = registerSyncedCached("has_travellers_gloves", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> TRAVELLERS_HAS_BELT = registerSyncedCached("has_travellers_belt", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> TRAVELLERS_HAS_WINGS = registerSyncedCached("has_travellers_wings", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> TRAVELLERS_HAS_BOOTS = registerSyncedCached("has_travellers_boots", Unit.CODEC, Unit.STREAM_CODEC);

	public static final DataComponentType<Float> AUTO_REPAIR_PROBABILITY = registerSyncedCached("auto_repair_probability", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Float> ZOOM_ABILITY_MODIFIER = registerSyncedCached("zoom_ability_modifier", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Unit> RED_THREAD_VISION = registerSyncedCached("red_thread_vision", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> STEALTH_CROUCHING = registerSyncedCached("stealth_crouching", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> ARROW_MAGNETISM = registerSyncedCached("arrow_magnetism", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Float> EFFICIENT_EATER = registerSyncedCached("efficient_eater", Codec.FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Float> PERFECT_DODGE_PROBABILITY = registerSyncedCached("perfect_dodge_probability", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Integer> HASTE_AMPLIFIER = registerSyncedCached("haste_amplifier", ExtraCodecs.UNSIGNED_BYTE, ByteBufCodecs.INT);
	public static final DataComponentType<Unit> SWAP_HOTBAR_ABILITY = registerSyncedCached("swap_hotbar_ability", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> SWAP_HOTBAR_MODIFIER = registerSyncedCached("swap_hotbar_modifier", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Integer> HIGH_JUMP_AMPLIFIER = registerSyncedCached("high_jump_amplifier", ExtraCodecs.UNSIGNED_BYTE, ByteBufCodecs.INT);
	public static final DataComponentType<Float> GRADUALLY_GLIDING_MULTIPLIER = registerSyncedCached("gradually_gliding_multiplier", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Float> AGILE_RANGER_MODIFIER = registerSyncedCached("agile_ranger_modifier", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Unit> DOUBLE_JUMP = registerSyncedCached("double_jump", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Long> SIDESTEP_COOLDOWN = registerSyncedCached("sidestep_cooldown", Codec.LONG, ByteBufCodecs.VAR_LONG);
	public static final DataComponentType<Double> STRAIGHT_AHEAD_MULTIPLIER = registerSyncedCached("straight_ahead_multiplier", Codec.DOUBLE, ByteBufCodecs.DOUBLE);
	public static final DataComponentType<Float> SLIMY_SOLES_COEFFICIENT = registerSyncedCached("slimy_soles_coefficient", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT);
	public static final DataComponentType<Unit> WATER_WALK = registerSyncedCached("water_walk", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> ALL_NIGHT_GOGGLES = registerSyncedCached("all_night_goggles", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<ItemDisplayContents> ITEM_DISPLAY = registerSyncedCached("item_display", ItemDisplayContents.CODEC, ItemDisplayContents.STREAM_CODEC);
	public static final DataComponentType<Unit> UNRESTRAINED = registerSyncedCached("unrestrained", Unit.CODEC, Unit.STREAM_CODEC);

	public static final DataComponentType<Unit> SWIFT_SWIM = registerSyncedCached("swift_swim", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> HIGH_STEP = registerSyncedCached("high_step", Unit.CODEC, Unit.STREAM_CODEC);
	public static final DataComponentType<Unit> AQUATIC_AGILITY = registerSyncedCached("aquatic_agility", Unit.CODEC, Unit.STREAM_CODEC);

	private static <T> DataComponentType<T> register(
		String name,
		Codec<T> codec
	) {
		return Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			TFMain.prefix(name),
			DataComponentType.<T>builder()
				.persistent(codec)
				.build()
		);
	}

	private static <T> DataComponentType<T> registerSynced(
		String name,
		Codec<T> codec,
		StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec
	) {
		return Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			TFMain.prefix(name),
			DataComponentType.<T>builder()
				.persistent(codec)
				.networkSynchronized(streamCodec)
				.build()
		);
	}

	private static <T> DataComponentType<T> registerSyncedCached(
		String name,
		Codec<T> codec,
		StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec
	) {
		return Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			TFMain.prefix(name),
			DataComponentType.<T>builder()
				.persistent(codec)
				.networkSynchronized(streamCodec)
				.cacheEncoding()
				.build()
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing data components...");
	}
}