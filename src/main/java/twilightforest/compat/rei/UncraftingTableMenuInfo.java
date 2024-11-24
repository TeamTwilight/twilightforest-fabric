package twilightforest.compat.rei;

import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;
import twilightforest.inventory.UncraftingMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("removal")
public record UncraftingTableMenuInfo<T extends DefaultCraftingDisplay<?>>(T display) implements me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo<UncraftingMenu, T> {

	@Override
	public Iterable<SlotAccessor> getInputSlots(me.shedaniel.rei.api.common.transfer.info.MenuInfoContext<UncraftingMenu, ?, T> context) {
		List<SlotAccessor> list;

		if(context.getDisplay() instanceof REIUncraftingDisplay){
			Container trinket = context.getMenu().tinkerInput;

			list = new ArrayList<>(trinket.getContainerSize());

			for (int i = 0; i < trinket.getContainerSize(); i++) {
				list.add(SlotAccessor.fromContainer(trinket, i));
			}
		} else {
			Container assemblyMatrix = context.getMenu().assemblyMatrix;

			list = new ArrayList<>(assemblyMatrix.getContainerSize());

			for (int i = 0; i < assemblyMatrix.getContainerSize(); i++) {
				list.add(SlotAccessor.fromContainer(assemblyMatrix, i));
			}
		}

		return list;
	}

	@Override
	public T getDisplay() {
		return this.display;
	}

	@Override
	public void markDirty(me.shedaniel.rei.api.common.transfer.info.MenuInfoContext<UncraftingMenu, ? extends ServerPlayer, T> context) {
		me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo.super.markDirty(context);

		UncraftingMenu menu = context.getMenu();

		menu.slotsChanged(menu.tinkerInput);
		menu.slotsChanged(menu.assemblyMatrix);
	}

	@Override
	public me.shedaniel.rei.api.common.transfer.info.clean.InputCleanHandler<UncraftingMenu, T> getInputCleanHandler() {
		return context -> {
			List<SlotAccessor> accessors = (List<SlotAccessor>) getInputSlots(context);

			if(context.getDisplay() instanceof REIUncraftingDisplay){
				Container assemblyMatrix = context.getMenu().assemblyMatrix;

				for (int i = 0; i < assemblyMatrix.getContainerSize(); i++) {
					accessors.add(SlotAccessor.fromContainer(assemblyMatrix, i));
				}
			}

			for (SlotAccessor gridStack : accessors) {
				me.shedaniel.rei.api.common.transfer.info.clean.InputCleanHandler.returnSlotsToPlayerInventory(context, getDumpHandler(), gridStack);
			}

			clearInputSlots(context.getMenu());
		};
	}
}