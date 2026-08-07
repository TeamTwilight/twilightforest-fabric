package twilightforest.mixin;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.TravellersArmorItem;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@ModifyArgs(
		method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V"
		)
	)
	private void twilightforest$stopDamagingTravellersGear(Args args) {
		ItemStack self = (ItemStack) (Object) this;

		if (!self.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
			return;

		int amount = args.get(0);
		ServerPlayer player = args.get(2);

		int currentDamage = self.getDamageValue();
		int newDamage = currentDamage + amount;

		if (newDamage >= self.getMaxDamage()) {
			args.set(0, self.getMaxDamage() - currentDamage - 1);
		} else if (newDamage >= self.getMaxDamage() - 1 && player != null) {
			player.playNotifySound(
				SoundEvents.ITEM_BREAK,
				SoundSource.PLAYERS,
				1.0F,
				player.getVoicePitch()
			);
		}
	}

	@Inject(
		method = "setDamageValue(I)V",
		at = @At("TAIL")
	)
	private void twilightforest$syncBrokenTravellersGearAttributes(
		int damage,
		CallbackInfo ci
	) {
		ItemStack self = (ItemStack) (Object) this;
		if (!self.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()) || !self.isDamageableItem())
			return;

		boolean broken = self.getMaxDamage() - 1 <= self.getDamageValue();
		boolean stored = self.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get());
		if (broken && !stored) {
			self.set(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get(),
				self.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY));
			self.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		} else if (!broken && stored) {
			self.set(DataComponents.ATTRIBUTE_MODIFIERS, self.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get()));
			self.remove(TFDataComponents.STORED_BROKEN_ATTRIBUTES.get());
		}
	}

	@Inject(
		method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$stopBrokenTravellersGearModifiers(
		EquipmentSlotGroup slotGroup,
		BiConsumer<Holder<Attribute>, AttributeModifier> consumer,
		CallbackInfo ci
	) {
		if (TravellersArmorItem.isTravellersArmorAndBroken((ItemStack) (Object) this))
			ci.cancel();
	}

	@Inject(
		method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$stopBrokenTravellersGearModifiers(
		EquipmentSlot slot,
		BiConsumer<Holder<Attribute>, AttributeModifier> consumer,
		CallbackInfo ci
	) {
		if (TravellersArmorItem.isTravellersArmorAndBroken((ItemStack) (Object) this))
			ci.cancel();
	}
}