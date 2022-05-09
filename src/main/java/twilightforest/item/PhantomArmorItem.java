package twilightforest.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.util.EnchantableItem;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.renderer.TFArmorRenderer;
import twilightforest.data.tags.CustomTagGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PhantomArmorItem extends ArmorItem implements EnchantableItem {
	private static final MutableComponent TOOLTIP = new TranslatableComponent("item.twilightforest.phantom_armor.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public PhantomArmorItem(ArmorMaterial armorMaterial, EquipmentSlot armorType, Properties props) {
		super(armorMaterial, armorType, props);
		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::initializeClient);
	}

	public static String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		// there's no legs, so let's not worry about them
		return TwilightForestMod.ARMOR_DIR + "phantom_1.png";
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !Registry.ENCHANTMENT.getTag(CustomTagGenerator.EnchantmentTagGenerator.PHANTOM_ARMOR_BANNED_ENCHANTS).get().contains(Holder.direct(enchantment)) && EnchantableItem.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(book);

		for (Enchantment ench : enchants.keySet()) {
			if (Registry.ENCHANTMENT.getTag(CustomTagGenerator.EnchantmentTagGenerator.PHANTOM_ARMOR_BANNED_ENCHANTS).get().contains(Holder.direct(ench))) {
				return false;
			}
		}
		return EnchantableItem.super.isBookEnchantable(stack, book);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(TOOLTIP);
	}

	@Environment(EnvType.CLIENT)
	public void initializeClient() {
		ArmorRenderer.register(ArmorRender.INSTANCE, this);
	}

	@Environment(EnvType.CLIENT)
	private static final class ArmorRender implements ArmorRenderer {
		private static final ArmorRender INSTANCE = new ArmorRender();

		private TFArmorModel armorModel;

		@Override
		public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack itemStack, LivingEntity entityLiving, EquipmentSlot armorSlot, int light, HumanoidModel<LivingEntity> parentModel) {
			if (armorModel == null) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.PHANTOM_ARMOR_INNER : TFModelLayers.PHANTOM_ARMOR_OUTER);
				armorModel = new TFArmorModel(root);
			}
			parentModel.copyPropertiesTo(armorModel);
			TFArmorRenderer.setPartVisibility(armorModel, armorSlot);
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, itemStack, armorModel, new ResourceLocation(getArmorTexture(itemStack, entityLiving, armorSlot, null)));
		}
	}
}
