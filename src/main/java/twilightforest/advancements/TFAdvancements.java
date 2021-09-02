package twilightforest.advancements;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class TFAdvancements {
    public static final HasAdvancementTrigger ADVANCEMENT_UNLOCKED = CriterionRegistry.register(new HasAdvancementTrigger());
    public static final MakePortalTrigger MADE_TF_PORTAL = CriterionRegistry.register(new MakePortalTrigger());
    public static final HydraChopTrigger CONSUME_HYDRA_CHOP = CriterionRegistry.register(new HydraChopTrigger());
    public static final QuestRamCompletionTrigger QUEST_RAM_COMPLETED = CriterionRegistry.register(new QuestRamCompletionTrigger());
    public static final TrophyPedestalTrigger PLACED_TROPHY_ON_PEDESTAL = CriterionRegistry.register(new TrophyPedestalTrigger());
    public static final ActivateGhastTrapTrigger ACTIVATED_GHAST_TRAP = CriterionRegistry.register(new ActivateGhastTrapTrigger());
    public static final StructureClearedTrigger STRUCTURE_CLEARED = CriterionRegistry.register(new StructureClearedTrigger());
    public static final ArmorInventoryChangedTrigger ARMOR_CHANGED = CriterionRegistry.register(new ArmorInventoryChangedTrigger());

    public static void init() {}
}
