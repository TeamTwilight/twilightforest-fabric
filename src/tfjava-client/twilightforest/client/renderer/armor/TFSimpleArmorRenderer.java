package twilightforest.client.renderer.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.function.Function;

public class TFSimpleArmorRenderer extends TFArmorRenderer {
	protected final Function<ModelPart, TFArmorModel> createModelInstance;
	protected final ModelLayerLocation innerArmorModel;
	protected final ModelLayerLocation outerArmorModel;
	private final String texturePrefix;

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation, String texturePrefix) {
		this.innerArmorModel = innerLayerLocation;
		this.outerArmorModel = outerLayerLocation;
		this.createModelInstance = createModelInstance;
		this.texturePrefix = texturePrefix;
	}

	@Override
	protected HumanoidModel<LivingEntity> createModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> contextModel) {
		return this.createModelInstance.apply(getModelPart(slot == EquipmentSlot.LEGS ? this.innerArmorModel : this.outerArmorModel));
	}

	@Override
	protected String texturePrefix(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
		return this.texturePrefix;
	}
}
