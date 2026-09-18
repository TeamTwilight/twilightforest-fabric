package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
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

public final class TravellersArmorRenderer extends TFArmorRenderer {
	public TravellersArmorRenderer(EntityRendererProvider.Context context) {
		super(context, TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
		INSTANCES.add(this);
	}

	@Override
	public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState state, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (equippable == null || equippable.assetId().isEmpty() || equippable.slot() != slot) {
			return;
		}

		ModelPart root = switch (slot) {
			case HEAD -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
			case CHEST -> {
				ModelPart chestLayer = this.getModelPart(this.isModelSlim(contextModel) ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
				chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
				boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
				boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
				chestLayer.getChild("body").skipDraw = !hasChestplate;
				chestLayer.getChild("left_arm").skipDraw = !hasGloves;
				chestLayer.getChild("right_arm").skipDraw = !hasGloves;
				yield chestLayer;
			}
			case LEGS -> {
				ModelPart leggingsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
				leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
				boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
				boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT) || TravellersModifiersManager.hasTravellersModifier(Minecraft.getInstance().level.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);
				TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
				TravellersWingsModel.skipWings(leggingsLayer, !hasWings);

				yield leggingsLayer;
			}
			case FEET -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
			default -> null;
		};

		if (root == null) {
			return;
		}

		HumanoidModel<HumanoidRenderState> model;
		if (slot == EquipmentSlot.LEGS) {
			model = new TravellersWingsModel(root);
		} else {
			TFArmorModel armorModel = new TFArmorModel(root);
			armorModel.setSlot(slot);
			model = armorModel;
		}

		EquipmentClientInfo.LayerType layerType = state.isBaby
			&& state.entityType != EntityType.ARMOR_STAND
			? EquipmentClientInfo.LayerType.HUMANOID_BABY
			: slot == EquipmentSlot.LEGS
			? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
			: EquipmentClientInfo.LayerType.HUMANOID;

		model.setupAnim(state);

		if (stack.has(TFDataComponents.IS_USING_GOGGLES_ZOOM) && layerType != EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS) {
			renderGogglesZoom(model, state, poseStack, submitNodeCollector, light, state.outlineColor);
		} else {
			equipmentRenderer.renderLayers(layerType, equippable.assetId().orElseThrow(), model, state, stack, poseStack, submitNodeCollector, light, state.outlineColor);
		}
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

	public static @Nullable TravellersArmorRenderer getInstance() {
		return INSTANCES.stream()
			.filter(TravellersArmorRenderer.class::isInstance)
			.map(TravellersArmorRenderer.class::cast)
			.findFirst()
			.orElse(null);
	}

	private void renderGogglesZoom(HumanoidModel<HumanoidRenderState> model, HumanoidRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int outlineColor) {
		Identifier texture = TFCommon.prefix("textures/models/armor/travellers_layer_1_down.png");
		submitNodeCollector.submitModel(
			model,
			state,
			poseStack,
			RenderTypes.armorCutoutNoCull(texture),
			light,
			OverlayTexture.NO_OVERLAY,
			-1,
			null,
			outlineColor,
			null
		);
	}

	private boolean isModelSlim(HumanoidModel<?> model) {
		return model instanceof PlayerModel player && player.slim;
	}
}