package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.OreScannerComponent;
import twilightforest.components.item.OreScannerData;
import twilightforest.components.item.PotionFlaskComponent;

import java.util.UUID;

public final class TFDataComponents {
    public static final DataComponentType<Unit> TRANSLATABLE_BOOK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("translatable_book"),
            DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    /**
     * Q32: registered for {@code BrittleFlaskItem}/{@code GreaterFlaskItem}.
     * Carries the brewed potion contents, dose counter, and breakage counter so
     * vanilla brewing recipes (when wired in a later batch) can write into the
     * stack and the flask drink-flow can read it back. The default for an
     * unset stack is {@link PotionFlaskComponent#EMPTY}.
     */
    public static final DataComponentType<PotionFlaskComponent> POTION_FLASK_CONTENTS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("potion_flask_contents"),
            DataComponentType.<PotionFlaskComponent>builder()
                .persistent(PotionFlaskComponent.CODEC)
                .networkSynchronized(PotionFlaskComponent.STREAM_CODEC)
                .build()
    );

    public static final DataComponentType<OreScannerComponent> ORE_SCANNER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("ore_scanner"),
            DataComponentType.<OreScannerComponent>builder()
                .persistent(OreScannerComponent.CODEC)
                .build()
    );

    public static final DataComponentType<OreScannerComponent> ORE_SCANNING = ORE_SCANNER;

    public static final DataComponentType<OreScannerData> ORE_SCANNER_DATA = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("ore_scanner_data"),
            DataComponentType.<OreScannerData>builder()
                .persistent(OreScannerData.CODEC)
                .networkSynchronized(OreScannerData.STREAM_CODEC)
                .build()
    );

    public static final DataComponentType<OreScannerData> ORE_DATA = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("ore_data"),
            DataComponentType.<OreScannerData>builder()
                .persistent(OreScannerData.CODEC)
                .networkSynchronized(OreScannerData.STREAM_CODEC)
                .build()
    );

    public static final DataComponentType<Integer> ORE_LOADING = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("ore_loading"),
            DataComponentType.<Integer>builder()
                .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .cacheEncoding()
                .build()
    );

    public static final DataComponentType<Integer> ORE_RANGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("ore_range"),
            DataComponentType.<Integer>builder()
                .persistent(ExtraCodecs.intRange(0, 2))
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .cacheEncoding()
                .build()
    );

    public static final DataComponentType<Block> ORE_FILTER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("ore_filter"),
            DataComponentType.<Block>builder()
                .persistent(BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR))
                .networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK))
                .cacheEncoding()
                .build()
    );

    public static final DataComponentType<UUID> THROWN_PROJECTILE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("thrown_projectile"),
            DataComponentType.<UUID>builder()
                .persistent(UUIDUtil.CODEC)
                .networkSynchronized(UUIDUtil.STREAM_CODEC)
                .build()
    );

    /**
     * Aliases under upstream TF ids — referenced by advancement / item
     * predicates that we copied verbatim from upstream. Without these the
     * advancement loader rejects {@code modify_travellers_gear} /
     * {@code full_mettle_alchemist} / {@code experiment_115_self_replenishing}
     * with «No component with type: ...».
     */
    public static final DataComponentType<Unit> WATER_WALK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("water_walk"),
            DataComponentType.<Unit>builder()
                .persistent(Codec.unit(Unit.INSTANCE))
                .build()
    );

    /**
     * Upstream-named alias of {@link #POTION_FLASK_CONTENTS}. Same codec/stream-codec.
     */
    public static final DataComponentType<twilightforest.components.item.PotionFlaskComponent> FLASK_CONTENTS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("flask_contents"),
            DataComponentType.<twilightforest.components.item.PotionFlaskComponent>builder()
                .persistent(twilightforest.components.item.PotionFlaskComponent.CODEC)
                .networkSynchronized(twilightforest.components.item.PotionFlaskComponent.STREAM_CODEC)
                .build()
    );

    public static final DataComponentType<String> E115_VARIANT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("e115_variant"),
            DataComponentType.<String>builder()
                .persistent(Codec.STRING)
                .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                .build()
    );

    /** Q: stamps which item is the lid of a glass jar (firefly/cicada). */
    public static final DataComponentType<twilightforest.components.item.JarLid> JAR_LID = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("jar_lid"),
            DataComponentType.<twilightforest.components.item.JarLid>builder()
                .persistent(twilightforest.components.item.JarLid.CODEC)
                .build()
    );

    /**
     * P2.d: registered for {@link twilightforest.block.CandelabraBlock} pickup-as-itemstack
     * (Silk Touch / clone item). Carries the three candle slots so when a player breaks the
     * candelabra and re-places it, the original arrangement persists.
     */
    public static final DataComponentType<twilightforest.components.item.CandelabraData> CANDELABRA_DATA = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("candelabra_data"),
            DataComponentType.<twilightforest.components.item.CandelabraData>builder()
                .persistent(twilightforest.components.item.CandelabraData.CODEC)
                .networkSynchronized(twilightforest.components.item.CandelabraData.STREAM_CODEC)
                .build()
    );

    public static final DataComponentType<twilightforest.components.item.SkullCandles> SKULL_CANDLES = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("skull_candles"),
            DataComponentType.<twilightforest.components.item.SkullCandles>builder()
                .persistent(twilightforest.components.item.SkullCandles.CODEC)
                .networkSynchronized(twilightforest.components.item.SkullCandles.STREAM_CODEC)
                .build()
    );

    public static final DataComponentType<Integer> CASKET_DAMAGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("casket_damage"),
            DataComponentType.<Integer>builder()
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build()
    );

    public static final DataComponentType<Unit> EMPERORS_CLOTH = unit("emperors_cloth");
    public static final DataComponentType<Unit> INFINITE_GLASS_SWORD = unit("infinite_glass_sword");
    public static final DataComponentType<Unit> IS_TRAVELLERS_GEAR = unit("travellers_armor");
    public static final DataComponentType<ItemAttributeModifiers> STORED_BROKEN_ATTRIBUTES = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("stored_broken_attributes"),
            DataComponentType.<ItemAttributeModifiers>builder()
                .persistent(ItemAttributeModifiers.CODEC)
                .networkSynchronized(ItemAttributeModifiers.STREAM_CODEC)
                .cacheEncoding()
                .build()
    );

    public static final DataComponentType<Unit> TRAVELLERS_HAS_CHESTPLATE = unit("has_travellers_chestplate");
    public static final DataComponentType<Unit> TRAVELLERS_HAS_GLOVES = unit("has_travellers_gloves");
    public static final DataComponentType<Unit> TRAVELLERS_HAS_BELT = unit("has_travellers_belt");
    public static final DataComponentType<Unit> TRAVELLERS_HAS_WINGS = unit("has_travellers_wings");
    public static final DataComponentType<Unit> TRAVELLERS_HAS_BOOTS = unit("has_travellers_boots");

    public static final DataComponentType<Float> AUTO_REPAIR_PROBABILITY = floatComponent("auto_repair_probability", ExtraCodecs.POSITIVE_FLOAT);
    public static final DataComponentType<Float> ZOOM_ABILITY_MODIFIER = floatComponent("zoom_ability_modifier", ExtraCodecs.POSITIVE_FLOAT);
    public static final DataComponentType<Unit> RED_THREAD_VISION = unit("red_thread_vision");
    public static final DataComponentType<Unit> STEALTH_CROUCHING = unit("stealth_crouching");
    public static final DataComponentType<Unit> ARROW_MAGNETISM = unit("arrow_magnetism");
    public static final DataComponentType<Float> EFFICIENT_EATER = floatComponent("efficient_eater", Codec.FLOAT);
    public static final DataComponentType<Float> PERFECT_DODGE_PROBABILITY = floatComponent("perfect_dodge_probability", ExtraCodecs.POSITIVE_FLOAT);
    public static final DataComponentType<Integer> HASTE_AMPLIFIER = intComponent("haste_amplifier");
    public static final DataComponentType<Unit> SWAP_HOTBAR_ABILITY = unit("swap_hotbar_ability");
    public static final DataComponentType<Unit> SWAP_HOTBAR_MODIFIER = unit("swap_hotbar_modifier");
    public static final DataComponentType<Integer> HIGH_JUMP_AMPLIFIER = intComponent("high_jump_amplifier");
    public static final DataComponentType<Float> GRADUALLY_GLIDING_MULTIPLIER = floatComponent("gradually_gliding_multiplier", ExtraCodecs.POSITIVE_FLOAT);
    public static final DataComponentType<Float> AGILE_RANGER_MODIFIER = floatComponent("agile_ranger_modifier", ExtraCodecs.POSITIVE_FLOAT);
    public static final DataComponentType<Unit> DOUBLE_JUMP = unit("double_jump");
    public static final DataComponentType<Long> SIDESTEP_COOLDOWN = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("sidestep_cooldown"),
            DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).cacheEncoding().build()
    );
    public static final DataComponentType<Double> STRAIGHT_AHEAD_MULTIPLIER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("straight_ahead_multiplier"),
            DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build()
    );
    public static final DataComponentType<Float> SLIMY_SOLES_COEFFICIENT = floatComponent("slimy_soles_coefficient", ExtraCodecs.POSITIVE_FLOAT);
    public static final DataComponentType<Unit> ALL_NIGHT_GOGGLES = unit("all_night_goggles");
    public static final DataComponentType<twilightforest.components.item.ItemDisplayContents> ITEM_DISPLAY = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("item_display"),
            DataComponentType.<twilightforest.components.item.ItemDisplayContents>builder()
                .persistent(twilightforest.components.item.ItemDisplayContents.CODEC)
                .networkSynchronized(twilightforest.components.item.ItemDisplayContents.STREAM_CODEC)
                .cacheEncoding()
                .build()
    );
    public static final DataComponentType<Unit> UNRESTRAINED = unit("unrestrained");
    public static final DataComponentType<Unit> SWIFT_SWIM = unit("swift_swim");
    public static final DataComponentType<Unit> HIGH_STEP = unit("high_step");
    public static final DataComponentType<Unit> AQUATIC_AGILITY = unit("aquatic_agility");

    /**
     * P5.c: data component carrying the {@link net.minecraft.core.Holder} of the
     * Magic Painting variant baked into a {@code MagicPaintingItem} stack so the
     * placed entity inherits the variant. Uses the datapack-driven
     * {@code MAGIC_PAINTINGS} registry codec set.
     */
    public static final DataComponentType<net.minecraft.core.Holder<twilightforest.entity.MagicPaintingVariant>> MAGIC_PAINTING_VARIANT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwilightForestMod.prefix("magic_painting_variant"),
            DataComponentType.<net.minecraft.core.Holder<twilightforest.entity.MagicPaintingVariant>>builder()
                .persistent(twilightforest.init.custom.MagicPaintingVariants.CODEC)
                .networkSynchronized(twilightforest.init.custom.MagicPaintingVariants.STREAM_CODEC)
                .build()
    );

    private TFDataComponents() {
    }

    private static DataComponentType<Unit> unit(String path) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                TwilightForestMod.prefix(path),
                DataComponentType.<Unit>builder()
                    .persistent(Codec.unit(Unit.INSTANCE))
                    .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
                    .cacheEncoding()
                    .build()
        );
    }

    private static DataComponentType<Integer> intComponent(String path) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                TwilightForestMod.prefix(path),
                DataComponentType.<Integer>builder()
                    .persistent(ExtraCodecs.UNSIGNED_BYTE)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .cacheEncoding()
                    .build()
        );
    }

    private static DataComponentType<Float> floatComponent(String path, Codec<Float> codec) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                TwilightForestMod.prefix(path),
                DataComponentType.<Float>builder()
                    .persistent(codec)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
                    .cacheEncoding()
                    .build()
        );
    }
}
