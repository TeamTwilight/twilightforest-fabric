package twilightforest.compat.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.function.Consumer;

public class CustomTooltipEMISlotWidget extends SlotWidget {
	private Consumer<List<ClientTooltipComponent>> tooltipModifier = null;

	public CustomTooltipEMISlotWidget(EmiIngredient stack, int x, int y) {
		super(stack, x, y);
	}

	public CustomTooltipEMISlotWidget tooltip(Consumer<List<ClientTooltipComponent>> modifier) {
		this.tooltipModifier = modifier;
		return this;
	}

	public CustomTooltipEMISlotWidget addTooltipLine(int index, Component component) {
		FormattedCharSequence text = component.getVisualOrderText();
		ClientTooltipComponent added = ClientTooltipComponent.create(text);
		return tooltip(tooltip -> tooltip.add(index, added));
	}

	@Override
	public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
		List<ClientTooltipComponent> tooltip = super.getTooltip(mouseX, mouseY);
		if (tooltipModifier != null) {
			tooltipModifier.accept(tooltip);
		}
		return tooltip;
	}
}
