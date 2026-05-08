package twilightforest.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import twilightforest.TwilightForestMod;

/**
 * P5.e — minimal port of upstream {@code twilightforest.data.tags.DamageTypeTagGenerator}.
 * Only declares the runtime-referenced tag keys; datapack JSON populates the
 * actual member damage-type lists at {@code data/twilightforest/tags/damage_type/<key>.json}.
 */
public final class DamageTypeTagGenerator {

	/** Magic / projectile damage types that strip a Lich shield charge instead of being blocked. */
	public static final TagKey<DamageType> BREAKS_LICH_SHIELDS = create("breaks_lich_shields");

	private DamageTypeTagGenerator() {
	}

	private static TagKey<DamageType> create(String path) {
		return TagKey.create(Registries.DAMAGE_TYPE, TwilightForestMod.prefix(path));
	}
}
