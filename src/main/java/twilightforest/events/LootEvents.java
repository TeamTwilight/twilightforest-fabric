package twilightforest.events;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.loot.conditions.GiantPickUsedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootEvents {
	public static final LootEvents INSTANCE = new LootEvents();

	public static Map<Block, Item> GIANT_BLOCK_CONVERSIONS = new HashMap<>();

	private static final GiantPickUsedCondition GIANT_PICK_USED = new GiantPickUsedCondition(LootContext.EntityTarget.THIS);

	public static void init() {
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> INSTANCE.handleFieryToolDrops(context, drops));
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> INSTANCE.handleGiantToolGrouping(context, drops));
	}

	private void handleFieryToolDrops(LootContext context, List<ItemStack> drops) {
		if (!context.getOptionalParameter(LootContextParams.TOOL).is(TFItems.FIERY_PICKAXE)) {
			return;
		}

		List<Pair<ItemStack, Float>> list = drops.stream().map(stack ->
			context.getLevel().recipeAccess().getRecipeFor(
					RecipeType.SMELTING,
					new SingleRecipeInput(stack),
					context.getLevel()
				)
				.map(holder -> {
					ItemStack result = holder.value().assemble(new SingleRecipeInput(stack)).copy();
					result.setCount(stack.getCount() * result.getCount());
					return Pair.of(result, holder.value().experience());
				})
				.filter(pair -> !pair.getLeft().isEmpty())
				.orElse(Pair.of(stack, 0.0F))).toList();

		float xp = (float) list.stream().mapToDouble(Pair::getRight).sum();
		if (xp > 0.0F && context.hasParameter(LootContextParams.THIS_ENTITY)) {
			ExperienceOrb.award(context.getLevel(), context.getParameter(LootContextParams.THIS_ENTITY).position(), Math.round(xp));
		}

		drops.clear();
		drops.addAll(list.stream().map(Pair::getLeft).toList());
	}

	private void handleGiantToolGrouping(LootContext context, List<ItemStack> drops) {
		if (!GIANT_PICK_USED.test(context)) {
			return;
		}

		if (context.getParameter(LootContextParams.THIS_ENTITY) instanceof Player player) {
			if (!drops.isEmpty() && drops.getFirst().getItem() instanceof BlockItem block) {
				if (GIANT_BLOCK_CONVERSIONS.containsKey(block.getBlock())) {
					var attachment = player.getAttached(TFDataAttachments.GIANT_PICKAXE_MINING);
					int blockConversion = attachment.getGiantBlockConversion();
					attachment.setGiantBlockConversion(blockConversion - 1);
					drops.clear();
					if (blockConversion == 64) {
						drops.add(new ItemStack(GIANT_BLOCK_CONVERSIONS.get(block.getBlock())));
					}
				}
			}
		}
	}
}