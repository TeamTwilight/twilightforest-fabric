package twilightforest.loot.modifiers;

import com.google.gson.JsonObject;
import io.github.fabricators_of_create.porting_lib.loot.GlobalLootModifierSerializer;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class FieryToolSmeltingModifier extends LootModifier {

	public FieryToolSmeltingModifier(LootItemCondition[] conditions) {
		super(conditions);
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
		public FieryToolSmeltingModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditions) {
			return new FieryToolSmeltingModifier(conditions);
		}

		@Override
		public JsonObject write(FieryToolSmeltingModifier instance) {
			return this.makeConditions(instance.conditions);
		}
	}
}
