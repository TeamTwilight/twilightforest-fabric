package twilightforest.init;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import twilightforest.TwilightForestMod;

public final class TFPOITypes {
	public static final TFRegistryObject<PoiType> GHAST_TRAP = poi("ghast_trap",
		new PoiType(ImmutableSet.copyOf(TFBlocks.GHAST_TRAP.get().getStateDefinition().getPossibleStates()), 0, 1));

	private TFPOITypes() {
	}

	public static void bootstrap() {
		GHAST_TRAP.get();
	}

	private static TFRegistryObject<PoiType> poi(String path, PoiType poiType) {
		ResourceKey<PoiType> key = ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), TwilightForestMod.prefix(path));
		PoiType registered = Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, key.location(), poiType);
		return new TFRegistryObject<>(registered, key);
	}
}
