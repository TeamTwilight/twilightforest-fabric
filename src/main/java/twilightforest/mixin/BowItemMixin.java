package twilightforest.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.extensions.IBowItemEx;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.item.EnderBowItem;
import twilightforest.item.IceBowItem;
import twilightforest.item.SeekerBowItem;

@Mixin(BowItem.class)
public class BowItemMixin implements IBowItemEx {

    @ModifyVariable(method = "releaseUsing", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ArrowItem;createArrow(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/projectile/AbstractArrow;"))
    public AbstractArrow customArrowTest(AbstractArrow oldArrow){
        Item bowItem = oldArrow.getOwner() instanceof Player owner ? owner.getMainHandItem().getItem() : null;

        if(bowItem != null) {
            if(bowItem instanceof EnderBowItem || bowItem instanceof IceBowItem || bowItem instanceof SeekerBowItem){
                return customArrow(oldArrow);
            }
        }

        return oldArrow;
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }
}
