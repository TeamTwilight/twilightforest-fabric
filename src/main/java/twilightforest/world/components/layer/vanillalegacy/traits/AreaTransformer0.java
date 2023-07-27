package twilightforest.world.components.layer.vanillalegacy.traits;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.Context;

public interface AreaTransformer0 {
	default <R extends Area> R run(BigContext<R> context) {
		return context.createResult((x, z) -> {
			context.initRandom(x, z);
			return this.applyPixel(context, x, z);
		});
	}

	ResourceKey<Biome> applyPixel(Context context, int x, int z);
}
