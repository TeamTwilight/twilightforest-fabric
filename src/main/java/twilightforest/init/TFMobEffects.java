package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import twilightforest.TwilightForestMod;
import twilightforest.potions.FrostedEffect;

public class TFMobEffects {

	public static final LazyRegistrar<MobEffect> MOB_EFFECTS = LazyRegistrar.create(Registries.MOB_EFFECT, TwilightForestMod.ID);

	public static final RegistryObject<MobEffect> FROSTY = MOB_EFFECTS.register("frosted", () -> new FrostedEffect(MobEffectCategory.HARMFUL, 0x56CBFD));
}
