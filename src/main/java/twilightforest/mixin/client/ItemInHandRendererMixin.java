package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.ASMHooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;getSavedData(Ljava/lang/Integer;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void renderMapData(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, ItemStack itemStack, CallbackInfo ci, Integer integer) {
        ASMHooks.renderMapData(MapItem.getSavedData(integer, this.minecraft.level), itemStack, this.minecraft.level);
    }

    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    public boolean shouldRenderCustomMap(ItemStack itemStack, Item item) {
        return ASMHooks.shouldMapRender(itemStack.is(item), itemStack);
    }
}
