package twilightforest.data;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.init.TFLootModifiers;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class LootModifierGenerator extends GlobalLootModifierProvider {
	public LootModifierGenerator(DataGenerator gen) {
		super(gen, TwilightForestMod.ID);
	}

	@Override
	protected void start() {
		add("fiery_pick_smelting", TFLootModifiers.FIERY_PICK_SMELTING.get(), new FieryToolSmeltingModifier(new LootItemCondition[]{MatchTool.toolMatches(ItemPredicate.Builder.item().of(TFItems.FIERY_PICKAXE.get())).build()}));
		add("giant_pick_grouping", TFLootModifiers.GIANT_PICK_GROUPING.get(), new GiantToolGroupingModifier(new LootItemCondition[]{MatchTool.toolMatches(ItemPredicate.Builder.item().of(TFItems.GIANT_PICKAXE.get())).build()}));
	}
}
