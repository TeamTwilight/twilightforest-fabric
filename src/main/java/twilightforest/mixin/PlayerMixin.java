package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.TFEventListener;
import twilightforest.TFTickHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.block.ThornsBlock;
import twilightforest.client.model.entity.PartEntity;

@Mixin(Player.class)
@Debug(export = true)
public class PlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void playerTickEnd(CallbackInfo ci) {
        TFTickHandler.playerTick((Player) (Object) this);
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", ordinal = 0), cancellable = true)
    public void entityHurt(DamageSource damageSrc, float damageAmount, CallbackInfo ci) {
        if (!TFEventListener.entityHurts((LivingEntity) (Object) this, damageSrc))
            ci.cancel();
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void onAttack(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!TFEventListener.livingAttack((LivingEntity) (Object) this, source))
            cir.cancel();
    }

    /*
    @ModifyVariable(method = "attack", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Enity", ordinal = 0) )
    public void attack(Entity targetEntity, CallbackInfo ci) {
        if (targetEntity instanceof PartEntity) {
            entity = ((PartEntity<?>) targetEntity).getParent();
        }
        else{

        }
    }
     */
    /*
    @Inject(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Player;getMainHandItem()Lnet/minecraft/world/item/ItemStack;"))
    public void attack(Entity targetEntity, CallbackInfo ci) {
        Player player = ((Player) (Object) this);

        ItemStack itemStack2 = player.getMainHandItem();
        Entity entity = targetEntity;
        if (targetEntity instanceof PartEntity) {
            entity = ((PartEntity<?>) targetEntity).getParent();
        }

        if (!player.level.isClientSide && !itemStack2.isEmpty() && entity instanceof LivingEntity) {
            itemStack2.hurtEnemy((LivingEntity)entity, player);
            if (itemStack2.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
        }

    }

     */

    @ModifyVariable(method = "attack", at=@At(value = "STORE"), ordinal = 1)//@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonPart", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private Entity attack(Entity target) {
        if (target instanceof PartEntity<?> mpE) {
            return mpE.getParent();
        }
        return target;
    }

}
