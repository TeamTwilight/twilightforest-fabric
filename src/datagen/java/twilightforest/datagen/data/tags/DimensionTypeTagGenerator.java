package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import twilightforest.init.TFDimension;
import twilightforest.tags.TFDimensionTypeTags;

import java.util.concurrent.CompletableFuture;

public class DimensionTypeTagGenerator extends KeyTagProvider<DimensionType> {

	public DimensionTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.DIMENSION_TYPE, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFDimensionTypeTags.ALLOWS_MAGIC_MAP_CHARTING).add(TFDimension.TWILIGHT_DIM_TYPE);
		this.tag(TFDimensionTypeTags.MOON_DIAL_INDETERMINATE)
			.add(TFDimension.TWILIGHT_DIM_TYPE)
			.add(BuiltinDimensionTypes.NETHER)
			.add(BuiltinDimensionTypes.END);
	}

	@Override
	public String getName() {
		return "Twilight Forest Dimension Type Tags";
	}
}
