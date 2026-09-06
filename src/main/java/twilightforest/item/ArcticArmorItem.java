package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

public class ArcticArmorItem extends Item {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.arctic_armor.desc").withStyle(ChatFormatting.GRAY);

	public ArcticArmorItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
		builder.accept(TOOLTIP);
	}

	@Override
	public boolean carminite$canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return stack.is(TFItems.ARCTIC_BOOTS);
	}
}