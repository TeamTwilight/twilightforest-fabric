package twilightforest.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class ArcticArmorItem extends DyeableArmorItem {
	@Environment(EnvType.CLIENT)
	private static final MutableComponent TOOLTIP_TEXT = new TranslatableComponent("item.twilightforest.arctic_armor.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public ArcticArmorItem(ArmorMaterial armorMaterial, EquipmentSlot armorType, Properties props) {
		super(armorMaterial, armorType, props);
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

		switch (type) {
			case 0:
				return 0xFFFFFF;
			default:
				return color;
		}
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

	//TODO do we even need this? CauldronInteraction checks for DyeableLeatherItem
	/*@Override
	public InteractionResult onItemUseFirst(ItemStack itemstack, UseOnContext context) {

		if (this.hasCustomColor(itemstack)) {
			BlockState blockAt = context.getLevel().getBlockState(context.getClickedPos());

			if (blockAt.getBlock() instanceof LayeredCauldronBlock && blockAt.getValue(LayeredCauldronBlock.LEVEL) > 0) {
				clearColor(itemstack);
				context.getPlayer().awardStat(Stats.CLEAN_ARMOR);

				LayeredCauldronBlock.lowerFillLevel(blockAt, context.getLevel(), context.getClickedPos());
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}*/

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(TOOLTIP_TEXT);
	}


	/*@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(ArmorRender.INSTANCE);
	}*/

	/*private static final class ArmorRender implements IItemRenderProperties {
		private static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		@SuppressWarnings("unchecked")
		public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A defModel) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = models.bakeLayer(armorSlot == EquipmentSlot.LEGS ? TFModelLayers.ARCTIC_ARMOR_INNER : TFModelLayers.ARCTIC_ARMOR_OUTER);
			return (A) new TFArmorModel(root);
		}
	}*/
}
