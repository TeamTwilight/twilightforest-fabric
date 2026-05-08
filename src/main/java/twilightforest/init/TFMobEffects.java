package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import twilightforest.TwilightForestMod;
import twilightforest.potions.FrostedEffect;

/**
 * Fabric port — registers TF custom MobEffects (currently {@code frosted}) into
 * vanilla {@link BuiltInRegistries#MOB_EFFECT} so {@code chill_aura} enchantment
 * applies a real per-tick effect instead of a vanilla fallback.
 */
public final class TFMobEffects {

    public static final Holder<MobEffect> FROSTY = register("frosted", new FrostedEffect());

    private TFMobEffects() {
    }

    public static void bootstrap() {
        FROSTY.value();
    }

    private static Holder<MobEffect> register(String path, MobEffect effect) {
        MobEffect registered = Registry.register(BuiltInRegistries.MOB_EFFECT, TwilightForestMod.prefix(path), effect);
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(registered);
    }
}
