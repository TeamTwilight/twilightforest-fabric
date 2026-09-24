package twilightforest.datagen.data.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import twilightforest.TFCommon;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.List;

public class TravellersModifierGenerator {
	public static void bootstrap(BootstrapContext<TravellersModifier> context) {
		TFCommon.LOGGER.info("Bootstrap called for travellers modifiers...");
		context.register(TravellersModifiersManager.AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.ARMOR, TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, componentText(TravellersModifiersManager.AUTO_REPAIR_MODIFIER)));
		context.register(TravellersModifiersManager.ZOOM_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ZOOM_ABILITY_MODIFIER));
		context.register(TravellersModifiersManager.AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(EquipmentSlotGroup.HEAD, List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING, EquipmentSlotGroup.HEAD)
		), TFDataComponents.AQUATIC_AGILITY, componentText(TravellersModifiersManager.AQUATIC_AGILITY_MODIFIER), false));
		context.register(TravellersModifiersManager.RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.RED_THREAD_VISION, Unit.INSTANCE, componentText(TravellersModifiersManager.RED_THREAD_VISION_MODIFIER)));
		context.register(TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ALL_NIGHT_GOGGLES, Unit.INSTANCE, componentText(TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)));
		context.register(TravellersModifiersManager.ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ITEM_DISPLAY, ItemDisplayContents.EMPTY, componentText(TravellersModifiersManager.ITEM_DISPLAY_MODIFIER)));

		context.register(TravellersModifiersManager.SWIFT_SWIM_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.CHEST, List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)), () -> TFDataComponents.SWIFT_SWIM, true));
		context.register(TravellersModifiersManager.STEALTH_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.STEALTH_CROUCHING, Unit.INSTANCE, componentText(TravellersModifiersManager.STEALTH_MODIFIER)));
		context.register(TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.ARROW_MAGNETISM, Unit.INSTANCE, componentText(TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER)));
		context.register(TravellersModifiersManager.EFFICIENT_EATER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.EFFICIENT_EATER, 2F, componentText(TravellersModifiersManager.EFFICIENT_EATER_MODIFIER)));
		context.register(TravellersModifiersManager.PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.3F, componentText(TravellersModifiersManager.PERFECT_DODGE_MODIFIER)));
		context.register(TravellersModifiersManager.HASTE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.HASTE_AMPLIFIER, 1, componentText(TravellersModifiersManager.HASTE_MODIFIER)));

		context.register(TravellersModifiersManager.SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_ABILITY));
		context.register(TravellersModifiersManager.SWAP_HOTBAR_MODIFIER, new TransferableComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_MODIFIER, DataComponents.CONTAINER, TravellersArmorBeltItem.DEFAULT_EMPTY_BELT_CONTAINER, componentText(TravellersModifiersManager.SWAP_HOTBAR_MODIFIER)));

		context.register(TravellersModifiersManager.HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.HIGH_JUMP_AMPLIFIER));
		context.register(TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER, 1 - 1 / 6F, componentText(TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER)));
		context.register(TravellersModifiersManager.AGILE_RANGER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.AGILE_RANGER_MODIFIER, Unit.INSTANCE, componentText(TravellersModifiersManager.AGILE_RANGER_MODIFIER)));
		context.register(TravellersModifiersManager.DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.DOUBLE_JUMP, Unit.INSTANCE, componentText(TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)));
		context.register(TravellersModifiersManager.SIDESTEP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SIDESTEP_COOLDOWN, 2 * 20L, componentText(TravellersModifiersManager.SIDESTEP_MODIFIER, Component.keybind("key.left"), Component.keybind("key.right"))));

		context.register(TravellersModifiersManager.STEP_UP_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.FEET, List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)), () -> TFDataComponents.HIGH_STEP, true));
		context.register(TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER, 1.4, componentText(TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER)));
		context.register(TravellersModifiersManager.SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, componentText(TravellersModifiersManager.SLIMY_SOLES_MODIFIER)));
		context.register(TravellersModifiersManager.UNRESTRAINED_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.UNRESTRAINED, Unit.INSTANCE, componentText(TravellersModifiersManager.UNRESTRAINED_MODIFIER)));
		context.register(TravellersModifiersManager.WATER_WALK_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.WATER_WALK, Unit.INSTANCE, componentText(TravellersModifiersManager.WATER_WALK_MODIFIER)));
	}

	private static List<Component> componentText(ResourceKey<TravellersModifier> modifier, Object... args) {
		return List.of(Component.translatable(modifier.identifier().toLanguageKey("travellers_gear.modifier", "description"), args));
	}
}