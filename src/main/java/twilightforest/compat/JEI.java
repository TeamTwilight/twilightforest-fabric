package twilightforest.compat;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import twilightforest.TFConstants;
import twilightforest.block.TFBlocks;

import net.minecraft.world.item.ItemStack;

public class JEI implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(CategoryIdentifier.of("minecraft", "plugins/crafting"), EntryStacks.of(new ItemStack(TFBlocks.uncrafting_table)));
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        //registry.register(UncraftingContainer.class);
    }

//    @Override
//    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        registration.addRecipeTransferHandler(UncraftingContainer.class, VanillaRecipeCategoryUid.CRAFTING, 11, 9, 20, 36);
//    }


    @Override
    public String getPluginProviderName() {
        return TFConstants.prefix("jei_plugin").toString();
    }
}
