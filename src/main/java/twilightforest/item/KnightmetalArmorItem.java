package twilightforest.item;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class KnightmetalArmorItem extends ArmorItem {

	private static final Map<EquipmentSlot, HumanoidModel<?>> knightlyArmorModel = new EnumMap<>(EquipmentSlot.class);

	public KnightmetalArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties props) {
		super(material, slot, props);
	}

//	@Override
//	public @NotNull HumanoidModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot armorSlot, HumanoidModel<LivingEntity> defaultModel) {
//		EntityModelSet models = Minecraft.getInstance().getEntityModels();
//		ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.KNIGHTMETAL_ARMOR_INNER : TFModelLayers.KNIGHTMETAL_ARMOR_OUTER);
//		return new TFArmorModel(root);
//	}
//
//	@Override
//	public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
//		if (slot == EquipmentSlot.LEGS) {
//			return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "knightly_2.png");
//		} else {
//			return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "knightly_1.png");
//		}
//	}

	/*@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "knightly_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "knightly_1.png";
		}
	}*/

	/*@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(ArmorRender.INSTANCE);
	}

	private static final class ArmorRender implements IItemRenderProperties {
		private static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		@SuppressWarnings("unchecked")
		public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A defModel) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.KNIGHTMETAL_ARMOR_INNER : TFModelLayers.KNIGHTMETAL_ARMOR_OUTER);
			return (A) new TFArmorModel(root);
		}
	}*/
}
