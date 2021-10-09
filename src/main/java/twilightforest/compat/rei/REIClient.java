package twilightforest.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.TFConstants;
import twilightforest.block.TFBlocks;
import twilightforest.compat.rei.common.REIServer;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class REIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(REIServer.UNCRAFTING_ID, EntryStacks.of(new ItemStack(TFBlocks.uncrafting_table)));
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        if(FabricLoader.getInstance().isModLoaded("patchouli")) {
            ItemStack book = new ItemStack(Registry.ITEM.get(new ResourceLocation("patchouli:guide_book")));
            CompoundTag bookId = new CompoundTag();
            bookId.putString("patchouli:book", "twilightforest:guide");
            book.setTag(bookId);
            registry.addEntries(EntryStacks.of(book));
        }
    }

    @Override
    public String getPluginProviderName() {
        return TFConstants.prefix("rei_plugin").toString();
    }
}
