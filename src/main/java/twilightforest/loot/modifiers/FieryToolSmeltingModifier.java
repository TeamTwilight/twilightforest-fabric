package twilightforest.loot.modifiers;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class FieryToolSmeltingModifier extends LootModifier {

	protected final LootItemCondition[] conditions;

	public FieryToolSmeltingModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
		this.conditions = conditionsIn;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
		generatedLoot.forEach((stack) -> newLoot.add(
				context.getLevel().getRecipeManager()
						.getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
						.map(SmeltingRecipe::getResultItem)
						.filter(itemStack -> !itemStack.isEmpty())
						.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
						.orElse(stack)));
		return newLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<FieryToolSmeltingModifier> {

		@Override
		public FieryToolSmeltingModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
			return new FieryToolSmeltingModifier(conditionsIn);
		}

		@Override
		public JsonObject write(FieryToolSmeltingModifier instance) {
			return null;
		}
	}
}
