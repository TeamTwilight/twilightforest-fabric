package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import twilightforest.config.TFConfig;

public class UncraftingTableCondition {

	public static final UncraftingTableCondition INSTANCE = new UncraftingTableCondition();
	public static final MapCodec<UncraftingTableCondition> CODEC = MapCodec.unit(INSTANCE);

	public MapCodec<UncraftingTableCondition> codec() {
		return CODEC;
	}

	public boolean test() {
		return !TFConfig.disableEntireTable;
	}

	@Override
	public String toString() {
		return "Uncrafting Table Enabled";
	}
}
