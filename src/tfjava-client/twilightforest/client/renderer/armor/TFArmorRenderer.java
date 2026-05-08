package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.FieryArmorModel;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.client.model.armor.YetiArmorModel;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.ArcticArmorItem;
import twilightforest.item.travellers_gear.TravellersArmorItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class TFArmorRenderer implements ArmorRenderer {
	private static final Map<ModelLayerLocation, ModelPart> ARMOR_MODELS = new HashMap<>();

	protected static ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.computeIfAbsent(layerLocation, layer -> Minecraft.getInstance().getEntityModels().bakeLayer(layer));
	}

	public static void resetModelCache() {
		ARMOR_MODELS.clear();
	}

	public static void bootstrap() {
		ArmorRenderer.register(new ArcticRenderer(),
				TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		ArmorRenderer.register(new TFSimpleArmorRenderer(FieryArmorModel::new, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER, "fiery"),
				TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get());
		ArmorRenderer.register(new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, "knightmetal"),
				TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get());
		ArmorRenderer.register(new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER, "phantom"),
				TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		ArmorRenderer.register(new TFSimpleArmorRenderer(YetiArmorModel::new, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER, "yeti"),
				TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get());
		ArmorRenderer.register(new TravellersArmorRenderer(),
				TFItems.TRAVELLERS_GOGGLES.get(), TFItems.TRAVELLERS_VEST.get(), TFItems.TRAVELLERS_GLOVES.get(), TFItems.TRAVELLERS_WINGS.get(), TFItems.TRAVELLERS_BELT.get(), TFItems.TRAVELLERS_BOOTS.get());
	}

	@Override
	public final void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		HumanoidModel<LivingEntity> model = this.createModel(entity, stack, slot, contextModel);
		contextModel.copyPropertiesTo(model);
		setSlotVisible(model, slot);
		this.setupModelAnimations(entity, stack, slot, model);
		this.renderModel(matrices, vertexConsumers, light, stack, slot, model, entity);
	}

	protected abstract HumanoidModel<LivingEntity> createModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> contextModel);

	protected void setupModelAnimations(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> model) {
	}

	protected void renderModel(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> model, LivingEntity entity) {
		ResourceLocation texture = armorTexture(this.texturePrefix(stack, slot, entity), slot, false);
		renderTinted(matrices, vertexConsumers, light, stack, model, texture, 0xFFFFFFFF);
	}

	protected String texturePrefix(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
		return "ironwood";
	}

	protected static ResourceLocation armorTexture(String prefix, EquipmentSlot slot, boolean overlay) {
		int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
		return TwilightForestMod.prefix("textures/models/armor/" + prefix + "_layer_" + layer + (overlay ? "_overlay" : "") + ".png");
	}

	protected static void renderTinted(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack, Model model, ResourceLocation texture, int color) {
		VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(texture), stack.hasFoil());
		model.renderToBuffer(matrices, consumer, light, OverlayTexture.NO_OVERLAY, color);
	}

	protected static void setSlotVisible(HumanoidModel<?> model, EquipmentSlot slot) {
		model.setAllVisible(false);
		switch (slot) {
			case HEAD -> {
				model.head.visible = true;
				model.hat.visible = true;
			}
			case CHEST -> {
				model.body.visible = true;
				model.rightArm.visible = true;
				model.leftArm.visible = true;
			}
			case LEGS -> {
				model.body.visible = true;
				model.rightLeg.visible = true;
				model.leftLeg.visible = true;
			}
			case FEET -> {
				model.rightLeg.visible = true;
				model.leftLeg.visible = true;
			}
			default -> {
			}
		}
	}

	private static final class ArcticRenderer extends TFSimpleArmorRenderer {
		private ArcticRenderer() {
			super(TFArmorModel::new, TFModelLayers.ARCTIC_ARMOR_INNER, TFModelLayers.ARCTIC_ARMOR_OUTER, "arctic");
		}

		@Override
		protected void renderModel(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> model, LivingEntity entity) {
			int color = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, ArcticArmorItem.DEFAULT_COLOR));
			renderTinted(matrices, vertexConsumers, light, stack, model, arcticTexture(slot, "dyed"), color);
			renderTinted(matrices, vertexConsumers, light, stack, model, arcticTexture(slot, "overlay"), 0xFFFFFFFF);
		}

		private static ResourceLocation arcticTexture(EquipmentSlot slot, String suffix) {
			int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
			return TwilightForestMod.prefix("textures/models/armor/arctic_layer_" + layer + "_" + suffix + ".png");
		}
	}

	private static final class TravellersArmorRenderer extends TFArmorRenderer {
		@Override
		protected HumanoidModel<LivingEntity> createModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> contextModel) {
			ModelPart root = switch (slot) {
				case HEAD -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = getModelPart(isModelSlim(entity) ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
					boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
					chestLayer.getChild("body").skipDraw = !hasChestplate;
					chestLayer.getChild("left_arm").skipDraw = !hasGloves;
					chestLayer.getChild("right_arm").skipDraw = !hasGloves;
					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
					boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT) || TravellersModifiersManager.hasTravellersModifier(entity.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);
					TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersWingsModel.skipWings(leggingsLayer, !hasWings);
					yield leggingsLayer;
				}
				case FEET -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
				default -> throw new IllegalArgumentException("Unexpected armor slot: " + slot + ": " + stack);
			};

			return slot == EquipmentSlot.LEGS ? new TravellersWingsModel(root) : new TFArmorModel(root);
		}

		@Override
		protected void setupModelAnimations(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> model) {
			if (model instanceof TravellersWingsModel wingsModel) {
				float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
				wingsModel.setupModelAnimations(entity, entity.walkAnimation.position(partialTick), entity.walkAnimation.speed(partialTick), entity.tickCount + partialTick, entity.getYRot(), entity.getXRot());
			}
		}

		@Override
		protected String texturePrefix(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
			if (slot != EquipmentSlot.LEGS && TFDataAttachments.get(entity, TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER)) {
				return "travellers_down";
			}
			return "travellers";
		}

		@Override
		protected void renderModel(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> model, LivingEntity entity) {
			ResourceLocation texture = slot != EquipmentSlot.LEGS && TFDataAttachments.get(entity, TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER)
					? TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png")
					: armorTexture("travellers", slot, false);
			renderTinted(matrices, vertexConsumers, light, stack, model, texture, 0xFFFFFFFF);
		}

		private static boolean isModelSlim(LivingEntity entity) {
			if (entity instanceof net.minecraft.client.player.AbstractClientPlayer player) {
				return player.getSkin().model().equals(net.minecraft.client.resources.PlayerSkin.Model.SLIM);
			}
			return false;
		}
	}
}
