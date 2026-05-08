package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Fabric custom ingredient with the old Twilight Forest id kept for existing
 * generated recipes that use {@code "fabric:type": "twilightforest:components"}.
 */
public record ComponentsIngredient(Ingredient base, DataComponentPatch components) implements CustomIngredient {

    public static final Serializer SERIALIZER = new Serializer();

    public ComponentsIngredient {
        if (components.isEmpty()) {
            throw new IllegalArgumentException("ComponentsIngredient must have at least one component");
        }
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!base.test(stack)) return false;

        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : components.entrySet()) {
            DataComponentType<?> type = entry.getKey();
            Optional<?> value = entry.getValue();
            if (value.isPresent()) {
                if (!stack.has(type) || !Objects.equals(value.get(), stack.get(type))) {
                    return false;
                }
            } else if (stack.has(type)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        List<ItemStack> stacks = new ArrayList<>(List.of(base.getItems()));
        stacks.replaceAll(stack -> {
            ItemStack copy = stack.copy();
            copy.applyComponents(components);
            return copy;
        });
        stacks.removeIf(stack -> !base.test(stack));
        return stacks;
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static final class Serializer implements CustomIngredientSerializer<ComponentsIngredient> {

        private static final ResourceLocation ID = TwilightForestMod.prefix("components");
        private static final MapCodec<ComponentsIngredient> ALLOW_EMPTY_CODEC = createCodec(Ingredient.CODEC);
        private static final MapCodec<ComponentsIngredient> DISALLOW_EMPTY_CODEC = createCodec(Ingredient.CODEC_NONEMPTY);
        private static final StreamCodec<RegistryFriendlyByteBuf, ComponentsIngredient> PACKET_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ComponentsIngredient::base,
                DataComponentPatch.STREAM_CODEC, ComponentsIngredient::components,
                ComponentsIngredient::new);

        private static MapCodec<ComponentsIngredient> createCodec(Codec<Ingredient> ingredientCodec) {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ingredientCodec.fieldOf("base").forGetter(ComponentsIngredient::base),
                    DataComponentPatch.CODEC.fieldOf("components").forGetter(ComponentsIngredient::components)
            ).apply(instance, ComponentsIngredient::new));
        }

        @Override
        public ResourceLocation getIdentifier() {
            return ID;
        }

        @Override
        public MapCodec<ComponentsIngredient> getCodec(boolean allowEmpty) {
            return allowEmpty ? ALLOW_EMPTY_CODEC : DISALLOW_EMPTY_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ComponentsIngredient> getPacketCodec() {
            return PACKET_CODEC;
        }
    }
}
