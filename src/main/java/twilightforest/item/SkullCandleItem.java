package twilightforest.item;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.compat.CuriosCompat;
import twilightforest.compat.TFCompat;
import twilightforest.init.TFBlocks;

import java.util.List;

public class SkullCandleItem extends StandingAndWallBlockItem {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, FabricItemSettings properties) {
		super(floor, wall, properties.equipmentSlot(SkullCandleItem::getEquipmentSlot));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (stack.hasTag()) {
			CompoundTag tag = stack.getTagElement("BlockEntityTag");
			if (tag != null) {
				if (tag.contains("CandleColor") && tag.contains("CandleAmount")) {
					tooltip.add(
							Component.translatable(tag.getInt("CandleAmount") > 1 ?
													"item.twilightforest.skull_candle.desc.multiple" :
													"item.twilightforest.skull_candle.desc",
											String.valueOf(tag.getInt("CandleAmount")),
											WordUtils.capitalize(AbstractSkullCandleBlock.CandleColors.colorFromInt(tag.getInt("CandleColor")).getSerializedName()
													.replace("\"", "").replace("_", " ")))
									.withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		if (stack.is(TFBlocks.PLAYER_SKULL_CANDLE.get().asItem()) && stack.hasTag()) {
			String s = null;
			CompoundTag compoundtag = stack.getTag();
			if (compoundtag != null && compoundtag.contains("SkullOwner", 8)) {
				s = compoundtag.getString("SkullOwner");
			} else if (compoundtag != null && compoundtag.contains("SkullOwner", 10)) {
				CompoundTag compoundtag1 = compoundtag.getCompound("SkullOwner");
				if (compoundtag1.contains("Name", 8)) {
					s = compoundtag1.getString("Name");
				}
			}

			if (s != null) {
				return Component.translatable(this.getDescriptionId() + ".named", s);
			}
		}

		return super.getName(stack);
	}

	@Override
	public void verifyTagAfterLoad(CompoundTag tag) {
		super.verifyTagAfterLoad(tag);
		if (tag.contains("SkullOwner", 8) && !StringUtils.isBlank(tag.getString("SkullOwner"))) {
			GameProfile gameprofile = new GameProfile(null, tag.getString("SkullOwner"));
			SkullCandleBlockEntity.updateGameprofile(gameprofile, (p_151177_) -> {
				tag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_151177_));
			});
		}

	}

	public static EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putInt("CandleAmount", 1);
			tag.putInt("CandleColor", 0);
			stack.addTagElement("BlockEntityTag", tag);
			items.add(stack);
		}
	}
}