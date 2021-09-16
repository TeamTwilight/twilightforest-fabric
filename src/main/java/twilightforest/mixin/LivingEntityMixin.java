package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.TFEventListener;

import java.util.Map;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void livingUpdate(CallbackInfo ci) {
        TFEventListener.livingUpdate((LivingEntity) (Object) this);
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", ordinal = 0), cancellable = true)
    public void entityHurt(DamageSource damageSrc, float damageAmount, CallbackInfo ci) {
        if(!TFEventListener.entityHurts((LivingEntity) (Object) this, damageSrc))
            ci.cancel();
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void onAttack(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(!TFEventListener.livingAttack((LivingEntity) (Object) this, source)) cir.cancel();
    }

    @Inject(method = "collectEquipmentChanges", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void armorUpdate(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir, Map<EquipmentSlot, ItemStack> map,  EquipmentSlot[] var2, int var4, int var3, EquipmentSlot equipmentSlot, ItemStack itemStack, ItemStack itemStack1) {
        TFEventListener.armorChanged((LivingEntity) (Object) this, itemStack, itemStack1);
    }
}
