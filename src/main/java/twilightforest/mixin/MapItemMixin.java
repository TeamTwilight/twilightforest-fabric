package twilightforest.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;
import twilightforest.extensions.IMapItemEx;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@Mixin(MapItem.class)
public abstract class MapItemMixin implements IMapItemEx {
    @Shadow
    @Nullable
    public static Integer getMapId(ItemStack itemStack) {
        return null;
    }

    @Shadow
    @Nullable
    public static MapItemSavedData getSavedData(@Nullable Integer integer, Level level) {
        return null;
    }

    @Inject(method = "getSavedData(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;", at = @At("HEAD"), cancellable = true)
    private static void injectCustomMapData(ItemStack itemStack, Level level, CallbackInfoReturnable<MapItemSavedData> cir) {
        Item map = itemStack.getItem();
        if(map instanceof MapItem) {
            cir.setReturnValue(ASMHooks.renderMapData(((IMapItemEx)map).getCustomMapData(itemStack, level), itemStack, level));
        }
        cir.setReturnValue(null);
    }

    @Override
    public MapItemSavedData getCustomMapData(ItemStack stack, Level level) {
        Integer integer = getMapId(stack);
        return getSavedData(integer, level);
    }
}
