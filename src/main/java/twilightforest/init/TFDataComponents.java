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
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import org.jspecify.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.components.item.*;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.Optional;
import java.util.UUID;

public class TFDataComponents {
	public static final DataComponentType<Unit> EMPERORS_CLOTH = register("emperors_cloth", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());
	public static final DataComponentType<PotionFlaskComponent> POTION_FLASK_CONTENTS = register("flask_contents", DataComponentType.<PotionFlaskComponent>builder().persistent(PotionFlaskComponent.CODEC).networkSynchronized(PotionFlaskComponent.STREAM_CODEC).build());
	public static final DataComponentType<Unit> INFINITE_GLASS_SWORD = register("infinite_glass_sword", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());
	public static final DataComponentType<UUID> THROWN_PROJECTILE = register("thrown_projectile", DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());
	public static final DataComponentType<String> EXPERIMENT_115_VARIANTS = register("e115_variant", DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DataComponentType<SkullCandles> SKULL_CANDLES = register("skull_candles", DataComponentType.<SkullCandles>builder().persistent(SkullCandles.CODEC).networkSynchronized(SkullCandles.STREAM_CODEC).build());
	public static final DataComponentType<CandelabraData> CANDELABRA_DATA = register("candelabra_data", DataComponentType.<CandelabraData>builder().persistent(CandelabraData.CODEC).build());
	public static final DataComponentType<Holder<MagicPaintingVariant>> MAGIC_PAINTING_VARIANT = register("magic_painting_variant", DataComponentType.<Holder<MagicPaintingVariant>>builder().persistent(MagicPaintingVariants.CODEC).networkSynchronized(MagicPaintingVariants.STREAM_CODEC).build());
	public static final DataComponentType<Unit> TRANSLATABLE_BOOK = register("translatable_book", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());
	public static final DataComponentType<JarLid> JAR_LID = register("jar_lid", JarLid.CODEC);
	public static final DataComponentType<Integer> CASKET_DAMAGE = register("casket_damage", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());

	public static final DataComponentType<OreScannerComponent> ORE_SCANNING = register("ore_scanner", OreScannerComponent.CODEC);
	public static final DataComponentType<OreScannerData> ORE_DATA = register("ore_data", OreScannerData.CODEC, OreScannerData.STREAM_CODEC);
	public static final DataComponentType<Integer> ORE_LOADING = register("ore_loading", DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());
	public static final DataComponentType<Integer> ORE_RANGE = register("ore_range", DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(1)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());
	public static final DataComponentType<Block> ORE_FILTER = register("ore_filter", DataComponentType.<Block>builder().persistent(BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR)).networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK)).cacheEncoding().build());

	public static final DataComponentType<Unit> IS_TRAVELLERS_GEAR = register("travellers_armor", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<ItemAttributeModifiers> STORED_BROKEN_ATTRIBUTES = register("stored_broken_attributes", DataComponentType.<ItemAttributeModifiers>builder().persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding().build());

	public static final DataComponentType<Unit> TRAVELLERS_HAS_CHESTPLATE = register("has_travellers_chestplate", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> TRAVELLERS_HAS_GLOVES = register("has_travellers_gloves", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> TRAVELLERS_HAS_BELT = register("has_travellers_belt", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> TRAVELLERS_HAS_WINGS = register("has_travellers_wings", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> TRAVELLERS_HAS_BOOTS = register("has_travellers_boots", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());

	public static final DataComponentType<Float> AUTO_REPAIR_PROBABILITY = register("auto_repair_probability", DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DataComponentType<Float> ZOOM_ABILITY_MODIFIER = register("zoom_ability_modifier", DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DataComponentType<Unit> IS_USING_GOGGLES_ZOOM = register("is_using_goggles_zoom", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> RED_THREAD_VISION =register("red_thread_vision", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> STEALTH_CROUCHING = register("stealth_crouching", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> ARROW_MAGNETISM = register("arrow_magnetism", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Float> EFFICIENT_EATER = register("efficient_eater", DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DataComponentType<Float> PERFECT_DODGE_PROBABILITY = register("perfect_dodge_probability", DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DataComponentType<Integer> HASTE_AMPLIFIER = register("haste_amplifier", DataComponentType.<Integer>builder().persistent(ExtraCodecs.UNSIGNED_BYTE).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
	public static final DataComponentType<Unit> SWAP_HOTBAR_ABILITY = register("swap_hotbar_ability", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> SWAP_HOTBAR_MODIFIER = register("swap_hotbar_modifier", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Integer> HIGH_JUMP_AMPLIFIER = register("high_jump_amplifier", DataComponentType.<Integer>builder().persistent(ExtraCodecs.UNSIGNED_BYTE).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
	public static final DataComponentType<Float> GRADUALLY_GLIDING_MULTIPLIER = register("gradually_gliding_multiplier", DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DataComponentType<Unit> AGILE_RANGER_MODIFIER = register("agile_ranger_modifier", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> DOUBLE_JUMP = register("double_jump", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Long> SIDESTEP_COOLDOWN = register("sidestep_cooldown", DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).cacheEncoding().build());
	public static final DataComponentType<Double> STRAIGHT_AHEAD_MULTIPLIER = register("straight_ahead_multiplier", DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());
	public static final DataComponentType<Float> SLIMY_SOLES_COEFFICIENT = register("slimy_soles_coefficient", DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DataComponentType<Unit> WATER_WALK = register("water_walk", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> ALL_NIGHT_GOGGLES = register("all_night_goggles", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<ItemDisplayContents> ITEM_DISPLAY = register("item_display", DataComponentType.<ItemDisplayContents>builder().persistent(ItemDisplayContents.CODEC).networkSynchronized(ItemDisplayContents.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> UNRESTRAINED = register("unrestrained", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());

	public static final DataComponentType<Unit> SWIFT_SWIM = register("swift_swim", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> HIGH_STEP = register("high_step", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DataComponentType<Unit> AQUATIC_AGILITY = register("aquatic_agility", DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());

	// Phase and DimensionType must sync for the Moon Dial's model property and Level for tooltip. DimensionType is the only one allowed to trigger the swap animation in first-person hand
	public static final DataComponentType<Optional<MoonPhase>> MOON_DIAL_PHASE = register("moon_dial_phase", DataComponentType.<Optional<MoonPhase>>builder().persistent(ExtraCodecs.optionalEmptyMap(MoonPhase.CODEC)).networkSynchronized(ByteBufCodecs.VAR_INT.map(index -> MoonPhase.values()[index], MoonPhase::index).apply(ByteBufCodecs::optional)).cacheEncoding().ignoreSwapAnimation().build());
	public static final DataComponentType<ResourceKey<DimensionType>> MOON_DIAL_DIMENSION = register("moon_dial_dimension", DataComponentType.<ResourceKey<DimensionType>>builder().persistent(ResourceKey.codec(Registries.DIMENSION_TYPE)).networkSynchronized(ResourceKey.streamCodec(Registries.DIMENSION_TYPE)).cacheEncoding().build());
	public static final DataComponentType<ResourceKey<Level>> MOON_DIAL_LEVEL = register("moon_dial_level", DataComponentType.<ResourceKey<Level>>builder().persistent(ResourceKey.codec(Registries.DIMENSION)).networkSynchronized(ResourceKey.streamCodec(Registries.DIMENSION)).ignoreSwapAnimation().build());

	private static <T> DataComponentType<T> register(String name, final DataComponentType<T> type) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TFCommon.prefix(name), type);
	}

	private static <T> DataComponentType<T> register(String name, final Codec<T> codec) {
		return register(name, codec, null);
	}

	private static <T> DataComponentType<T> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		if (streamCodec == null) {
			return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TFCommon.prefix(name), DataComponentType.<T>builder().persistent(codec).build());
		} else {
			return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TFCommon.prefix(name), DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
		}
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing data ..");
	}
}