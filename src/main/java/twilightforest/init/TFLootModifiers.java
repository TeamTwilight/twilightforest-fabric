package twilightforest.init;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class TFLootModifiers {

	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, TwilightForestMod.ID);

	public static final RegistryObject<FieryToolSmeltingModifier.Serializer> FIERY_PICK_SMELTING = LOOT_MODIFIERS.register("fiery_pick_smelting", FieryToolSmeltingModifier.Serializer::new);
	public static final RegistryObject<GiantToolGroupingModifier.Serializer> GIANT_PICK_GROUPING = LOOT_MODIFIERS.register("giant_block_grouping", GiantToolGroupingModifier.Serializer::new);
}
