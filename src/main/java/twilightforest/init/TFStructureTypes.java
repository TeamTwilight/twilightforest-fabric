package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.start.LegacyLandmark;

public class TFStructureTypes {
	public static final LazyRegistrar<StructureType<?>> STRUCTURE_TYPES = LazyRegistrar.create(Registry.STRUCTURE_TYPE_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<StructureType<LegacyLandmark>> LEGACY_LANDMARK = STRUCTURE_TYPES.register("legacy_landmark", () -> () -> LegacyLandmark.CODEC);
}
