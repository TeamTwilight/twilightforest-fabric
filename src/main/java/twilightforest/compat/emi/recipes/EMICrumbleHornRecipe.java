package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.compat.emi.CustomTooltipEMISlotWidget;
import twilightforest.compat.emi.EMICompat;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.CrumbleRecipe;

import java.util.List;

public class EMICrumbleHornRecipe extends TFEmiRecipe<CrumbleRecipe> {
	public static final int WIDTH = 125;
	public static final int HEIGHT = 18;
	public static final EmiStack HORN = EmiStack.of(TFItems.CRUMBLE_HORN.get());
	public static final Component DROPPED_AS_ITEM = Component.translatable("gui.crumble_horn_jei.dropped_as_item").withStyle(ChatFormatting.DARK_PURPLE);

	public EMICrumbleHornRecipe(CrumbleRecipe recipe) {
		super(EMICompat.CRUMBLE_HORN, recipe, WIDTH, HEIGHT);
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
		Block input = recipe.input().getBlock();
		inputs.add(EmiStack.of(input));
	}

	@Override
	protected void addOutputs(List<EmiStack> outputs) {
		BlockState result = recipe.result();
		if (!result.isAir()) {
			outputs.add(EmiStack.of(result.getBlock()));
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		EmiIngredient input = inputs.get(0);
		widgets.addSlot(input, 0, 0);
		widgets.addTexture(EmiTexture.PLUS, 27, 3);
		widgets.addSlot(HORN, 49, 0);
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
		if (!outputs.isEmpty()) {
			EmiStack output = outputs.get(0);
			widgets.addSlot(output, 107, 0);
		} else {
			Block block = recipe.input().getBlock();
			widgets.add(new CustomTooltipEMISlotWidget(EmiStack.of(block), 107, 0))
					.addTooltipLine(1, DROPPED_AS_ITEM)
					.drawBack(false);
		}
	}
}
