package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.GiantItemRenderHelper;
import twilightforest.init.TFItems;
import twilightforest.item.GiantItem;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	@Shadow @Final private ItemModelShaper itemModelShaper;

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"))
	private void startRenderItem(ItemRenderer instance, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack matrixStack, VertexConsumer buffer, Operation<Void> original) {
		if (stack.getItem() instanceof GiantItem) {
			matrixStack.pushPose();
			GiantItemRenderHelper.handle(matrixStack);
			original.call(instance, model, stack, combinedLight, combinedOverlay, matrixStack, buffer);
			matrixStack.popPose();
		} else {
			original.call(instance, model, stack, combinedLight, combinedOverlay, matrixStack, buffer);
		}
	}

	@ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
	private BakedModel render(BakedModel value, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		if (!(displayContext == ItemDisplayContext.GUI)) {
			return value;
		}
		boolean sword;
		if (itemStack.is(TFItems.GIANT_SWORD.get())) {
			sword = true;
		} else if (itemStack.is(TFItems.GIANT_PICKAXE.get())) {
			sword = false;
		} else {
			return value;
		}
		String path;
		if (sword) {
			path = "giant_sword_gui";
		} else {
			path = "giant_pickaxe_gui";
		}
		return itemModelShaper.getModelManager().getModel(new ModelResourceLocation(TwilightForestMod.ID, path, "inventory"));
	}
}
