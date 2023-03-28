package twilightforest.init;

import com.google.common.collect.ImmutableSet;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import twilightforest.TwilightForestMod;

public class TFPOITypes {

	public static final LazyRegistrar<PoiType> POIS = LazyRegistrar.create(Registries.POINT_OF_INTEREST_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<PoiType> GHAST_TRAP = POIS.register("ghast_trap", () -> new PoiType(ImmutableSet.copyOf(TFBlocks.GHAST_TRAP.get().getStateDefinition().getPossibleStates()), 0, 1));

}
