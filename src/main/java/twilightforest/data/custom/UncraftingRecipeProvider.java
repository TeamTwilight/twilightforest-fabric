package twilightforest.data.custom;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.UncraftingRecipe;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public abstract class UncraftingRecipeProvider implements DataProvider {
    private final DataGenerator generator;
    private final String modId;
    private final ExistingFileHelper helper;
    protected final Map<UncraftingRecipe, String[]> builders = Maps.newLinkedHashMap();

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public UncraftingRecipeProvider(DataGenerator generator, final String modId, final ExistingFileHelper helper) {
        this.generator = generator;
        this.modId = modId;
        this.helper = helper;
    }

    public abstract void registerUncraftingRecipes();

    @Override
    public void run(CachedOutput cache) {
        this.builders.clear();
        this.registerUncraftingRecipes();
        this.builders.forEach((uncraftingRecipe, pattern) -> {
            List<UncraftingRecipe> list = builders.keySet().stream().filter(s -> {
                String name = s.getId().getPath();
                return this.missing(name) && !this.builders.containsKey(uncraftingRecipe);
            }).toList();

            if (!list.isEmpty()) {
                throw new IllegalArgumentException(String.format("Duplicate Uncrafting recipe: %s", list.stream().map(Objects::toString).collect(Collectors.joining(", "))));
            } else {
                JsonObject obj = serializeToJson(uncraftingRecipe, pattern);
                Path path = createPath(new ResourceLocation(modId, (uncraftingRecipe.getId().getPath())));
                try {
                    DataProvider.saveStable(cache, obj, path);
                } catch (IOException e) {
                    TwilightForestMod.LOGGER.error("Couldn't save Uncrafting recipe to {}", path, e);
                }
            }
        });
    }

    private boolean missing(String name) {
        return helper == null || !helper.exists(new ResourceLocation(modId, name), new ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", "crumble_horn/"));
    }

    private Path createPath(ResourceLocation name) {
        return generator.getOutputFolder().resolve("data/" + name.getNamespace() + "/recipes/uncrafting/" + name.getPath() + ".json");
    }

    private JsonObject serializeToJson(UncraftingRecipe uncraftingRecipe, String[] pattern) {
        JsonObject jsonobject = new JsonObject();

        jsonobject.addProperty("type", Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getKey(TFRecipes.UNCRAFTING_SERIALIZER.get())).toString());
        jsonobject.add("ingredient", uncraftingRecipe.ingredient().toJson());
        jsonobject.addProperty("count", uncraftingRecipe.count());
        jsonobject.addProperty("cost", uncraftingRecipe.cost());

        JsonArray jsonarray = new JsonArray();
        for (String line : pattern) jsonarray.add(line);
        jsonobject.add("pattern", jsonarray);

        Map<Character, Ingredient> keyedPattern = new HashMap<>();
        for (String s : pattern) {
            for (int x = 0; x < uncraftingRecipe.width(); x++) {
                char key = s.charAt(x);
                if (key != ' ' && !keyedPattern.containsKey(key)) keyedPattern.put(key, uncraftingRecipe.getIngredients().get(keyedPattern.size()));
            }
        }

        JsonObject keyObject = new JsonObject();

        for(Map.Entry<Character, Ingredient> entry : keyedPattern.entrySet()) {
            keyObject.add(entry.getKey().toString(), entry.getValue().toJson());
        }

        jsonobject.add("key", keyObject);

        return jsonobject;
    }

    @Override
    public String getName() {
        return modId + " Uncrafting Recipes";
    }

    /**
     * To generate the pattern, simply make an array of strings between the size of 0 and 3, and fill it with the characters of your choosing.
     * There must also be 0 to 3 characters in each string, the same amount in each. Insert an empty space (' '), to skip a slot.
     * After, provide the method with the output ingredients that will automatically be assigned to the pattern characters in order of appearance.
     */
    public void addUncraftingRecipe(String name, Ingredient input, int count, int cost, String[] pattern, Ingredient... output) {
        int width = pattern[0].length();
        int height = pattern.length;
        if (width > 3 || height > 3) {
            TwilightForestMod.LOGGER.error("The pattern must be 3x3 in size, or smaller!");
            return;
        } else if (width == 0) {
            TwilightForestMod.LOGGER.error("A pattern line can not be empty!");
            return;
        }

        for (String s : pattern) {
            if (s.length() != width) {
                TwilightForestMod.LOGGER.error("Each line of a recipe pattern must be the same length!");
                return;
            }
        }

        builders.put(new UncraftingRecipe(new ResourceLocation(name), cost, width, height, input, count, NonNullList.of(Ingredient.EMPTY, output)), pattern);
    }
}
