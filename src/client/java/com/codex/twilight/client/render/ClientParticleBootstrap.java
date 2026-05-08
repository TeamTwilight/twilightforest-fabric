package com.codex.twilight.client.render;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import twilightforest.client.particle.AngryLichParticle;
import twilightforest.client.particle.AnnihilateParticle;
import twilightforest.client.particle.CloudPuffParticle;
import twilightforest.client.particle.CustomTextureParticle;
import twilightforest.client.particle.DoubleJumpParticle;
import twilightforest.client.particle.DryingRackParticle;
import twilightforest.client.particle.FireflyParticle;
import twilightforest.client.particle.GhastTearParticle;
import twilightforest.client.particle.GhastTrapParticle;
import twilightforest.client.particle.IceBeamParticle;
import twilightforest.client.particle.LargeFlameParticle;
import twilightforest.client.particle.LeafParticle;
import twilightforest.client.particle.LeafRuneParticle;
import twilightforest.client.particle.LogCoreParticle;
import twilightforest.client.particle.MagicEffectParticle;
import twilightforest.client.particle.PerfectDodgeParticle;
import twilightforest.client.particle.ProtectionParticle;
import twilightforest.client.particle.SmokeScaleParticle;
import twilightforest.client.particle.SnowGuardianParticle;
import twilightforest.client.particle.SnowParticle;
import twilightforest.client.particle.SnowWarningParticle;
import twilightforest.client.particle.SortingParticle;
import twilightforest.client.particle.TransformationParticle;
import twilightforest.init.TFParticleTypes;

/**
 * Paired client-side particle providers for Twilight Forest custom particles.
 */
public final class ClientParticleBootstrap {

    private ClientParticleBootstrap() {
    }

    public static void bootstrap() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(TFParticleTypes.LARGE_FLAME, LargeFlameParticle.Factory::new);
        registry.register(TFParticleTypes.LEAF_RUNE, LeafRuneParticle.Factory::new);
        registry.register(TFParticleTypes.BOSS_TEAR, new GhastTearParticle.Factory());
        registry.register(TFParticleTypes.GHAST_TRAP, GhastTrapParticle.Factory::new);
        registry.register(TFParticleTypes.PROTECTION, ProtectionParticle.Factory::new);
        registry.register(TFParticleTypes.SNOW, SnowParticle.Factory::new);
        registry.register(TFParticleTypes.SNOW_GUARDIAN, SnowGuardianParticle.Factory::new);
        registry.register(TFParticleTypes.SNOW_WARNING, SnowWarningParticle.SimpleFactory::new);
        registry.register(TFParticleTypes.EXTENDED_SNOW_WARNING, SnowWarningParticle.ExtendedFactory::new);
        registry.register(TFParticleTypes.ICE_BEAM, IceBeamParticle.Factory::new);
        registry.register(TFParticleTypes.ANNIHILATE, AnnihilateParticle.Factory::new);
        registry.register(TFParticleTypes.PERFECT_DODGE, PerfectDodgeParticle.Provider::new);
        registry.register(TFParticleTypes.DOUBLE_JUMP, DoubleJumpParticle.Provider::new);
        registry.register(TFParticleTypes.HUGE_SMOKE, SmokeScaleParticle.Factory::new);
        registry.register(TFParticleTypes.FIREFLY, FireflyParticle.StationaryProvider::new);
        registry.register(TFParticleTypes.WANDERING_FIREFLY, FireflyParticle.WanderingProvider::new);
        registry.register(TFParticleTypes.PARTICLE_SPAWNER_FIREFLY, FireflyParticle.ParticleSpawnerProvider::new);
        registry.register(TFParticleTypes.FALLEN_LEAF, LeafParticle.Factory::new);
        registry.register(TFParticleTypes.DIM_FLAME, FlameParticle.SmallFlameProvider::new);
        registry.register(TFParticleTypes.OMINOUS_FLAME, FlameParticle.SmallFlameProvider::new);
        registry.register(TFParticleTypes.SORTING_PARTICLE, SortingParticle.Factory::new);
        registry.register(TFParticleTypes.TRANSFORMATION_PARTICLE, TransformationParticle.Factory::new);
        registry.register(TFParticleTypes.LOG_CORE_PARTICLE, LogCoreParticle.Factory::new);
        registry.register(TFParticleTypes.CLOUD_PUFF, CloudPuffParticle.Factory::new);
        registerMagicEffect(registry, TFParticleTypes.MAGIC_EFFECT);
        registry.register(TFParticleTypes.ANGRY_LICH, AngryLichParticle.Factory::new);
        registry.register(TFParticleTypes.TWILIGHT_ORB, sprite -> new CustomTextureParticle.Factory(sprite, true));
        registry.register(TFParticleTypes.SHIELD_BREAK, CustomTextureParticle.ShieldBreak::new);
        registry.register(TFParticleTypes.DRYING_RACK, DryingRackParticle.Provider::new);
    }

    private static void registerMagicEffect(ParticleFactoryRegistry registry, ParticleType<ColorParticleOption> type) {
        if (type == null) return;
        registry.register(type, MagicEffectParticle.Factory::new);
    }
}
