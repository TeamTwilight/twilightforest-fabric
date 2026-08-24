package twilightforest.init;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import twilightforest.TFMain;

public class TFPOITypes {
	public static final ResourceKey<PoiType> GHAST_TRAP_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, TFMain.prefix("ghast_trap"));
	public static final PoiType GHAST_TRAP = PoiTypes.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, GHAST_TRAP_KEY, ImmutableSet.copyOf(TFBlocks.GHAST_TRAP.getStateDefinition().getPossibleStates()), 0, 1);

	public static void init() {
		TFMain.LOGGER.info("Initializing poi types...");
	}
}