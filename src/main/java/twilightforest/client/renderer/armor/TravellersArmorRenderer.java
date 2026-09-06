package twilightforest.client.renderer.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;

public final class TravellersArmorRenderer extends TFArmorRenderer {
	public TravellersArmorRenderer() {
		super(TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
	}

	//TODO I dont know how to check the entity for this anymore.
	//we dont even have access to the renderstate which wouldve been a way around it, but alas
	@Nullable
	@Override
	public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier def) {
		return type != EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS && entity.getData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER) ?
			TFCommon.prefix("textures/models/armor/travellers_layer_1_down.png") :
			super.getArmorTexture(stack, type, layer, def);
	}

	@Override
	public Model<?> getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model model) {
		if (stack.has(DataComponents.EQUIPPABLE)) {
			EquipmentSlot slot = stack.get(DataComponents.EQUIPPABLE).slot();
			ModelPart root = switch (slot) {
				case HEAD -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = this.getModelPart(this.isModelSlim(model) ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
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


			if (slot == EquipmentSlot.LEGS) {
				return new TravellersWingsModel(root);
			} else if (root != null) {
				TFArmorModel armorModel = new TFArmorModel(root);
				armorModel.setSlot(slot);
				return armorModel;
			}
		}
		return super.getHumanoidArmorModel(stack, layerType, model);
	}

	@Override
	public void setupModelAnimations(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		if (model instanceof TravellersWingsModel wingsModel)
			wingsModel.setupModelAnimations(livingEntity, ageInTicks);
	}

	private boolean isModelSlim(Model<?> model) {
		if (model instanceof PlayerModel player) return player.slim;
		return false;
	}
}