package twilightforest.compat.rei.common;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.clean.InputCleanHandler;
import me.shedaniel.rei.api.common.transfer.info.simple.DumpHandler;
import me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import twilightforest.inventory.UncraftingContainer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class UncraftingTableMenuInfo<T extends UncraftingContainer, D extends Display> implements SimplePlayerInventoryMenuInfo<T, D> {
    @Override
    public Iterable<SlotAccessor> getInputSlots(MenuInfoContext<T, ?, D> context) {
        UncraftingContainer menu = context.getMenu();

        List<SlotAccessor> list = new ArrayList<>(menu.assemblyMatrix.getContainerSize());
        for (int i = 0; i < menu.assemblyMatrix.getContainerSize(); i++) {
            list.add(SlotAccessor.fromContainer(menu.assemblyMatrix, i));
        }
        return list;
    }

    static <T extends AbstractContainerMenu> void returnSlotsToPlayerInventory(MenuInfoContext<T, ?, ?> context, DumpHandler<T, ?> dumpHandler, SlotAccessor slotAccessor) {
        if (!slotAccessor.getItemStack().isEmpty()) {
            for (; slotAccessor.getItemStack().getCount() > 0; slotAccessor.takeStack(1)) {
                ItemStack stackToInsert = slotAccessor.getItemStack().copy();
                stackToInsert.setCount(1);
                if (!InputCleanHandler.dumpGenericsFtw(context, dumpHandler, stackToInsert)) {
                    InputCleanHandler.error("rei.rei.no.slot.in.inv");
                }
            }
        }
    }

    public InputCleanHandler<T, D> getInputCleanHandler() {
        return context -> {
            T container = context.getMenu();
            for (SlotAccessor gridStack : getInputSlots(context)) {
                returnSlotsToPlayerInventory(context, getDumpHandler(), gridStack);
            }

            clearInputSlots(container);
        };
    }
}
