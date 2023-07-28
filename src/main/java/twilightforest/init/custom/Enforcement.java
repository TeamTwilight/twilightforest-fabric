package twilightforest.init.custom;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import org.apache.logging.log4j.util.TriConsumer;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.util.Restriction;
import twilightforest.world.components.structures.util.StructureHints;

public record Enforcement(TriConsumer<Player, ServerLevel, Restriction> consumer) {
    public static final ResourceKey<Registry<Enforcement>> ENFORCEMENT_KEY = ResourceKey.createRegistryKey(TwilightForestMod.prefix("enforcement"));
    public static final LazyRegistrar<Enforcement> ENFORCEMENTS = LazyRegistrar.create(ENFORCEMENT_KEY, TwilightForestMod.ID);
    public static final Supplier<Registry<Enforcement>> ENFORCEMENT_REGISTRY = ENFORCEMENTS.makeRegistry();

    public static final RegistryObject<Enforcement> DARKNESS = ENFORCEMENTS.register("darkness", () -> new Enforcement((player, level, restriction) -> {
        if (player.tickCount % 60 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, (int) restriction.multiplier(), false, true));
        }
    }));

    public static final RegistryObject<Enforcement> HUNGER = ENFORCEMENTS.register("hunger", () -> new Enforcement((player, level, restriction) -> {
        if (player.tickCount % 60 == 0) {
            MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);
            int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + (int) restriction.multiplier() : (int) restriction.multiplier();
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel, false, true));
        }
    }));

    public static final RegistryObject<Enforcement> FIRE = ENFORCEMENTS.register("fire", () -> new Enforcement((player, level, restriction) -> {
        if (player.tickCount % 60 == 0) {
            player.setSecondsOnFire((int) restriction.multiplier());
        }
    }));

    public static final RegistryObject<Enforcement> FROST = ENFORCEMENTS.register("frost", () -> new Enforcement((player, level, restriction) -> {
        if (player.tickCount % 60 == 0) {
            player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 100, (int) restriction.multiplier(), false, true));
        }
    }));

    public static final RegistryObject<Enforcement> ACID_RAIN = ENFORCEMENTS.register("acid_rain", () -> new Enforcement((player, level, restriction) -> {
        if (player.tickCount % 5 == 0) {
            if (player.hurt(TFDamageTypes.getDamageSource(level, TFDamageTypes.ACID_RAIN), restriction.multiplier())) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }));

    public static void enforceBiomeProgression(Player player, ServerLevel level) {
        Restrictions.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player).ifPresent(restriction -> {
            Enforcement enforcement = ENFORCEMENT_REGISTRY.get().get(restriction.enforcement().location());
            if (enforcement != null) {
                enforcement.consumer().accept(player, level, restriction);
                if (restriction.hintStructureKey() != null) {
                    StructureHints.tryHintForStructure(player, level, restriction.hintStructureKey());
                }
            }
        });
    }
}
