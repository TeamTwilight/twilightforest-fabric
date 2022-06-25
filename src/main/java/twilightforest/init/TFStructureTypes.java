package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.start.LegacyLandmark;

public class TFStructureTypes {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<StructureType<LegacyLandmark>> LEGACY_LANDMARK = STRUCTURE_TYPES.register("legacy_landmark", () -> () -> LegacyLandmark.CODEC);
}
