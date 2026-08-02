package twilightforest.init;

import com.mojang.serialization.MapCodec;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.PortingLibLoot;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class TFLootModifiers {

	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(PortingLibLoot.GLOBAL_LOOT_MODIFIER_SERIALIZERS_KEY, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FieryToolSmeltingModifier>> FIERY_PICK_SMELTING = LOOT_MODIFIERS.register("fiery_pick_smelting", () -> FieryToolSmeltingModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<GiantToolGroupingModifier>> GIANT_PICK_GROUPING = LOOT_MODIFIERS.register("giant_block_grouping", () -> GiantToolGroupingModifier.CODEC);
}
