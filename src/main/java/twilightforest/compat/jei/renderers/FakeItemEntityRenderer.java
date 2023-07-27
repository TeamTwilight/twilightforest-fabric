package twilightforest.compat.jei.renderers;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import twilightforest.TwilightForestMod;
import twilightforest.compat.jei.FakeItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class FakeItemEntityRenderer implements IIngredientRenderer<FakeItemEntity> {

	private final float bobOffs;
	private final int size;

	public FakeItemEntityRenderer(int size) {
		this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
		this.size = size;
	}

	@Override
	public int getWidth() {
		return this.size;
	}

	@Override
	public int getHeight() {
		return this.size;
	}

	@Override
	public void render(GuiGraphics graphics, @Nullable FakeItemEntity item) {
		Level level = Minecraft.getInstance().level;
		if (item != null && level != null) {
			try {
				PoseStack modelView = RenderSystem.getModelViewStack();
				modelView.pushPose();
				modelView.mulPoseMatrix(graphics.pose().last().pose());
				this.renderItemEntity(item.stack(), level);
				modelView.popPose();
				RenderSystem.applyModelViewMatrix();
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing item in JEI!", e);
			}
		}
	}

	@Override
	public List<Component> getTooltip(FakeItemEntity item, TooltipFlag flag) {
		List<Component> tooltip = new ArrayList<>();
		tooltip.add(item.stack().getItem().getDescription());
		if (flag.isAdvanced()) {
			tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.stack().getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
		}
		return tooltip;
	}

	private void renderItemEntity(ItemStack stack, Level level) {
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(16.0D, 32.0D, 1050.0D);
		posestack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		posestack1.translate(0.0D, 0.0D, 1000.0D);
		posestack1.scale(50.0F, 50.0F, 50.0F);
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
		posestack1.mulPose(quaternion);
		posestack1.mulPose(Axis.XN.rotationDegrees(35.0F));
		posestack1.mulPose(Axis.YN.rotationDegrees(145.0F));
		Lighting.setupForEntityInInventory();
		quaternion1.conjugate();
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		ItemEntity item = EntityType.ITEM.create(level);
		Objects.requireNonNull(item).setItem(stack);
		RenderSystem.runAsFancy(() ->
				this.render(item, Minecraft.getInstance().getDeltaFrameTime(), posestack1, buffer, 15728880));
		buffer.endBatch();
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}

	//[VanillaCopy] of ItemEntityRenderer.render. I have to add my own bob offset and ticker since using the vanilla method has issues
	public void render(ItemEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		ItemStack itemstack = entity.getItem();
		BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, entity.level(), null, entity.getId());
		float f1 = Mth.sin((Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 10.0F + this.bobOffs) * 0.1F + 0.1F;
		float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
		stack.translate(0.0D, f1 + 0.25F * f2, 0.0D);
		float f3 = this.getSpin(partialTicks);
		stack.mulPose(Axis.YP.rotation(f3));

		stack.pushPose();

		Minecraft.getInstance().getItemRenderer().render(itemstack, ItemDisplayContext.GROUND, false, stack, buffer, light, OverlayTexture.NO_OVERLAY, bakedmodel);
		stack.popPose();


		stack.popPose();
	}

	public float getSpin(float pPartialTicks) {
		return (Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + pPartialTicks) / 20.0F + this.bobOffs;
	}
}
