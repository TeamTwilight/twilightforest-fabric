package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.function.Function;

public class TFSimpleArmorRenderer extends TFArmorRenderer {
	protected final Function<ModelPart, TFArmorModel> CREATE_MODEL_INSTANCE;
	protected final ModelLayerLocation INNER_ARMOR_MODEL;
	protected final ModelLayerLocation OUTER_ARMOR_MODEL;

	public TFSimpleArmorRenderer(EntityRendererProvider.Context context, Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		super(context, innerLayerLocation, outerLayerLocation);

		this.INNER_ARMOR_MODEL = innerLayerLocation;
		this.OUTER_ARMOR_MODEL = outerLayerLocation;
		this.CREATE_MODEL_INSTANCE = createModelInstance;

		INSTANCES.add(this);
	}

	@Override
	public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState state, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (equippable == null || equippable.assetId().isEmpty() || equippable.slot() != slot) {
			return;
		}

		TFArmorModel model = this.CREATE_MODEL_INSTANCE.apply(
			this.getModelPart(slot == EquipmentSlot.LEGS ? this.INNER_ARMOR_MODEL : this.OUTER_ARMOR_MODEL)
		);

		model.setSlot(slot);
		EquipmentClientInfo.LayerType layerType =
			state.isBaby && state.entityType != EntityType.ARMOR_STAND
				? EquipmentClientInfo.LayerType.HUMANOID_BABY
				: slot == EquipmentSlot.LEGS
				? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
				: EquipmentClientInfo.LayerType.HUMANOID;

		model.setupAnim(state);
		equipmentRenderer.renderLayers(
			layerType,
			equippable.assetId().orElseThrow(),
			model,
			state,
			stack,
			poseStack,
			submitNodeCollector,
			light,
			state.outlineColor
		);
	}
}