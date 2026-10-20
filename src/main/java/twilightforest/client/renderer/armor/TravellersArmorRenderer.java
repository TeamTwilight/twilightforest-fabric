package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.HashMap;
import java.util.Map;

public final class TravellersArmorRenderer extends TFArmorRenderer {
	private final Map<ChestVariant, ModelPart> CHEST_VARIANTS = new HashMap<>();
	private final Map<WingsVariant, ModelPart> WINGS_VARIANTS = new HashMap<>();

	public TravellersArmorRenderer(EntityRendererProvider.Context context) {
		super(context, TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
		INSTANCES.add(this); // TODO: This is probably not the best way to make the model available for TravellersClientEvents
	}

	@Override
	public void resetModelCache() {
		super.resetModelCache();
		this.CHEST_VARIANTS.clear();
		this.WINGS_VARIANTS.clear();
	}

	@Override
	public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (equippable == null)
			return;

		ModelPart root = switch (slot) {
			case HEAD -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
			case CHEST -> {
				boolean slim = this.isModelSlim(contextModel);
				boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
				boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);

				yield this.CHEST_VARIANTS.computeIfAbsent(new ChestVariant(slim, hasChestplate, hasGloves), variant -> {
					ModelPart chestLayer = bakeLayer(variant.slim() ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					chestLayer.getChild("body").skipDraw = !variant.hasChestplate();
					chestLayer.getChild("left_arm").skipDraw = !variant.hasGloves();
					chestLayer.getChild("right_arm").skipDraw = !variant.hasGloves();
					return chestLayer;
				});
			}
			case LEGS -> {
				boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
				boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT) || TravellersModifiersManager.hasTravellersModifier(Minecraft.getInstance().level.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);

				yield this.WINGS_VARIANTS.computeIfAbsent(new WingsVariant(hasWings, hasBelt), variant -> {
					ModelPart leggingsLayer = bakeLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					TravellersWingsModel.skipBelt(leggingsLayer, !variant.hasBelt());
					TravellersWingsModel.skipWings(leggingsLayer, !variant.hasWings());
					return leggingsLayer;
				});
			}
			case FEET -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
			default -> null;
		};

		if (root == null)
			return;

		HumanoidModel<HumanoidRenderState> model;
		if (slot == EquipmentSlot.LEGS) {
			model = new TravellersWingsModel(root);
		} else {
			TFArmorModel armorModel = new TFArmorModel(root);
			armorModel.setSlot(slot);
			model = armorModel;
		}

		EquipmentClientInfo.LayerType layerType = humanoidRenderState.isBaby
			&& humanoidRenderState.entityType != EntityType.ARMOR_STAND
			? EquipmentClientInfo.LayerType.HUMANOID_BABY
			: slot == EquipmentSlot.LEGS
			? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
			: EquipmentClientInfo.LayerType.HUMANOID;

		model.setupAnim(humanoidRenderState);

		if (stack.has(TFDataComponents.IS_USING_GOGGLES_ZOOM) && layerType != EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS) {
			renderGogglesZoom(model, humanoidRenderState, poseStack, submitNodeCollector, light, humanoidRenderState.outlineColor);
		} else {
			equipmentRenderer.renderLayers(layerType, equippable.assetId().orElseThrow(), model, humanoidRenderState, stack, poseStack, submitNodeCollector, light, humanoidRenderState.outlineColor);
		}
	}

	public static @Nullable TravellersArmorRenderer getInstance() {
		return INSTANCES.stream()
			.filter(TravellersArmorRenderer.class::isInstance)
			.map(TravellersArmorRenderer.class::cast)
			.findFirst()
			.orElse(null);
	}

	public HumanoidModel<HumanoidRenderState> getChestModel(ItemStack stack, HumanoidModel<?> contextModel) {
		ModelPart chestLayer = this.getModelPart(this.isModelSlim(contextModel) ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
		chestLayer.getAllParts().forEach(part -> part.skipDraw = true);

		boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
		boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);

		chestLayer.getChild("body").skipDraw = !hasChestplate;
		chestLayer.getChild("left_arm").skipDraw = !hasGloves;
		chestLayer.getChild("right_arm").skipDraw = !hasGloves;

		TFArmorModel model = new TFArmorModel(chestLayer);
		model.setSlot(EquipmentSlot.CHEST);

		return model;
	}

	private static ModelPart bakeLayer(ModelLayerLocation layerLocation) {
		return Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation);
	}

	private boolean isModelSlim(Model<?> model) {
		if (model instanceof PlayerModel player) return player.slim;
		return false;
	}

	private void renderGogglesZoom(HumanoidModel<HumanoidRenderState> model, HumanoidRenderState humanoidRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int outlineColor) {
		submitNodeCollector.submitModel(model, humanoidRenderState, poseStack, RenderTypes.armorCutoutNoCull(TFCommon.prefix("textures/models/armor/travellers_layer_1_down.png")), light, OverlayTexture.NO_OVERLAY, -1, null, outlineColor, null);
	}

	private record ChestVariant(boolean slim, boolean hasChestplate, boolean hasGloves) {
	}

	private record WingsVariant(boolean hasWings, boolean hasBelt) {
	}
}
