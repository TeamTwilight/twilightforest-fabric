package twilightforest.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import twilightforest.init.TFDataComponents;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@ModifyArgs(
		method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V"
		)
	)
	private void twilightforest$stopDamagingTravellersGear(
		Args args
	) {
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
}
