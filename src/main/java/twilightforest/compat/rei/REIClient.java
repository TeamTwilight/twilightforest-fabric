package twilightforest.compat.rei;

import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import twilightforest.TFConstants;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.compat.rei.common.REIServer;
import twilightforest.inventory.UncraftingContainer;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class REIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(REIServer.UNCRAFTING_ID, EntryStacks.of(new ItemStack(TFBlocks.uncrafting_table)));
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {

    }

    @Override
    public String getPluginProviderName() {
        return TFConstants.prefix("rei_plugin").toString();
    }
}
