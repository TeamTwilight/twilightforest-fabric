package twilightforest.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.AddModifierTrigger;
import twilightforest.advancements.DrinkFromFlaskTrigger;
import twilightforest.advancements.HurtBossTrigger;
import twilightforest.advancements.KillBugTrigger;
import twilightforest.advancements.SimpleAdvancementTrigger;
import twilightforest.advancements.StructureClearedTrigger;
import twilightforest.advancements.UncraftItemTrigger;

import java.util.function.Supplier;

/**
 * Fabric port of upstream TFAdvancements — registers TF custom CriterionTrigger
 * subclasses into vanilla {@link BuiltInRegistries#TRIGGER_TYPES}.
 *
 * <p>Without this class, advancement JSONs that reference any
 * {@code twilightforest:*} trigger fail to load (rejected by datapack loader),
 * which is what produced the 23-advancement reject batch on the first cold-start
 * after the bulk advancement copy.</p>
 */
public final class TFAdvancements {

    public static final TFRegistryObject<SimpleAdvancementTrigger> MADE_TF_PORTAL = simple("make_tf_portal");
    public static final TFRegistryObject<SimpleAdvancementTrigger> CONSUME_HYDRA_CHOP = simple("consume_hydra_chop_on_low_hunger");
    public static final TFRegistryObject<SimpleAdvancementTrigger> QUEST_RAM_COMPLETED = simple("complete_quest_ram");
    public static final TFRegistryObject<SimpleAdvancementTrigger> PLACED_TROPHY_ON_PEDESTAL = simple("placed_on_trophy_pedestal");
    public static final TFRegistryObject<SimpleAdvancementTrigger> ACTIVATED_GHAST_TRAP = simple("activate_ghast_trap");
    public static final TFRegistryObject<SimpleAdvancementTrigger> KILL_ALL_PHANTOMS = simple("kill_all_phantoms");
    public static final TFRegistryObject<SimpleAdvancementTrigger> BROKE_GLASS_SWORD = simple("broke_glass_sword");

    public static final TFRegistryObject<StructureClearedTrigger> STRUCTURE_CLEARED = register("structure_cleared", StructureClearedTrigger::new);
    public static final TFRegistryObject<DrinkFromFlaskTrigger> DRINK_FROM_FLASK = register("drink_from_flask", DrinkFromFlaskTrigger::new);
    public static final TFRegistryObject<KillBugTrigger> KILL_BUG = register("kill_bug", KillBugTrigger::new);
    public static final TFRegistryObject<HurtBossTrigger> HURT_BOSS = register("hurt_boss", HurtBossTrigger::new);
    public static final TFRegistryObject<UncraftItemTrigger> UNCRAFT_ITEM = register("uncraft_item", UncraftItemTrigger::new);
    public static final TFRegistryObject<AddModifierTrigger> ADD_MODIFIER = register("add_modifier", AddModifierTrigger::new);

    private TFAdvancements() {
    }

    /** Force class-init so all 13 holders are registered before datapack load. */
    public static void bootstrap() {
        // Touching any field forces the entire class to initialize.
        MADE_TF_PORTAL.get();
    }

    private static TFRegistryObject<SimpleAdvancementTrigger> simple(String path) {
        return register(path, SimpleAdvancementTrigger::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T extends CriterionTrigger<?>> TFRegistryObject<T> register(String path, Supplier<T> factory) {
        T instance = factory.get();
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, TwilightForestMod.prefix(path), instance);
        return (TFRegistryObject<T>) new TFRegistryObject(instance);
    }
}
