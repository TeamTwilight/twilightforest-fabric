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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ArcticArmorItem extends ArmorItem implements DyeableLeatherItem, ArmorTextureItem {
	private static final MutableComponent TOOLTIP = new TranslatableComponent("item.twilightforest.arctic_armor.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public ArcticArmorItem(ArmorMaterial armorMaterial, EquipmentSlot armorType, Properties props) {
		super(armorMaterial, armorType, props);
		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::initializeClient);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, @Nullable String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_2" + (layer == null ? "_dyed" : "_overlay") + ".png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_1" + (layer == null ? "_dyed" : "_overlay") + ".png";
		}
	}

	@Override
	public boolean hasCustomColor(ItemStack stack) {
		CompoundTag CompoundNBT = stack.getTag();
		return (CompoundNBT != null && CompoundNBT.contains("display", 10)) && CompoundNBT.getCompound("display").contains("color", 3);
	}

	@Override
	public int getColor(ItemStack stack) {
		return this.getColor(stack, 1);
	}

	@Override
	public void clearColor(ItemStack stack) {
		this.removeColor(stack, 1);
	}

	@Override
	public void setColor(ItemStack stack, int color) {
		this.setColor(stack, color, 1);
	}

	public int getColor(ItemStack stack, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		CompoundTag stackTagCompound = stack.getTag();

		int color = 0xBDCFD9;

		if (stackTagCompound != null) {
			CompoundTag displayCompound = stackTagCompound.getCompound("display");

			if (displayCompound.contains("color" + string, 3))
				color = displayCompound.getInt("color" + string);
		}

		if (type == 0) {
			return 0xFFFFFF;
		}
		return color;
	}

	public void removeColor(ItemStack stack, int type) {
		String string = "";
		CompoundTag stackTagCompound = stack.getTag();

		if (stackTagCompound != null) {
			CompoundTag displayCompound = stackTagCompound.getCompound("display");

			if (displayCompound.contains("color" + string))
				displayCompound.remove("color" + string);

			if (displayCompound.contains("hasColor"))
				displayCompound.putBoolean("hasColor", false);
		}
	}

	public void setColor(ItemStack stack, int color, int type) {
		String string = "";
		CompoundTag stackTagCompound = stack.getTag();

		if (stackTagCompound == null) {
			stackTagCompound = new CompoundTag();
			stack.setTag(stackTagCompound);
		}

		CompoundTag displayCompound = stackTagCompound.getCompound("display");

		if (!stackTagCompound.contains("display", 10))
			stackTagCompound.put("display", displayCompound);

		displayCompound.putInt("color" + string, color);
		displayCompound.putBoolean("hasColor", true);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(TOOLTIP);
	}

	public void initializeClient() {
		ArmorRenderer.register(ArmorRender.INSTANCE, this);
	}

	private static final class ArmorRender implements ArmorRenderer {
		private static final ArmorRender INSTANCE = new ArmorRender();

		private TFArmorModel armorModel;

		@Override
		public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack itemStack, LivingEntity entityLiving, EquipmentSlot armorSlot, int light, HumanoidModel<LivingEntity> _default) {
			if(armorModel == null) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.ARCTIC_ARMOR_INNER : TFModelLayers.ARCTIC_ARMOR_OUTER);
				armorModel = new TFArmorModel(root);
			}
//			ArmorRenderer.renderPart(matrices, vertexConsumers, light, itemStack, armorModel, getArmorTexture(itemStack, entityLiving, armorSlot, null)); TODO: PORT
		}
	}
}
