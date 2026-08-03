package twilightforest.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.ArmorHooks;
import twilightforest.asmhooks.EntityHooks;
import twilightforest.compat.trinkets.TrinketsCompat;
import twilightforest.init.TFBlocks;
import twilightforest.network.CreateMovingCicadaSoundPacket;
import twilightforest.network.PacketDistributor;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
	private void twilightforest$modifyArmorVisibility(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(ArmorHooks.modifyArmorVisibility(cir.getReturnValue(), (LivingEntity) (Object) this));
	}

	@Inject(method = "canStandOnFluid", at = @At("RETURN"), cancellable = true)
	private void twilightforest$processWaterWalking(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(EntityHooks.processWaterWalking(cir.getReturnValue(), (LivingEntity) (Object) this, fluidState));
	}

	@Inject(
		method = "onEquipItem(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
		at = @At("TAIL")
	)
	private void twilightforest$updateCicadaSoundsOnHead(
		EquipmentSlot slot,
		ItemStack oldItem,
		ItemStack newItem,
		CallbackInfo ci
	) {
		LivingEntity self = (LivingEntity) (Object) this;

		if (self.level().isClientSide() || slot != EquipmentSlot.HEAD || !newItem.is(TFBlocks.CICADA.asItem())) {
			return;
		}

		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			try {
				if (TrinketsCompat.isTrinketEquipped(self, stack -> stack.is(TFBlocks.CICADA.asItem()))) return;
			} catch (NoClassDefFoundError ignored) {}
		}

		PacketDistributor.sendToPlayersTrackingEntityAndSelf(self, new CreateMovingCicadaSoundPacket(self.getId()));
	}
}