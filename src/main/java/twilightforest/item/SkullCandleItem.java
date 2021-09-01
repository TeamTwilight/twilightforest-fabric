package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.text.WordUtils;
import twilightforest.block.AbstractSkullCandleBlock;

import javax.annotation.Nullable;
import java.util.List;

public class SkullCandleItem extends StandingAndWallBlockItem {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, Properties properties) {
		super(floor, wall, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		if(stack.hasTag()) {
			CompoundTag tag = stack.getTagElement("BlockStateTag");
			if (tag != null) {
				if (tag.contains("color") && tag.contains("candles")) {
					tooltip.add(
							new TranslatableComponent(tag.getInt("candles") > 1 ?
									"item.twilightforest.skull_candle.desc.multiple" :
									"item.twilightforest.skull_candle.desc",
									String.valueOf(tag.getInt("candles")),
									WordUtils.capitalize(String.valueOf(tag.get("color")).replace("\"", "").replace("_", " ")))
									.withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return armorType == EquipmentSlot.HEAD;
	}

	@Override
	@Nullable
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putInt("candles", 1);
			tag.putString("color", AbstractSkullCandleBlock.CandleColors.PLAIN.getSerializedName());
			istack.addTagElement("BlockStateTag", tag);
			list.add(istack);
		}
	}
}
