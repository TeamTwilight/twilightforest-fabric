package twilightforest.init.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TravellersModifiersManager {
    public static final ResourceKey<TravellersModifier> AUTO_REPAIR_MODIFIER = makeKey("auto_repair");
    public static final ResourceKey<TravellersModifier> ZOOM_ABILITY = makeKey("zoom");
    public static final ResourceKey<TravellersModifier> AQUATIC_AGILITY_MODIFIER = makeKey("aquatic_agility");
    public static final ResourceKey<TravellersModifier> RED_THREAD_VISION_MODIFIER = makeKey("red_thread_vision");
    public static final ResourceKey<TravellersModifier> ALL_NIGHT_GOGGLES_MODIFIER = makeKey("all_night_goggles");
    public static final ResourceKey<TravellersModifier> ITEM_DISPLAY_MODIFIER = makeKey("item_display");
    public static final ResourceKey<TravellersModifier> SWIFT_SWIM_ABILITY = makeKey("swift_swim");
    public static final ResourceKey<TravellersModifier> STEALTH_MODIFIER = makeKey("stealth");
    public static final ResourceKey<TravellersModifier> ARROW_MAGNETISM_MODIFIER = makeKey("arrow_magnetism");
    public static final ResourceKey<TravellersModifier> EFFICIENT_EATER_MODIFIER = makeKey("efficient_eater");
    public static final ResourceKey<TravellersModifier> PERFECT_DODGE_MODIFIER = makeKey("perfect_dodge");
    public static final ResourceKey<TravellersModifier> HASTE_MODIFIER = makeKey("haste");
    public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_ABILITY = makeKey("swap_hotbar_ability");
    public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_MODIFIER = makeKey("swap_hotbar");
    public static final ResourceKey<TravellersModifier> HIGH_JUMP_ABILITY = makeKey("high_jump");
    public static final ResourceKey<TravellersModifier> GRADUAL_GLIDE_MODIFIER = makeKey("gradual_glide");
    public static final ResourceKey<TravellersModifier> AGILE_RANGER_MODIFIER = makeKey("agile_ranger");
    public static final ResourceKey<TravellersModifier> DOUBLE_JUMP_MODIFIER = makeKey("double_jump");
    public static final ResourceKey<TravellersModifier> SIDESTEP_MODIFIER = makeKey("side_step");
    public static final ResourceKey<TravellersModifier> STEP_UP_ABILITY = makeKey("step_up");
    public static final ResourceKey<TravellersModifier> STRAIGHT_AHEAD_MODIFIER = makeKey("straight_ahead");
    public static final ResourceKey<TravellersModifier> SLIMY_SOLES_MODIFIER = makeKey("slimy_soles");
    public static final ResourceKey<TravellersModifier> UNRESTRAINED_MODIFIER = makeKey("unrestrained");
    public static final ResourceKey<TravellersModifier> WATER_WALK_MODIFIER = makeKey("water_walk");
    public static final Set<ResourceKey<TravellersModifier>> ALWAYS_ACTIVE = Set.of(AUTO_REPAIR_MODIFIER);

    private static final Map<ResourceKey<TravellersModifier>, TravellersModifier> MODIFIERS = new LinkedHashMap<>();

    static {
        register(AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.ARMOR, TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, componentText(AUTO_REPAIR_MODIFIER)));
        register(ZOOM_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ZOOM_ABILITY_MODIFIER));
        register(AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(EquipmentSlotGroup.HEAD, List.of(
            new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN, EquipmentSlotGroup.HEAD),
            new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING, EquipmentSlotGroup.HEAD)
        ), TFDataComponents.AQUATIC_AGILITY, componentText(AQUATIC_AGILITY_MODIFIER), false));
        register(RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.RED_THREAD_VISION, Unit.INSTANCE, componentText(RED_THREAD_VISION_MODIFIER)));
        register(ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ALL_NIGHT_GOGGLES, Unit.INSTANCE, componentText(ALL_NIGHT_GOGGLES_MODIFIER)));
        register(ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ITEM_DISPLAY, ItemDisplayContents.EMPTY, componentText(ITEM_DISPLAY_MODIFIER)));
        register(SWIFT_SWIM_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.CHEST, List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)), TFDataComponents.SWIFT_SWIM, List.of(), true));
        register(STEALTH_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.STEALTH_CROUCHING, Unit.INSTANCE, componentText(STEALTH_MODIFIER)));
        register(ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.ARROW_MAGNETISM, Unit.INSTANCE, componentText(ARROW_MAGNETISM_MODIFIER)));
        register(EFFICIENT_EATER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.EFFICIENT_EATER, 2.0F, componentText(EFFICIENT_EATER_MODIFIER)));
        register(PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.3F, componentText(PERFECT_DODGE_MODIFIER)));
        register(HASTE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.HASTE_AMPLIFIER, 1, componentText(HASTE_MODIFIER)));
        register(SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_ABILITY));
        register(SWAP_HOTBAR_MODIFIER, new TransferableComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_MODIFIER, DataComponents.CONTAINER, TravellersArmorBeltItem.DEFAULT_EMPTY_BELT_CONTAINER, componentText(SWAP_HOTBAR_MODIFIER)));
        register(HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.HIGH_JUMP_AMPLIFIER));
        register(GRADUAL_GLIDE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER, 1 - 1 / 6F, componentText(GRADUAL_GLIDE_MODIFIER)));
        register(AGILE_RANGER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.AGILE_RANGER_MODIFIER, 5.0F, componentText(AGILE_RANGER_MODIFIER)));
        register(DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.DOUBLE_JUMP, Unit.INSTANCE, componentText(DOUBLE_JUMP_MODIFIER)));
        register(SIDESTEP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SIDESTEP_COOLDOWN, 40L, componentText(SIDESTEP_MODIFIER, Component.keybind("key.left"), Component.keybind("key.right"))));
        register(STEP_UP_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.FEET, List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)), TFDataComponents.HIGH_STEP, List.of(), true));
        register(STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER, 1.4D, componentText(STRAIGHT_AHEAD_MODIFIER)));
        register(SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, componentText(SLIMY_SOLES_MODIFIER)));
        register(UNRESTRAINED_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.UNRESTRAINED, Unit.INSTANCE, componentText(UNRESTRAINED_MODIFIER)));
        register(WATER_WALK_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.WATER_WALK, Unit.INSTANCE, componentText(WATER_WALK_MODIFIER)));
    }

    private TravellersModifiersManager() {
    }

    public static void bootstrap(BootstrapContext<TravellersModifier> context) {
        MODIFIERS.forEach(context::register);
    }

    private static ResourceKey<TravellersModifier> makeKey(String name) {
        return ResourceKey.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TwilightForestMod.prefix(name));
    }

    private static void register(ResourceKey<TravellersModifier> key, TravellersModifier modifier) {
        MODIFIERS.put(key, modifier);
    }

    private static List<Component> componentText(ResourceKey<TravellersModifier> modifier, Object... args) {
        return List.of(Component.translatable(modifier.location().toLanguageKey("travellers_gear.modifier", "description"), args));
    }

    public static boolean isModifierActive(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey, boolean spectator) {
        return getModifier(modifierKey).map(modifier -> modifier.isActive(stack, modifierKey, spectator)).orElse(false);
    }

    public static boolean isModifierActive(Entity entity, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
        return isModifierActive(entity.registryAccess(), stack, modifierKey, entity.isSpectator());
    }

    public static boolean isModifierActive(Entity entity, ResourceKey<TravellersModifier> modifierKey) {
        return entity instanceof LivingEntity livingEntity && isModifierActive(livingEntity, modifierKey);
    }

    public static boolean isModifierActive(LivingEntity livingEntity, ResourceKey<TravellersModifier> modifierKey) {
        Optional<TravellersModifier> modifier = getModifier(modifierKey);
        if (modifier.isEmpty()) {
            return false;
        }
        ItemStack stack = getStackForGroup(livingEntity, modifier.get().group());
        return !stack.isEmpty() && modifier.get().isActive(stack, modifierKey, livingEntity.isSpectator());
    }

    public static boolean hasTravellersModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
        return getModifier(modifierKey).map(modifier -> modifier.hasModifier(stack)).orElse(false);
    }

    public static boolean addModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
        Optional<TravellersModifier> modifier = getModifier(modifierKey);
        return modifier.filter(value -> value instanceof InsertableTravellersModifier)
            .map(value -> ((InsertableTravellersModifier) value).addModifier(stack)).orElse(false);
    }

    public static boolean transferModifier(HolderLookup.Provider registries, ItemStack stack, List<Ingredient> ingredients, ResourceKey<TravellersModifier> modifierKey) {
        Optional<TravellersModifier> modifier = getModifier(modifierKey);
        return modifier.filter(value -> value instanceof TransferableTravellersModifier)
            .map(value -> ((TransferableTravellersModifier) value).transfer(stack, ingredients)).orElse(false);
    }

    public static int getModifierDataComponentProviders(HolderLookup.Provider registries, List<Ingredient> ingredients, ResourceKey<TravellersModifier> modifierKey) {
        Optional<TravellersModifier> modifier = getModifier(modifierKey);
        if (modifier.isEmpty() || !(modifier.get() instanceof TransferableComponentModifier transferable)) {
            return 0;
        }
        return transferable.findDataComponentProviders(ingredients).size();
    }

    public static MutableComponent getModifierTooltipComponent(Holder.Reference<TravellersModifier> modifier) {
        return Component.translatable(modifier.key().location().toLanguageKey(modifier.value().getPrefix())).withStyle(ChatFormatting.GRAY);
    }

    public static List<Holder.Reference<TravellersModifier>> findAllInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
        return findRegistryEntries(registries, stack, false);
    }

    public static List<Holder.Reference<TravellersModifier>> findAllAbilityModifiers(HolderLookup.Provider registries, ItemStack stack) {
        return findRegistryEntries(registries, stack, true);
    }

    private static List<Holder.Reference<TravellersModifier>> findRegistryEntries(HolderLookup.Provider registries, ItemStack stack, boolean ability) {
        try {
            return registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements()
                .filter(entry -> entry.value().isAbility() == ability && entry.value().hasModifier(stack))
                .toList();
        } catch (Exception ignored) {
            return List.of();
        }
    }

    public static long countInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
        long registryCount = findAllInsertableModifiers(registries, stack).size();
        if (registryCount > 0) {
            return registryCount;
        }
        return MODIFIERS.values().stream().filter(modifier -> modifier instanceof InsertableTravellersModifier && !modifier.isAbility() && modifier.hasModifier(stack)).count();
    }

    public static boolean isModifierEnabled(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifierKey) {
        return getModifier(modifierKey).isPresent();
    }

    public static List<TravellersModifier> allModifiers() {
        return MODIFIERS.values().stream().sorted(Comparator.comparing(modifier -> modifier.group().toString())).toList();
    }

    private static Optional<TravellersModifier> getModifier(ResourceKey<TravellersModifier> modifierKey) {
        return Optional.ofNullable(MODIFIERS.get(modifierKey));
    }

    private static ItemStack getStackForGroup(LivingEntity livingEntity, EquipmentSlotGroup group) {
        EquipmentSlot matchedSlot = null;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor() || !group.test(slot)) {
                continue;
            }
            if (matchedSlot != null) {
                return ItemStack.EMPTY;
            }
            matchedSlot = slot;
        }
        return matchedSlot == null ? ItemStack.EMPTY : livingEntity.getItemBySlot(matchedSlot);
    }

    public static boolean isDoubleJumpActive(LivingEntity entity) { return isModifierActive(entity, DOUBLE_JUMP_MODIFIER) || isModifierActive(entity, HIGH_JUMP_ABILITY); }
    public static boolean isWaterWalkActive(LivingEntity entity) { return isModifierActive(entity, WATER_WALK_MODIFIER); }
    public static boolean isStepUpActive(LivingEntity entity) { return isModifierActive(entity, STEP_UP_ABILITY); }
    public static boolean isCushionActive(LivingEntity entity) { return isModifierActive(entity, PERFECT_DODGE_MODIFIER); }
    public static boolean isSwiftSwimActive(LivingEntity entity) { return isModifierActive(entity, SWIFT_SWIM_ABILITY); }
    public static boolean isArrowMagnetismActive(LivingEntity entity) { return isModifierActive(entity, ARROW_MAGNETISM_MODIFIER); }
    public static boolean isPerfectDodgeActive(LivingEntity entity) { return isModifierActive(entity, PERFECT_DODGE_MODIFIER); }
    public static boolean isSlimySolesActive(LivingEntity entity) { return isModifierActive(entity, SLIMY_SOLES_MODIFIER); }
    public static boolean isRedThreadVisionActive(LivingEntity entity) { return isModifierActive(entity, RED_THREAD_VISION_MODIFIER); }
    public static boolean isUnrestrainedActive(LivingEntity entity) { return isModifierActive(entity, UNRESTRAINED_MODIFIER); }
    public static boolean isAutoRepairActive(LivingEntity entity) { return isModifierActive(entity, AUTO_REPAIR_MODIFIER); }
    public static boolean isEfficientEaterActive(LivingEntity entity) { return isModifierActive(entity, EFFICIENT_EATER_MODIFIER); }
    public static boolean isGradualGlideActive(LivingEntity entity) { return isModifierActive(entity, GRADUAL_GLIDE_MODIFIER); }
    public static boolean isStealthActive(LivingEntity entity) { return isModifierActive(entity, STEALTH_MODIFIER); }
    public static boolean isStraightAheadActive(LivingEntity entity) { return isModifierActive(entity, STRAIGHT_AHEAD_MODIFIER); }
    public static boolean isAgileRangerActive(LivingEntity entity) { return isModifierActive(entity, AGILE_RANGER_MODIFIER); }
    public static boolean isHighJumpActive(LivingEntity entity) { return isModifierActive(entity, HIGH_JUMP_ABILITY); }
}
