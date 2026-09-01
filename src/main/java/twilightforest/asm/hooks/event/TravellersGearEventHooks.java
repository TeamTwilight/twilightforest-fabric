package twilightforest.asm.hooks.event;

// TODO [Fabric] : Integrate these hooks into mixins and validate each one of them once the project compiles
public final class TravellersGearEventHooks {
	/*
	private void reduceSlimySolesFallDamage(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Float coefficient = boots.get(TFDataComponents.SLIMY_SOLES_COEFFICIENT);
		SlimySolesAttachment slimySolesAttachment = livingEntity.getData(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
		if (!livingEntity.isShiftKeyDown() && TravellersModifiersManager.isModifierActive(livingEntity, boots, TravellersModifiersManager.SLIMY_SOLES_MODIFIER) && coefficient != null && (calculateFallDamage(event) > 0 || slimySolesAttachment.forceBounce)) {
			event.setCanceled(true);
			slimySolesAttachment.bounceVelocity = -livingEntity.getDeltaMovement().y() * Math.sqrt(coefficient);
			slimySolesAttachment.doubleJumpBoostVelocity = slimySolesAttachment.bounceVelocity;
			slimySolesAttachment.hasBounced = false;
			livingEntity.setData(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
		}
	}

	// [VanillaCopy]
	private double calculateFallDamage(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		double safeFallDistance = livingEntity.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
		double unsafeFallDistance = event.getDistance() - safeFallDistance;
		return Mth.ceil(unsafeFallDistance * event.getDamageMultiplier() * livingEntity.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
	}

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

	private void cancelPhantomSpawns(PlayerSpawnPhantomsEvent event) {
		if (TravellersModifiersManager.isModifierActive(event.getEntity(), TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}
	 */
}