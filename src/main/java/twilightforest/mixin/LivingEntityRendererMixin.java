package twilightforest.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.item.TrophyItem;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {

	@Shadow
	private EntityModel<T> model;

	@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
	private void twilightforest$hideTrophyHead(T entity, float yRot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
		ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);
		boolean isTrophy = headStack.getItem() instanceof TrophyItem;
		boolean isPlayer = entity instanceof Player;

		if (model instanceof HeadedModel headedModel) {
			headedModel.getHead().visible = !isTrophy && (!isPlayer || headedModel.getHead().visible);
			if (model instanceof HumanoidModel<?> humanoidModel) {
				humanoidModel.hat.visible = !isTrophy && (!isPlayer || humanoidModel.hat.visible);
			}
		}
	}
}