package twilightforest.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TFMain;
import twilightforest.advancements.*;

public class TFAdvancements {

	public static final SimpleAdvancementTrigger MADE_TF_PORTAL = register("make_tf_portal", new SimpleAdvancementTrigger());
	public static final SimpleAdvancementTrigger CONSUME_HYDRA_CHOP = register("consume_hydra_chop_on_low_hunger", new SimpleAdvancementTrigger());
	public static final SimpleAdvancementTrigger QUEST_RAM_COMPLETED = register("complete_quest_ram", new SimpleAdvancementTrigger());
	public static final SimpleAdvancementTrigger PLACED_TROPHY_ON_PEDESTAL = register("placed_on_trophy_pedestal", new SimpleAdvancementTrigger());
	public static final SimpleAdvancementTrigger ACTIVATED_GHAST_TRAP = register("activate_ghast_trap", new SimpleAdvancementTrigger());
	public static final StructureClearedTrigger STRUCTURE_CLEARED = register("structure_cleared", new StructureClearedTrigger());
	public static final DrinkFromFlaskTrigger DRINK_FROM_FLASK = register("drink_from_flask", new DrinkFromFlaskTrigger());
	public static final KillBugTrigger KILL_BUG = register("kill_bug", new KillBugTrigger());
	public static final HurtBossTrigger HURT_BOSS = register("hurt_boss", new HurtBossTrigger());
	public static final SimpleAdvancementTrigger KILL_ALL_PHANTOMS = register("kill_all_phantoms", new SimpleAdvancementTrigger());
	public static final UncraftItemTrigger UNCRAFT_ITEM = register("uncraft_item", new UncraftItemTrigger());
	public static final SimpleAdvancementTrigger BROKE_GLASS_SWORD = register("broke_glass_sword", new SimpleAdvancementTrigger());
	public static final AddModifierTrigger ADD_MODIFIER = register("add_modifier", new AddModifierTrigger());

	private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
		return Registry.register(
			BuiltInRegistries.TRIGGER_TYPES,
			TFMain.prefix(name),
			trigger
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing advancements...");
	}
}