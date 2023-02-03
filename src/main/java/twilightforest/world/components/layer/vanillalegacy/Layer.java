package twilightforest.world.components.layer.vanillalegacy;

import io.github.fabricators_of_create.porting_lib.util.ServerLifecycleHooks;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import twilightforest.world.components.layer.vanillalegacy.area.AreaFactory;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;

import java.util.Optional;

public class Layer {
	public final LazyArea area;

	public Layer(AreaFactory<LazyArea> p_76714_) {
		this.area = p_76714_.make();
	}

	public Holder<Biome> get(HolderGetter<Biome> registry, int p_76717_, int p_76718_) {
		int i = this.area.get(p_76717_, p_76718_);
		Optional<Holder.Reference<Biome>> biome = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registries.BIOME).getHolder(i);
		if (biome.isEmpty()) {
			Util.logAndPauseIfInIde("Unknown biome id: " + i);
			return registry.getOrThrow(Biomes.PLAINS);
		} else {
			return biome.get();
		}

	}
}
