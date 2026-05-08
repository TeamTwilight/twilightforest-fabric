package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.init.TFParticleType;

public class CloudBlock extends Block {
    @Nullable
    protected final Biome.Precipitation precipitation;

    public CloudBlock(Properties properties, @Nullable Biome.Precipitation precipitation) {
        super(properties);
        this.precipitation = precipitation;
    }

    public static boolean shouldSnow(LevelReader level, BlockPos pos) {
        if (pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
            BlockState blockstate = level.getBlockState(pos);
            return (blockstate.isAir() || blockstate.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(level, pos);
        }
        return false;
    }

    public static void addEntityMovementParticles(Level level, BlockPos pos, Entity entity, boolean jumping) {
        if (level.getRandom().nextBoolean()) {
            return;
        }
        Vec3 delta = entity.getDeltaMovement();
        BlockPos entityPos = entity.blockPosition();
        double jumpMultiplier = jumping ? 2.0D : 1.0D;
        double x = entity.getX() + (level.getRandom().nextDouble() - 0.5D) * entity.getBbWidth() * jumpMultiplier;
        double y = entity.getY() + 0.1D;
        double z = entity.getZ() + (level.getRandom().nextDouble() - 0.5D) * entity.getBbWidth() * jumpMultiplier;
        if (entityPos.getX() != pos.getX()) {
            x = Mth.clamp(x, pos.getX(), pos.getX() + 1.0D);
        }
        if (entityPos.getZ() != pos.getZ()) {
            z = Mth.clamp(z, pos.getZ(), pos.getZ() + 1.0D);
        }
        level.addParticle(TFParticleType.CLOUD_PUFF, x, y, z, delta.x * -0.5D, 0.015D * jumpMultiplier, delta.z * -0.5D);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity living && fallDistance > 0.5F) {
            this.addLandingEffects(state, serverLevel, pos, state, living, 0);
        }
        entity.causeFallDamage(fallDistance, 0.1F, level.damageSources().fall());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 1;
    }

    @Nullable
    public Biome.Precipitation getPrecipitation() {
        return this.precipitation;
    }

    public Pair<Biome.Precipitation, Float> getCurrentPrecipitation(BlockPos pos, Level level, float rainLevel) {
        if (this.getPrecipitation() == null) {
            return rainLevel > 0.0F ? Pair.of(level.getBiome(pos).value().getPrecipitationAt(pos), rainLevel) : Pair.of(Biome.Precipitation.NONE, 0.0F);
        }
        return Pair.of(this.getPrecipitation(), 1.0F);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isLoaded(pos) || TFConfig.commonCloudBlockPrecipitationDistance == 0) {
            return;
        }
        Pair<Biome.Precipitation, Float> pair = this.getCurrentPrecipitation(pos, level, level.getRainLevel(1.0F));
        if (pair.getRight() <= 0.0F || pair.getLeft() == Biome.Precipitation.NONE) {
            return;
        }
        int highestRainyBlock = pos.getY() - 1;
        for (int y = pos.getY() - 1; y > pos.getY() - TFConfig.commonCloudBlockPrecipitationDistance; y--) {
            if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(level.getBlockState(pos.atY(y)))) {
                highestRainyBlock = y - 1;
            } else {
                break;
            }
        }
        if (highestRainyBlock <= level.getMinBuildHeight()) {
            return;
        }
        if (pair.getLeft() == Biome.Precipitation.SNOW) {
            int snowHeight = level.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);
            BlockPos snowOnPos = pos.atY(highestRainyBlock + 1);
            if (snowHeight > 0 && CloudBlock.shouldSnow(level, snowOnPos)) {
                BlockState snowOnState = level.getBlockState(snowOnPos);
                if (snowOnState.is(Blocks.SNOW)) {
                    int layers = snowOnState.getValue(SnowLayerBlock.LAYERS);
                    if (layers < Math.min(snowHeight, 8)) {
                        BlockState snowLayerState = snowOnState.setValue(SnowLayerBlock.LAYERS, layers + 1);
                        Block.pushEntitiesUp(snowOnState, snowLayerState, level, snowOnPos);
                        level.setBlockAndUpdate(snowOnPos, snowLayerState);
                    }
                } else {
                    level.setBlockAndUpdate(snowOnPos, Blocks.SNOW.defaultBlockState());
                }
            }
        }
        BlockPos rainOnPos = pos.atY(highestRainyBlock);
        BlockState rainOnState = level.getBlockState(rainOnPos);
        rainOnState.getBlock().handlePrecipitation(rainOnState, level, rainOnPos, pair.getLeft());
    }

    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity living, int numberOfParticles) {
        int maxParticles = Mth.clamp((int) living.fallDistance * 2, 8, 40);
        double width = living.getBbWidth();
        double y = living.getY() + 0.1D;
        double ySpeed = 0.0005D * maxParticles;

        for (int i = 0; i < maxParticles; i++) {
            double xOffset = (living.getRandom().nextDouble() - 0.5D) * width * 2.5D;
            double zOffset = (living.getRandom().nextDouble() - 0.5D) * width * 2.5D;
            double xSpeed = xOffset * 0.0035D * maxParticles;
            double zSpeed = zOffset * 0.0035D * maxParticles;
            level.sendParticles(TFParticleType.CLOUD_PUFF, living.getX() + xOffset, y, living.getZ() + zOffset, 0, xSpeed, ySpeed, zSpeed, 1.0D);
        }
        return true;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (this.canSpawnCloudParticles(entity, level.getRandom())) {
            addEntityMovementParticles(level, pos, entity, false);
        }
    }

    public boolean canSpawnCloudParticles(Entity entity, RandomSource random) {
        if (entity.getDeltaMovement().x() == 0.0D && entity.getDeltaMovement().z() == 0.0D && random.nextInt(20) != 0) {
            return false;
        }
        return entity.tickCount % 2 == 0 && !entity.isSpectator();
    }

    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide() && state.getRenderShape() != RenderShape.INVISIBLE) {
            addEntityMovementParticles(level, pos, entity, false);
        }
        return true;
    }
}
