package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.data.LeafParticleData;

public final class TFParticleTypes {
    public static final SimpleParticleType LARGE_FLAME = register("large_flame");
    public static final SimpleParticleType LEAF_RUNE = register("leaf_rune");
    public static final SimpleParticleType BOSS_TEAR = register("boss_tear");
    public static final SimpleParticleType GHAST_TRAP = register("ghast_trap");
    public static final SimpleParticleType PROTECTION = register("protection");
    public static final SimpleParticleType SNOW = register("snow");
    public static final SimpleParticleType SNOW_WARNING = register("snow_warning");
    public static final SimpleParticleType EXTENDED_SNOW_WARNING = register("extended_snow_warning");
    public static final SimpleParticleType SNOW_GUARDIAN = register("snow_guardian");
    public static final SimpleParticleType ICE_BEAM = register("ice_beam");
    public static final SimpleParticleType ANNIHILATE = register("annihilate");
    public static final SimpleParticleType PERFECT_DODGE = register("perfect_dodge");
    public static final SimpleParticleType DOUBLE_JUMP = register("double_jump");
    public static final SimpleParticleType HUGE_SMOKE = register("huge_smoke");
    public static final SimpleParticleType FIREFLY = register("firefly");
    public static final SimpleParticleType WANDERING_FIREFLY = register("wandering_firefly");
    public static final SimpleParticleType PARTICLE_SPAWNER_FIREFLY = register("particle_spawner_firefly");
    public static final ParticleType<LeafParticleData> FALLEN_LEAF = register("fallen_leaf", new ParticleType<LeafParticleData>(false) {
        @Override
        public MapCodec<LeafParticleData> codec() {
            return LeafParticleData.CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, LeafParticleData> streamCodec() {
            return LeafParticleData.STREAM_CODEC;
        }
    });
    public static final SimpleParticleType DIM_FLAME = register("dim_flame");
    public static final SimpleParticleType OMINOUS_FLAME = register("ominous_flame");
    public static final SimpleParticleType SORTING_PARTICLE = register("sorting_particle");
    public static final SimpleParticleType TRANSFORMATION_PARTICLE = register("transformation_particle");
    public static final SimpleParticleType LOG_CORE_PARTICLE = register("log_core_particle");
    public static final SimpleParticleType CLOUD_PUFF = register("cloud_puff");
    public static final ParticleType<ColorParticleOption> MAGIC_EFFECT = register("magic_effect", new ParticleType<ColorParticleOption>(false) {
        @Override
        public MapCodec<ColorParticleOption> codec() {
            return ColorParticleOption.codec(this);
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
            return ColorParticleOption.streamCodec(this);
        }
    });
    public static final SimpleParticleType ANGRY_LICH = register("angry_lich");
    public static final SimpleParticleType TWILIGHT_ORB = register("twilight_orb");
    public static final SimpleParticleType SHIELD_BREAK = register("shield_break");
    public static final SimpleParticleType DRYING_RACK = register("drying_rack");

    private TFParticleTypes() {
    }

    public static void bootstrap() {
    }

    private static SimpleParticleType register(String path) {
        return register(path, new SimpleParticleType(false));
    }

    private static <T extends ParticleType<?>> T register(String path, T particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                TwilightForestMod.prefix(path), particleType);
    }
}
