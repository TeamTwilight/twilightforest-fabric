package twilightforest.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.FieryArmorModel;
import twilightforest.client.renderer.TFArmorRenderer;
import twilightforest.init.TFEnchantments;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class FieryArmorItem extends ArmorItem implements CustomEnchantingBehaviorItem {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.fiery_armor.desc").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public FieryArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);
		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::initializeClient);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		AtomicBoolean badEnchant = new AtomicBoolean();
		EnchantmentHelper.getEnchantments(book).forEach((enchantment, integer) -> {
			if (Objects.equals(Enchantments.THORNS, enchantment) || Objects.equals(TFEnchantments.CHILL_AURA.get(), enchantment)
					|| Objects.equals(TFEnchantments.FIRE_REACT.get(), enchantment)
					|| Objects.equals(Enchantments.FROST_WALKER, enchantment)) {
				badEnchant.set(true);
			}
		});

		return !badEnchant.get();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !TFEnchantments.FIRE_REACT.get().equals(enchantment) &&
				!Enchantments.THORNS.equals(enchantment) &&
				!TFEnchantments.CHILL_AURA.get().equals(enchantment) &&
				!Enchantments.FROST_WALKER.equals(enchantment) &&
				CustomEnchantingBehaviorItem.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	public static String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "fiery_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "fiery_1.png";
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(TOOLTIP);
	}

	@Environment(EnvType.CLIENT)
	public void initializeClient() {
		ArmorRenderer.register(ArmorRender.INSTANCE, this);
	}

	@Environment(EnvType.CLIENT)
	private static final class ArmorRender implements ArmorRenderer {
		private static final ArmorRender INSTANCE = new ArmorRender();

		private FieryArmorModel armorModel;

		@Override
		public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack itemStack, LivingEntity entityLiving, EquipmentSlot armorSlot, int light, HumanoidModel<LivingEntity> parentModel) {
			if (armorModel == null) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.FIERY_ARMOR_INNER : TFModelLayers.FIERY_ARMOR_OUTER);
				armorModel = new FieryArmorModel(root);
			}
			parentModel.copyPropertiesTo(armorModel);
			TFArmorRenderer.setPartVisibility(armorModel, armorSlot);
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, itemStack, armorModel, new ResourceLocation(getArmorTexture(itemStack, entityLiving, armorSlot, null)));
		}
	}
}