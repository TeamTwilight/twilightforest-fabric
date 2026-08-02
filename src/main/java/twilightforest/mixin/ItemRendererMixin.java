package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	@Shadow @Final private ItemModelShaper itemModelShaper;

	/**
	 * Replace the model for giant tools in GUI context to show the zoomed blade/head texture.
	 */
	@ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
	private BakedModel tf_replaceGiantToolGuiModel(BakedModel model, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		if (displayContext != ItemDisplayContext.GUI) {
			return model;
		}
		boolean sword;
		if (stack.is(TFItems.GIANT_SWORD.get())) {
			sword = true;
		} else if (stack.is(TFItems.GIANT_PICKAXE.get())) {
			sword = false;
		} else {
			return model;
		}
		String path = sword ? "giant_sword_gui" : "giant_pickaxe_gui";
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, path);
		return itemModelShaper.getModelManager().getModel(ModelResourceLocation.inventory(id));
	}
}