package twilightforest.client.renderer.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.function.Function;

public class TFSimpleArmorRenderer extends TFArmorRenderer {
	protected final Function<ModelPart, TFArmorModel> CREATE_MODEL_INSTANCE;
	protected final ModelLayerLocation INNER_ARMOR_MODEL;
	protected final ModelLayerLocation OUTER_ARMOR_MODEL;

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		super(innerLayerLocation, outerLayerLocation);
		this.INNER_ARMOR_MODEL = innerLayerLocation;
		this.OUTER_ARMOR_MODEL = outerLayerLocation;
		this.CREATE_MODEL_INSTANCE = createModelInstance;
	}

	@Override
	public void render(com.mojang.blaze3d.vertex.PoseStack poseStack, SubmitNodeCollector collector, ItemStack stack, HumanoidRenderState state, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
		if (!stack.has(DataComponents.EQUIPPABLE))
			return;

		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		boolean leggings = equippable.slot() == EquipmentSlot.LEGS;
		ModelPart root = getModelPart(leggings ? INNER_ARMOR_MODEL : OUTER_ARMOR_MODEL);
		TFArmorModel armorModel = CREATE_MODEL_INSTANCE.apply(root);
		armorModel.setupAnim(state);

		Identifier texture = equippable.assetId()
			.map(id -> fromEquipmentAsset(id.identifier(), leggings))
			.orElseGet(() -> Identifier.withDefaultNamespace("textures/models/armor/leather_layer_" + (leggings ? 2 : 1) + ".png"));

		collector.submitModel(armorModel, state, poseStack, armorModel.renderType(texture), light, OverlayTexture.NO_OVERLAY, 0xffffffff, null);
	}

	private static Identifier fromEquipmentAsset(Identifier assetId, boolean leggings) {
		return Identifier.fromNamespaceAndPath(assetId.getNamespace(), "textures/entity/equipment/armor/" + assetId.getPath() + "_layer_" + (leggings ? 2 : 1) + ".png");
	}
}
