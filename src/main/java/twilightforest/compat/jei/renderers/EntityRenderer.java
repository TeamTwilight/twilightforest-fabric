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
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import twilightforest.TwilightForestMod;

import java.util.*;

@SuppressWarnings({"rawtypes", "deprecation"})
//yoinked from TCon, entity melting rendering looks so good and I couldnt be bothered to write my own system
public class EntityRenderer implements IIngredientRenderer<EntityType> {

	private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();
	private final int size;
	private final Map<EntityType<?>, Entity> ENTITY_MAP = new HashMap<>();

	public EntityRenderer(int size) {
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
	public void render(GuiGraphics graphics, @Nullable EntityType type) {
		if (type != null) {
			Level level = Minecraft.getInstance().level;
			if (level != null && !IGNORED_ENTITIES.contains(type)) {
				Entity entity;
				// players cannot be created using the type, but we can use the client player
				// side effect is it renders armor/items
				if (type == EntityType.PLAYER) {
					entity = Minecraft.getInstance().player;
				} else {
					// entity is created with the client world, but the entity map is thrown away when JEI restarts so they should be okay I think
					entity = this.ENTITY_MAP.computeIfAbsent(type, t -> t.create(level));
				}
				// only can draw living entities, plus non-living ones don't get recipes anyways
				if (entity instanceof LivingEntity livingEntity) {
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
						modelView.mulPoseMatrix(graphics.pose().last().pose());
						this.renderTheEntity(this.size / 2, this.size - 2, scale, livingEntity);
						modelView.popPose();
						RenderSystem.applyModelViewMatrix();
					} catch (Exception e) {
						TwilightForestMod.LOGGER.error("Error drawing entity " + BuiltInRegistries.ENTITY_TYPE.getKey(type), e);
						IGNORED_ENTITIES.add(type);
						this.ENTITY_MAP.remove(type);
					}
				} else {
					// not living, so might as well skip next time
					IGNORED_ENTITIES.add(type);
					this.ENTITY_MAP.remove(type);
				}
			}
		}
	}

	@Override
	public List<Component> getTooltip(EntityType type, TooltipFlag flag) {
		List<Component> tooltip = new ArrayList<>();
		tooltip.add(type.getDescription());
		if (flag.isAdvanced()) {
			tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(type)).toString()).withStyle(ChatFormatting.DARK_GRAY));
		}
		return tooltip;
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
		RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
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