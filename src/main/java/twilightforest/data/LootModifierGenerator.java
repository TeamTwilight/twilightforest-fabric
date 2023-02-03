package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.loot.GlobalLootModifierProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.init.TFLootModifiers;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class LootModifierGenerator extends GlobalLootModifierProvider {
	public LootModifierGenerator(PackOutput output) {
		super(output, TwilightForestMod.ID);
	}

	@Override
	protected void start() {
		add("fiery_pick_smelting", new FieryToolSmeltingModifier(new LootItemCondition[]{MatchTool.toolMatches(ItemPredicate.Builder.item().of(TFItems.FIERY_PICKAXE.get())).build()}));
		add("giant_pick_grouping", new GiantToolGroupingModifier(new LootItemCondition[]{MatchTool.toolMatches(ItemPredicate.Builder.item().of(TFItems.GIANT_PICKAXE.get())).build()}));
	}
}
