package twilightforest.compat.rei;

import com.mojang.logging.LogUtils;
import dev.architectury.event.CompoundEventResult;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.AbstractEntryRenderer;
import me.shedaniel.rei.api.client.entry.renderer.EntryRendererRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.slf4j.Logger;
import twilightforest.TFConfig;
import twilightforest.client.UncraftingScreen;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.compat.rei.categories.REIUncraftingCategory;
import twilightforest.compat.rei.displays.REICrumbleHornDisplay;
import twilightforest.compat.rei.displays.REITransformationPowderDisplay;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;
import twilightforest.compat.rei.entries.EntityEntryDefinition;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.CrumbleRecipe;
import twilightforest.item.recipe.TransformPowderRecipe;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Stream;

public class TwilightForestREIClientPlugin implements REIClientPlugin {

    public static final EntityEntryDefinition ENTITY_DEFINITION = new EntityEntryDefinition();

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void registerCategories(CategoryRegistry registry) {
        // TODO update this
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE.get()));
        registry.addWorkstations(TwilightForestREIServerPlugin.UNCRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE.get()));
        registry.addWorkstations(REICrumbleHornCategory.CRUMBLE_HORN, EntryStacks.of(TFItems.CRUMBLE_HORN.get()));
        registry.addWorkstations(REITransformationPowderCategory.TRANSFORMATION, EntryStacks.of(TFItems.TRANSFORMATION_POWDER.get()));

        registry.add(new REIUncraftingCategory());
        registry.add(new REICrumbleHornCategory());
        registry.add(new REITransformationPowderCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

        registry.registerFiller(UncraftingRecipe.class, REIUncraftingDisplay::of);
        registry.registerRecipeFiller(CraftingRecipe.class, RecipeType.CRAFTING, recipe -> {
            TFConfig.Common.UncraftingStuff nestedConfig = TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS;

            if(recipe.getResultItem(registryAccess).isEmpty() ||
                    recipe.getResultItem(registryAccess).is(ItemTagGenerator.BANNED_UNCRAFTABLES) ||
                    nestedConfig.disableUncraftingRecipes.get().contains(recipe.getId().toString()) ||
                    nestedConfig.flipUncraftingModIdList.get() != nestedConfig.blacklistedUncraftingModIds.get().contains(recipe.getId().getNamespace())){
                return null;
            }

            return REIUncraftingDisplay.of(recipe);
        });

        registry.registerFiller(CrumbleRecipe.class, REICrumbleHornDisplay::of);
        registry.registerFiller(TransformPowderRecipe.class, REITransformationPowderDisplay::of);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(34, 33, 27, 20), UncraftingScreen.class, TwilightForestREIServerPlugin.UNCRAFTING);
        registry.registerClickArea(screen -> new Rectangle(115, 33, 27, 20), UncraftingScreen.class, BuiltinPlugin.CRAFTING);
    }

    public Map<EntryStack<Entity>, AbstractEntryRenderer<Entity>> RENDER_CACHE = new WeakHashMap<>();

    @Override
    public void registerEntryRenderers(EntryRendererRegistry registry) {
        RENDER_CACHE.clear();

        registry.register(EntityEntryDefinition.ENTITY_TYPE, (entry, last) -> {
            if(entry.getValue() instanceof ItemEntity itemEntity) {
                return RENDER_CACHE.computeIfAbsent(entry, entityEntryStack -> {
                    return new EntityEntryDefinition.ItemEntityRender();
                });
            }

            return last;
        });
    }

    @Override
    public void registerEntryTypes(EntryTypeRegistry registry) {
        registry.register(EntityEntryDefinition.ENTITY_TYPE, ENTITY_DEFINITION);

        registry.registerBridge(VanillaEntryTypes.ITEM, EntityEntryDefinition.ENTITY_TYPE, object -> {
            Optional<Stream<EntryStack<Entity>>> stream;

            ItemStack stack = object.getValue();

            try {
                Entity entity;

                if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
                    EntityType<Entity> type = (EntityType<Entity>) spawnEggItem.getType(stack.getTag());

                    entity = type.create(Minecraft.getInstance().level);
                } else {
                    entity = createItemEntity(stack);
                }

                stream = Optional.of(List.of(EntryStack.of(ENTITY_DEFINITION, entity)).stream());
            } catch (Exception e){
                stream = Optional.empty();
                LOGGER.error("[TwilightForestREIPlugin]: It seems that there was an attempted to create a Entity for a Itemstack but a Error occurred");
            }

            return stream.map(CompoundEventResult::interruptTrue).orElseGet(CompoundEventResult::pass);
        });

        registry.registerBridge(EntityEntryDefinition.ENTITY_TYPE, VanillaEntryTypes.ITEM, object -> {
            Optional<Stream<EntryStack<ItemStack>>> stream = Optional.empty();;

            Entity entity = object.getValue();

            ItemStack stack = null;

            if (entity instanceof ItemEntity itemEntity) {
                stack = itemEntity.getItem();
            } else {
                Item spawnEggItem = SpawnEggItem.byId(entity.getType());

                if(spawnEggItem != null) stack = spawnEggItem.getDefaultInstance();
            }

            if(stack != null) {
                stream = Optional.of(List.of(EntryStacks.of(stack)).stream());
            }

            return stream.map(CompoundEventResult::interruptTrue).orElseGet(CompoundEventResult::pass);
        });
    }

    public static ItemEntity createItemEntity(Item item){
        return createItemEntity(item.getDefaultInstance());
    }

    public static ItemEntity createItemEntity(ItemStack stack){
        ItemEntity itemEntity = EntityType.ITEM.create(Minecraft.getInstance().level);

        itemEntity.setItem(stack);

        return itemEntity;
    }
}
