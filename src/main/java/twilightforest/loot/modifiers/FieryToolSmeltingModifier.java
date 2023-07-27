package twilightforest.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

public class FieryToolSmeltingModifier extends LootModifier {
	public static final Codec<FieryToolSmeltingModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, FieryToolSmeltingModifier::new));

	public FieryToolSmeltingModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
		generatedLoot.forEach((stack) -> newLoot.add(
				context.getLevel().getRecipeManager()
						.getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
						.map(recipe -> recipe.getResultItem(context.getLevel().registryAccess()))
						.filter(itemStack -> !itemStack.isEmpty())
						.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
						.orElse(stack)));
		return newLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return FieryToolSmeltingModifier.CODEC;
	}
}
