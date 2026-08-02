package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.potions.FrostedEffect;

public class TFMobEffects {

	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TwilightForestMod.ID);

	public static final DeferredHolder<MobEffect, MobEffect> FROSTY = MOB_EFFECTS.register("frosted", FrostedEffect::new);
}
