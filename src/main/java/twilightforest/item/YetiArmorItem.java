package twilightforest.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.util.ArmorTextureItem;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.YetiArmorModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class YetiArmorItem extends ArmorItem {
	private static final MutableComponent TOOLTIP = new TranslatableComponent("item.twilightforest.yeti_armor.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public YetiArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties props) {
		super(material, slot, props);
		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::initializeClient);
	}

	public static String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.CHEST) {
			return TwilightForestMod.ARMOR_DIR + "yetiarmor_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "yetiarmor_1.png";
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.slot) {
				case HEAD, CHEST, LEGS -> istack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 2);
				case FEET -> {
					istack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 2);
					istack.enchant(Enchantments.FALL_PROTECTION, 4);
				}
				default -> { }
			}
			list.add(istack);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltips, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltips, flags);
		tooltips.add(TOOLTIP);
	}

	public void initializeClient() {
		ArmorRenderer.register(ArmorRender.INSTANCE, this);
	}

	private static final class ArmorRender implements ArmorRenderer {
		private static final ArmorRender INSTANCE = new ArmorRender();
		private YetiArmorModel armorModel;

		@Override
		public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack itemStack, LivingEntity entityLiving, EquipmentSlot armorSlot, int light, HumanoidModel<LivingEntity> contextModel) {
			if(armorSlot == null) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.YETI_ARMOR_INNER : TFModelLayers.YETI_ARMOR_OUTER);
				armorModel = new YetiArmorModel(armorSlot, root);
			}
//			ArmorRenderer.renderPart(matrices, vertexConsumers, light, itemStack, armorModel, getArmorTexture(itemStack, entityLiving, armorSlot, "")); TODO: PORT
		}
	}
}
