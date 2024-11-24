package twilightforest.compat.emi;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.runtime.EmiDrawContext;
import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// based on EntityRenderer from JEI compat package
public class EMIEntityWidget extends Widget {
	private final Entity entity;
	private final EntityType<?> type;
	private final int size;
	private final Bounds bounds;

	private boolean invalid = false;

	public EMIEntityWidget(EntityType<?> type, int x, int y, int size) {
		this.entity = type.create(Objects.requireNonNull(Minecraft.getInstance().level));
		this.type = type;
		this.size = size;
		this.bounds = new Bounds(x, y, size, size);
	}

	@Override
	public Bounds getBounds() {
		return bounds;
	}

	@Override
	public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
		List<ClientTooltipComponent> tooltip = new ArrayList<>();
		FormattedCharSequence typeName = type.getDescription().getVisualOrderText();
		tooltip.add(ClientTooltipComponent.create(typeName));
		if (Minecraft.getInstance().options.advancedItemTooltips) {
			String id = BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();
			FormattedCharSequence text = Component.literal(id).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText();
			tooltip.add(ClientTooltipComponent.create(text));
		}
		return tooltip;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		// only can draw living entities, plus non-living ones don't get recipes anyways
		if (!invalid && entity instanceof LivingEntity livingEntity) {
			// scale down large mobs, but don't scale up small ones
			int scale = this.size / 2;
			float height = entity.getBbHeight();
			float width = entity.getBbWidth();
			if (height > 2.25F || width > 2.25F) {
				scale = (int) (20 / Math.max(height, width));
			}
			// catch exceptions drawing the entity to be safe, any caught exceptions blacklist the entity
			try {
				PoseStack modelView = RenderSystem.getModelViewStack();
				modelView.pushPose();
				modelView.mulPoseMatrix(guiGraphics.pose().last().pose());
				modelView.translate(bounds.x(), bounds.y(), 0);
				this.renderTheEntity(this.size / 2, this.size - 2, scale, livingEntity);
				modelView.popPose();
				RenderSystem.applyModelViewMatrix();
				if (bounds.contains(mouseX, mouseY)) {
					RenderSystem.disableDepthTest();
					EmiRenderHelper.drawSlotHightlight(EmiDrawContext.wrap(guiGraphics), bounds.x(), bounds.y(), bounds.width(), bounds.height(), 0);
					RenderSystem.enableDepthTest();
				}
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing entity {}", BuiltInRegistries.ENTITY_TYPE.getKey(type), e);
				this.invalid = true;
			}
		} else {
			// not living, so might as well skip next time
			TwilightForestMod.LOGGER.error("Error drawing entity %s: not a LivingEntity".formatted(BuiltInRegistries.ENTITY_TYPE.getKey(type)));
			this.invalid = true;
		}
	}

	//[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
	private void renderTheEntity(int x, int y, int scale, LivingEntity entity) {
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(x, y, 1050.0D);
		if (entity.getType() == EntityType.GHAST) posestack.translate(0.0D, -8.5D, 0.0D);
		posestack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		posestack1.translate(0.0D, 0.0D, 1000.0D);
		posestack1.scale((float) scale, (float) scale, (float) scale);
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
		posestack1.mulPose(quaternion);
		posestack1.mulPose(Axis.XN.rotationDegrees(35.0F));
		posestack1.mulPose(Axis.YN.rotationDegrees(145.0F));
		float f2 = entity.yBodyRot;
		float f3 = entity.getYRot();
		float f4 = entity.getXRot();
		float f5 = entity.yHeadRotO;
		float f6 = entity.yHeadRot;
		entity.yBodyRot = 0.0F;
		entity.setYRot(0.0F);
		entity.setXRot(0.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();
		Lighting.setupForEntityInInventory();
		EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion1.conjugate();
		entityrenderdispatcher.overrideCameraOrientation(quaternion1);
		entityrenderdispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
			if (entity instanceof MultiPartEntity multiPartEntity && multiPartEntity.isMultipartEntity()) {
				Arrays.stream(multiPartEntity.getParts())
						.filter(Objects::nonNull)
						.forEach(partEntity ->
								entityrenderdispatcher.render(partEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
			}
		});
		multibuffersource$buffersource.endBatch();
		entityrenderdispatcher.setRenderShadow(true);
		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}
}
