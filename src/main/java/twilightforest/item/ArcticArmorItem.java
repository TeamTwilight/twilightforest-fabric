package twilightforest.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.item.WalkOnSnowItem;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.renderer.TFArmorRenderer;
import twilightforest.init.TFItems;

import java.util.List;

public class ArcticArmorItem extends ArmorItem implements DyeableLeatherItem, WalkOnSnowItem {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.arctic_armor.desc").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public ArcticArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
		super(armorMaterial, type, properties);
		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::initializeClient);
	}

	public static String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlot slot, @Nullable String layer) {
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
		this.removeColor(stack);
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

	public void removeColor(ItemStack stack) {
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

	@Override
	public void setColor(ItemStack stack, int color) {
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		tooltip.add(TOOLTIP);
	}

	@Override
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return stack.is(TFItems.ARCTIC_BOOTS.get());
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
			if(armorModel == null) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.ARCTIC_ARMOR_INNER : TFModelLayers.ARCTIC_ARMOR_OUTER);
				armorModel = new TFArmorModel(root);
			}
			parentModel.copyPropertiesTo(armorModel);
			TFArmorRenderer.setPartVisibility(armorModel, armorSlot);
			int color = ((DyeableLeatherItem)itemStack.getItem()).getColor(itemStack);
			TFArmorRenderer.renderArmorPart(matrices, vertexConsumers, light, itemStack, armorModel, new ResourceLocation(getArmorTexture(itemStack, entityLiving, armorSlot, null)), color);
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, itemStack, armorModel, new ResourceLocation(getArmorTexture(itemStack, entityLiving, armorSlot, "overlay")));
		}
	}
}