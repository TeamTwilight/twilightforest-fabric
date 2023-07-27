package twilightforest.world.components.layer.vanillalegacy.traits;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;

public interface AreaTransformer1 extends DimensionTransformer {
	default <R extends Area> R run(BigContext<R> context, R area) {
		return context.createResult((x, z) -> {
			context.initRandom(x, z);
			return this.applyPixel(context, area, x, z);
		}, area);
	}

	ResourceKey<Biome> applyPixel(BigContext<?> context, Area layer, int x, int z);
}