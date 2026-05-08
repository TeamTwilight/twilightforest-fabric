package twilightforest.item.travellers_gear;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class TravellersArmorItem extends ArmorItem implements TravellersModifiable {
	private static final MutableComponent GLOVES_TOOLTIP = Component.translatable("item.twilightforest.travellers_gloves.desc").withStyle(ChatFormatting.GRAY);
	private final int insertableModifierSlots;
	@Nullable
	private ItemAttributeModifiers attributeModifiers;

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots) {
		super(TFArmorMaterials.TRAVELLERS, equipmentType, properties.component(TFDataComponents.IS_TRAVELLERS_GEAR, Unit.INSTANCE));
		this.insertableModifierSlots = insertableModifierSlots;
		this.attributeModifiers = this.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (this.attributeModifiers == null)
			return;

		for (ItemAttributeModifiers.Entry modifier : this.getDefaultAttributeModifiers().modifiers()) {
			this.attributeModifiers = this.attributeModifiers.withModifierAdded(modifier.attribute(), modifier.modifier(), modifier.slot());
		}
	}

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots, int durability) {
		this(equipmentType, properties.durability(equipmentType.getDurability(durability)), insertableModifierSlots);
	}

	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		if (isTravellersArmorAndBroken(stack)) {
			return Component.translatable(this.getDescriptionId(stack)).append(Component.translatable("travellers_gear.broken").withStyle(ChatFormatting.GRAY));
		}
		return super.getName(stack);
	}

	@Override
	public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.attributeModifiers == null ? super.getDefaultAttributeModifiers() : this.attributeModifiers;
	}

	public static Properties gogglesProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(Type.HELMET).build())
			.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F);
	}

	public static Properties chestProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE, Unit.INSTANCE)
			.component(TFDataComponents.SWIFT_SWIM, Unit.INSTANCE)
			.attributes(defaultArmorProperties(Type.CHESTPLATE)
				.add(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)
				.build());
	}

	public static Properties glovesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
	}

	public static Properties wingsProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(Type.LEGGINGS).build())
			.component(TFDataComponents.TRAVELLERS_HAS_WINGS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_JUMP_AMPLIFIER, 1);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_BOOTS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_STEP, Unit.INSTANCE)
			.attributes(defaultArmorProperties(Type.BOOTS)
				.add(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)
				.build());
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);
		HolderLookup.Provider registries = context.registries();
		if (registries == null)
			return;

		List<Holder.Reference<TravellersModifier>> abilityModifiers = TravellersModifiersManager.findAllAbilityModifiers(registries, stack);
		for (Holder.Reference<TravellersModifier> travellersModifierReference : abilityModifiers) {
			tooltip.add(Component.translatable("travellers_gear.ability", TravellersModifiersManager.getModifierTooltipComponent(travellersModifierReference).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GOLD));
		}

		List<Holder.Reference<TravellersModifier>> insertableModifiers = TravellersModifiersManager.findAllInsertableModifiers(registries, stack);
		for (Holder.Reference<TravellersModifier> modifier : insertableModifiers) {
			tooltip.add(Component.literal("- ").append(TravellersModifiersManager.getModifierTooltipComponent(modifier).withStyle(ChatFormatting.GRAY)));
			if (flags.isAdvanced()) {
				for (Component description : modifier.value().getDescription()) {
					// FIXME There has to be a better way to bold only the indent and arrow and not the information component
					tooltip.add(Component.literal("").append(Component.translatable("travellers_gear.info_indent").withStyle(ChatFormatting.BOLD)).append(description));
				}
			}
		}

		for (int i = insertableModifiers.size(); i < getModifierSlots(); i++) {
			tooltip.add(Component.literal("- ").append(Component.translatable("travellers_gear.modifier.empty").withStyle(ChatFormatting.DARK_GRAY)));
		}

		if (TFItems.TRAVELLERS_GLOVES.get() == this) {
			tooltip.add(GLOVES_TOOLTIP);
		}

		if (!flags.isAdvanced()) {
			boolean hasHiddenDescriptions = java.util.stream.Stream.concat(abilityModifiers.stream(), insertableModifiers.stream()).map(Holder::value).map(TravellersModifier::getDescription).anyMatch(Predicate.not(List::isEmpty));
			if (hasHiddenDescriptions) {
				tooltip.add(Component.translatable("travellers_gear.shift_info", Component.literal("Shift").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.WHITE));
			}
		}
	}

	@Override
	public boolean isEnchantable(@NotNull ItemStack stack) {
		return false;
	}

	public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
		return true;
	}

	public boolean isPrimaryItemFor(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return false;
	}

	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return false;
	}

	public boolean isRepairable(@NotNull ItemStack stack) {
		return false;
	}

	public boolean canWalkOnPowderedSnow(ItemStack stack, @NotNull LivingEntity wearer) {
		return stack.is(TFItems.TRAVELLERS_BOOTS);
	}

	public static boolean isTravellersArmorAndBroken(ItemStack stack) {
		return stack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && stack.isDamageableItem() && stack.getMaxDamage() - 1 <= stack.getDamageValue();
	}

	// [VanillaCopy] modified ArmorItem constructor to just return default attribute modifiers
	public static ItemAttributeModifiers.Builder defaultArmorProperties(Type type) {
		int defense = TFArmorMaterials.TRAVELLERS.value().getDefense(type);
		float toughness = TFArmorMaterials.TRAVELLERS.value().toughness();
		ItemAttributeModifiers.Builder defaultArmorModifiers = ItemAttributeModifiers.builder();
		EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		defaultArmorModifiers.add(
			Attributes.ARMOR, new AttributeModifier(resourcelocation, defense, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
		);
		defaultArmorModifiers.add(
			Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, toughness, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
		);
		float knockbackResistance = TFArmorMaterials.TRAVELLERS.value().knockbackResistance();
		if (knockbackResistance > 0.0F) {
			defaultArmorModifiers.add(
				Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(resourcelocation, knockbackResistance, AttributeModifier.Operation.ADD_VALUE),
				equipmentslotgroup
			);
		}
		return defaultArmorModifiers;
	}

	public int getModifierSlots() {
		return insertableModifierSlots;
	}

	public boolean makesPiglinsNeutral(@NotNull ItemStack stack, @NotNull LivingEntity wearer) {
		return this == TFItems.TRAVELLERS_GOGGLES.get() || stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
	}
}
