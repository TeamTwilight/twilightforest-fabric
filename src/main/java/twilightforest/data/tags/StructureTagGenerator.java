package twilightforest.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;

public final class StructureTagGenerator {
	public static final TagKey<Structure> LANDMARK = TagKey.create(Registries.STRUCTURE, TwilightForestMod.prefix("landmark"));

	private StructureTagGenerator() {
	}
}
