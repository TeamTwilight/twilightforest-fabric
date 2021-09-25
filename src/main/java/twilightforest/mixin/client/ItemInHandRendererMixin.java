package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.ASMHooks;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    private static ItemStack capturedStack = null;

    @Inject(method = "renderMap", at = @At("HEAD"))
    public void renderMapHead(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, ItemStack stack, CallbackInfo ci) {
        capturedStack = stack;
    }

    @Redirect(method = "renderMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;getSavedData(Ljava/lang/Integer;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;"))
    public MapItemSavedData renderMapData(Integer mapId, Level level) {
        return ASMHooks.renderMapData(MapItem.getSavedData(capturedStack, level), capturedStack, level);
    }

    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    public boolean shouldRenderCustomMap(ItemStack itemStack, Item item) {
        return ASMHooks.shouldMapRender(itemStack.is(item), itemStack);
    }
}
