package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public class MoonDialItem extends Item {
	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		boolean aprilFools = LocalDate.of(LocalDate.now().getYear(), 4, 1).equals(LocalDate.now());
		tooltip.add(Component.translatable("item.twilightforest.moon_dial.phase_" + (level != null && level.dimensionType().natural() ? level.getMoonPhase() : aprilFools ? "unknown_fools" : "unknown")).withStyle(ChatFormatting.GRAY));
	}
}