package twilightforest.asm.hooks.event;

// TODO [Fabric] : Integrate these hooks into mixins and validate each one of them once the project compiles
public final class ToolEventHooks {
	/*
	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;
	*/

	/*
	private void doKnightmetalToolLogic(LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity target = event.getEntity();

			DamageContainer container = event.getContainer();
			if (!target.level().isClientSide() && container.getSource().getDirectEntity() instanceof LivingEntity living) {
				ItemStack weapon = living.getMainHandItem();

				if (!weapon.isEmpty()) {
					if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE.get()) || weapon.is(TFItems.KNIGHTMETAL_SWORD.get()))) {
						if (target.getArmorCoverPercentage() > 0) {
							int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
							container.setNewDamage(container.getNewDamage() + moreBonus);
						} else {
							container.setNewDamage(container.getNewDamage() + KNIGHTMETAL_BONUS_DAMAGE);
						}
						// enchantment attack sparkles
						((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
					} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE.get())) {
						container.setNewDamage(container.getOriginalDamage() + KNIGHTMETAL_BONUS_DAMAGE);
						// enchantment attack sparkles
						((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
					}
				}
			}
		}
	}

	private void addExtraAxeChargingDamage(LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity target = event.getEntity();
			DamageContainer container = event.getContainer();
			if (!target.level().isClientSide() && container.getSource().getDirectEntity() instanceof LivingEntity living && living.isSprinting()) {
				ItemStack weapon = living.getMainHandItem();
				if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
					container.setNewDamage(container.getNewDamage() + MINOTAUR_AXE_BONUS_DAMAGE);
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
	}
	 */
}