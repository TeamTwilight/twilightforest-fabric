package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Q33 minimal ambient-jar block: paired-client block that emits a particle
 * cloud and/or plays an ambient sound on random ticks. Used by FIREFLY_JAR
 * (GLOW particles) and CICADA_JAR (rare ambient hum) so the jars feel alive
 * without needing a registered {@code BlockEntityType} + {@code BlockEntityTicker}.
 *
 * <p>{@code BlockBehaviour.Properties.randomTicks()} must be set at registration
 * time. Vanilla random tick is called ~3 times per chunk per tick on average,
 * which maps to roughly one tick per 40s per block at the default game-rule
 * randomTickSpeed=3 — light enough to leave on by default.</p>
 */
public class AmbientJarBlock extends CodexBlock {

    @Nullable private final ParticleOptions particle;
    private final int particleCount;
    private final double particleSpread;
    @Nullable private final SoundEvent ambientSound;
    private final float soundVolume;
    private final float soundPitch;

    public AmbientJarBlock(BlockBehaviour.Properties properties, BlockState templateState,
                           @Nullable ParticleOptions particle, int particleCount, double particleSpread,
                           @Nullable SoundEvent ambientSound, float soundVolume, float soundPitch) {
        super(properties, templateState);
        this.particle = particle;
        this.particleCount = particleCount;
        this.particleSpread = particleSpread;
        this.ambientSound = ambientSound;
        this.soundVolume = soundVolume;
        this.soundPitch = soundPitch;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.particle != null) {
            level.sendParticles(this.particle,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    this.particleCount,
                    this.particleSpread, this.particleSpread, this.particleSpread,
                    0.0D);
        }
        if (this.ambientSound != null && random.nextInt(4) == 0) {
            level.playSound(null,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    this.ambientSound, SoundSource.BLOCKS,
                    this.soundVolume, this.soundPitch + (random.nextFloat() - 0.5F) * 0.2F);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return this.particle != null || this.ambientSound != null;
    }
}
