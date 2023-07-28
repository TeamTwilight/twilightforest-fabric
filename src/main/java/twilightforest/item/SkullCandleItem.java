package twilightforest.item;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.client.ISTER;
import twilightforest.init.TFBlocks;

import java.util.List;

public class SkullCandleItem extends StandingAndWallBlockItem implements Equipable {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, FabricItemSettings properties) {
		super(floor, wall, properties, Direction.DOWN);
		CuriosCharmItem.setupTrinket(this);
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

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack itemstack1 = player.getItemBySlot(equipmentslot);
		if (itemstack1.isEmpty()) {
			player.setItemSlot(equipmentslot, itemstack.split(1));
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}

			return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}
}