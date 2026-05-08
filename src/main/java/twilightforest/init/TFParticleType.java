package twilightforest.init;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import twilightforest.client.particle.data.LeafParticleData;

/**
 * Shared aliases for registered Twilight particle ids.
 *
 * <p>Server code imports {@code TFParticleType.X}; paired clients render the
 * real {@code twilightforest:*} particle ids through their client-side particle
 * providers.</p>
 */
public final class TFParticleType {

    public static final ParticleOptions LARGE_FLAME = TFParticleTypes.LARGE_FLAME;
    public static final ParticleOptions LEAF_RUNE = TFParticleTypes.LEAF_RUNE;
    public static final ParticleOptions BOSS_TEAR = TFParticleTypes.BOSS_TEAR;
    public static final ParticleOptions GHAST_TRAP = TFParticleTypes.GHAST_TRAP;
    public static final ParticleOptions PROTECTION = TFParticleTypes.PROTECTION;
    public static final ParticleOptions SNOW = TFParticleTypes.SNOW;
    public static final ParticleOptions SNOW_WARNING = TFParticleTypes.SNOW_WARNING;
    public static final ParticleOptions EXTENDED_SNOW_WARNING = TFParticleTypes.EXTENDED_SNOW_WARNING;
    public static final ParticleOptions SNOW_GUARDIAN = TFParticleTypes.SNOW_GUARDIAN;
    public static final ParticleOptions ICE_BEAM = TFParticleTypes.ICE_BEAM;
    public static final ParticleOptions ANNIHILATE = TFParticleTypes.ANNIHILATE;
    public static final ParticleOptions PERFECT_DODGE = TFParticleTypes.PERFECT_DODGE;
    public static final ParticleOptions DOUBLE_JUMP = TFParticleTypes.DOUBLE_JUMP;
    public static final ParticleOptions HUGE_SMOKE = TFParticleTypes.HUGE_SMOKE;
    public static final ParticleOptions FIREFLY = TFParticleTypes.FIREFLY;
    public static final ParticleOptions WANDERING_FIREFLY = TFParticleTypes.WANDERING_FIREFLY;
    public static final ParticleOptions PARTICLE_SPAWNER_FIREFLY = TFParticleTypes.PARTICLE_SPAWNER_FIREFLY;
    public static final LeafParticleData FALLEN_LEAF = new LeafParticleData(255, 255, 255);
    public static final ParticleType<LeafParticleData> FALLEN_LEAF_TYPE = TFParticleTypes.FALLEN_LEAF;
    public static final ParticleOptions DIM_FLAME = TFParticleTypes.DIM_FLAME;
    public static final ParticleOptions OMINOUS_FLAME = TFParticleTypes.OMINOUS_FLAME;
    public static final ParticleOptions SORTING_PARTICLE = TFParticleTypes.SORTING_PARTICLE;
    public static final ParticleOptions TRANSFORMATION_PARTICLE = TFParticleTypes.TRANSFORMATION_PARTICLE;
    public static final ParticleOptions LOG_CORE_PARTICLE = TFParticleTypes.LOG_CORE_PARTICLE;
    public static final ParticleOptions CLOUD_PUFF = TFParticleTypes.CLOUD_PUFF;
    public static final ParticleType<ColorParticleOption> MAGIC_EFFECT = TFParticleTypes.MAGIC_EFFECT;
    public static final ParticleOptions ANGRY_LICH = TFParticleTypes.ANGRY_LICH;
    public static final ParticleOptions TWILIGHT_ORB = TFParticleTypes.TWILIGHT_ORB;
    public static final ParticleOptions SHIELD_BREAK = TFParticleTypes.SHIELD_BREAK;
    public static final ParticleOptions DRYING_RACK = TFParticleTypes.DRYING_RACK;

    private TFParticleType() {}
}
