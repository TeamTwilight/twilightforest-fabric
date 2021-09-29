package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.ASMHooks;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;


@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private ItemStack offHandItem;

    @Shadow protected abstract void renderTwoHandedMap(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, float pitch, float equippedProgress, float swingProgress);

    @Shadow protected abstract void renderOneHandedMap(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, float equippedProgress, HumanoidArm hand, float swingProgress, ItemStack stack);

    private static ItemStack capturedStack = null;

    @Inject(method = "renderMap", at = @At("HEAD"))
    public void renderMapHead(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, ItemStack stack, CallbackInfo ci) {
        capturedStack = stack;
    }

    @ModifyVariable(method = "renderMap", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/MapItem;getSavedData(Ljava/lang/Integer;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;"))
    public MapItemSavedData renderMapData(MapItemSavedData mapItemSavedData) {
        return ASMHooks.renderMapData(MapItem.getSavedData(capturedStack, minecraft.level), capturedStack, minecraft.level);
    }


    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER),  cancellable = true)
    public void shouldRenderCustomMap(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        ASMHooks.shouldMapRender(stack.is(Items.FILLED_MAP), stack);
        boolean bl = hand == InteractionHand.MAIN_HAND;
        HumanoidArm humanoidArm = bl ? player.getMainArm() : player.getMainArm().getOpposite();

        if (ASMHooks.shouldMapRender(stack.is(Items.FILLED_MAP), stack)) {
            if (bl && this.offHandItem.isEmpty()) {
                this.renderTwoHandedMap(matrixStack, buffer, combinedLight, pitch, equippedProgress, swingProgress);
            } else {
                this.renderOneHandedMap(matrixStack, buffer, combinedLight, equippedProgress, humanoidArm, swingProgress, stack);
            }
        }

        matrixStack.popPose();
        ci.cancel();
    }
}