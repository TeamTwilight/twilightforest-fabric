package twilightforest.loot.modifiers;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import twilightforest.init.TFItems;

public final class FieryToolSmeltingModifier {
	private FieryToolSmeltingModifier() {
	}

	public static ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootParams context) {
		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
		if (tool == null || !tool.is(TFItems.FIERY_PICKAXE.get()) || generatedLoot.isEmpty()) {
			return generatedLoot;
		}

		ObjectArrayList<ItemStack> result = new ObjectArrayList<>(generatedLoot.size());
		float xp = 0.0F;
		for (ItemStack stack : generatedLoot) {
			var recipe = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), context.getLevel());
			if (recipe.isPresent()) {
				ItemStack smelted = recipe.get().value().getResultItem(context.getLevel().registryAccess()).copy();
				if (!smelted.isEmpty()) {
					smelted.setCount(stack.getCount() * smelted.getCount());
					result.add(smelted);
					xp += recipe.get().value().getExperience();
					continue;
				}
			}
			result.add(stack);
		}

		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (xp > 0.0F && entity != null) {
			ExperienceOrb.award(context.getLevel(), entity.position(), Math.round(xp));
		}
		return result;
	}
}
