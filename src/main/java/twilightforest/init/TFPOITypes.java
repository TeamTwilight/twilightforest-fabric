package twilightforest.init;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import twilightforest.TFMain;

public class TFPOITypes {

	public static final PoiType GHAST_TRAP = register("ghast_trap", new PoiType(ImmutableSet.copyOf(TFBlocks.GHAST_TRAP.getStateDefinition().getPossibleStates()), 0, 1));

	private static PoiType register(String name, PoiType type) {
		return Registry.register(
			BuiltInRegistries.POINT_OF_INTEREST_TYPE,
			TFMain.prefix(name),
			type
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing poi types...");
	}
}