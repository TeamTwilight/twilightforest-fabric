package twilightforest.compat.trinkets.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.trinkets.model.CharmOfLifeNecklaceModel;

public class CharmOfLifeNecklaceRenderer {

	private CharmOfLifeNecklaceModel model;
	private final float[] necklaceColors;

	public CharmOfLifeNecklaceRenderer(float[] rgbNecklaceColor) {
		if (rgbNecklaceColor.length != 3) throw new IllegalArgumentException("Charm of Life Curio must define 3 colors");
		this.necklaceColors = rgbNecklaceColor;
	}

	public void render(ItemStack item, SlotReference slotContext, EntityModel<? extends LivingEntity> contextModel, PoseStack stack, MultiBufferSource buffer, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (model == null) {
			model = new CharmOfLifeNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE));
		}
		if (contextModel instanceof HumanoidModel<?> model) {
			stack.pushPose();
			model.body.translateAndRotate(stack);
			stack.translate(-0.0D, 0.23D, -0.135D);
			stack.mulPose(Axis.YP.rotationDegrees(0.0F));
			stack.scale(-0.4F, -0.4F, 0.4F);
			ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
			renderer.renderItem(slotContext.inventory().getComponent().getEntity(), item, ItemDisplayContext.FIXED, false, stack, buffer, light);
			stack.popPose();
		}
		this.model.setupAnim(slotContext.inventory().getComponent().getEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.model.prepareMobModel(slotContext.inventory().getComponent().getEntity(), limbSwing, limbSwingAmount, partialTicks);
		TrinketRenderer.followBodyRotations(slotContext.inventory().getComponent().getEntity(), this.model);
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("charm_of_life_necklace.png")));
		this.model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, this.necklaceColors[0], this.necklaceColors[1], this.necklaceColors[2], 1.0F);
	}
}