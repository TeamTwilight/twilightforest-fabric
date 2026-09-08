package twilightforest.asm.hooks.event;

// TODO [Fabric] : Integrate these hooks into mixins and validate each one of them once the project compiles
public final class TravellersGearEventHooks {
	/*
	private void activateAndDeactivateTravellersModifiers(ItemAttributeModifierEvent event) {
		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;

		ItemStack armor = event.getItemStack();
		if (!armor.has(TFDataComponents.IS_TRAVELLERS_GEAR) || !armor.isDamageableItem())
			return;

		if (armor.getMaxDamage() - 1 <= armor.getDamageValue()) {
			if (armor.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
				Set<ItemAttributeModifiers.Entry> entries = new LinkedHashSet<>(armor.get(DataComponents.ATTRIBUTE_MODIFIERS).modifiers());
				if (armor.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES)) {
					entries.addAll(armor.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES).modifiers());
				}
				armor.set(TFDataComponents.STORED_BROKEN_ATTRIBUTES, new ItemAttributeModifiers(entries.stream().toList()));
				event.clearModifiers();
			}
		} else {
			if (armor.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES)) {
				armor.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES).modifiers().forEach(entry -> event.replaceModifier(entry.attribute(), entry.modifier(), entry.slot()));
				armor.remove(TFDataComponents.STORED_BROKEN_ATTRIBUTES);
				armor.set(DataComponents.ATTRIBUTE_MODIFIERS, event.build());
			}
		}
	}
	*/
}