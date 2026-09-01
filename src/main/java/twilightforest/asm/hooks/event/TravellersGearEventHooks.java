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

	private void removeModifiersFromTravellersGear(GrindstoneEvent.OnPlaceItem event) {
		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;
		RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
		List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
			.toList();

		if (travellersItemStacks.isEmpty())
			return; // Delegate to vanilla logic
		if (travellersItemStacks.size() > 1) {
			event.setCanceled(true);
			return;
		}
		ItemStack inputStack = travellersItemStacks.getFirst();
		List<Holder.Reference<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(access, inputStack);
		if (modifiers.isEmpty()) {
			event.setCanceled(true);
			return;
		}

		ItemStack unmodifiedStack = inputStack.copy();
		modifiers.forEach(modifier -> ((InsertableTravellersModifier) modifier.value()).removeModifier(unmodifiedStack));
		event.setOutput(unmodifiedStack.copy());
	}

	private void extractItemsFromSwapHotbarModifier(GrindstoneEvent.OnTakeItem event) {
		returnModifierItems(event,
			TravellersModifiersManager.SWAP_HOTBAR_MODIFIER,
			DataComponents.CONTAINER,
			ItemContainerContents::nonEmptyItemCopyStream
		);

		returnModifierItems(event,
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER,
			TFDataComponents.ITEM_DISPLAY.get(),
			contents -> contents.items().stream()
		);
	}

	private <T> void returnModifierItems(GrindstoneEvent.OnTakeItem event, ResourceKey<TravellersModifier> modifierKey, DataComponentType<T> componentType, Function<T, Stream<ItemStack>> itemStreamExtractor) {
		getUniqueTravellersGear(event.getTopItem(), event.getBottomItem(), stack ->
			TravellersModifiersManager.hasTravellersModifier(event.getPlayer().registryAccess(), stack, modifierKey)
		).map(stack -> stack.get(componentType))
			.ifPresent(component ->
				itemStreamExtractor.apply(component)
					.forEach(itemStack -> InventoryUtil.giveItemToPlayer(event.getPlayer(), itemStack))
			);
	}

	private Optional<ItemStack> getUniqueTravellersGear(ItemStack top, ItemStack bottom, Predicate<ItemStack> predicate) {
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
			.filter(predicate)
			.toList();
		return travellersItemStacks.size() == 1 ? Optional.of(travellersItemStacks.getFirst()) : Optional.empty();
	}

	private void cancelPhantomSpawns(PlayerSpawnPhantomsEvent event) {
		if (TravellersModifiersManager.isModifierActive(event.getEntity(), TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}

	private void fireCraftingModifierTrigger(PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			ItemStack compareStack = ItemStack.EMPTY;
			for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
				if (event.getInventory().getItem(i).is(event.getCrafting().getItem())) compareStack = event.getInventory().getItem(i);
			}

			if (!compareStack.isEmpty()) {
				var oldMods = TravellersModifiersManager.findAllInsertableModifiers(player, compareStack);
				TravellersModifiersManager.findAllInsertableModifiers(player, event.getCrafting()).stream()
					.filter(modifier -> !oldMods.contains(modifier)).toList()
					.forEach(modifier -> TFAdvancements.ADD_MODIFIER.get().trigger(player, modifier.key().identifier()));
			}
		}
	}
	 */
}