package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
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

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	@Final
	private ItemModelShaper itemModelShaper;

	@ModifyVariable(
		method = "render",
		at = @At("HEAD"),
		argsOnly = true
	)
	private BakedModel twilightforest$replaceGiantToolGuiModel(
		BakedModel model,
		ItemStack itemStack,
		ItemDisplayContext displayContext,
		boolean leftHand,
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int combinedLight,
		int combinedOverlay
	) {
		if (displayContext != ItemDisplayContext.GUI) return model;

		boolean sword;
		if (itemStack.is(TFItems.GIANT_SWORD.get())) {
			sword = true;
		} else if (itemStack.is(TFItems.GIANT_PICKAXE.get())) {
			sword = false;
		} else {
			return model;
		}

		String path = sword ? "giant_sword_gui" : "giant_pickaxe_gui";
		ResourceLocation id = TwilightForestMod.prefix(path);

		return itemModelShaper
			.getModelManager()
			.getModel(ModelResourceLocation.inventory(id));
	}
}