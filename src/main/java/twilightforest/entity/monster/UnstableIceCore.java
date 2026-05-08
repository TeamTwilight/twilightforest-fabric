package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class UnstableIceCore extends BaseIceMob {
    private static final float EXPLOSION_RADIUS = 1.0F;

    public UnstableIceCore(EntityType<? extends UnstableIceCore> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MAX_HEALTH, 10.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 60) {
            if (!this.level().isClientSide()) {
                boolean mobGriefing = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB);
                if (mobGriefing) {
                    this.transformBlocks();
                }
            }
            this.deathTime = 19;
            super.tickDeath();
            this.deathTime = 60;
        }
    }

    private void transformBlocks() {
        BlockPos origin = this.blockPosition();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    float range = 4.0F + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 2.0F;
                    if (distance < range) {
                        this.transformBlock(origin.offset(dx, dy, dz));
                    }
                }
            }
        }
    }

    private void transformBlock(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        Block block = state.getBlock();
        if (block.getExplosionResistance() < 8.0F && state.getDestroySpeed(this.level(), pos) >= 0.0F) {
            int color = state.getMapColor(this.level(), pos).col;
            if (this.shouldTransformGlass(state, pos)) {
                this.level().setBlockAndUpdate(pos, getStainedGlass(getClosestDyeColor(color)));
            } else if (this.shouldTransformClay(state, pos)) {
                this.level().setBlockAndUpdate(pos, getTerracotta(getClosestDyeColor(color)));
            }
        }
    }

    private boolean shouldTransformClay(BlockState state, BlockPos pos) {
        return state.isRedstoneConductor(this.level(), pos);
    }

    private boolean shouldTransformGlass(BlockState state, BlockPos pos) {
        return state.getBlock() != Blocks.AIR && Block.isShapeFullBlock(state.getShape(this.level(), pos)) && (!state.isSolid() || state.is(BlockTags.LEAVES) || state.is(Blocks.ICE));
    }

    private static DyeColor getClosestDyeColor(int color) {
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        DyeColor bestColor = DyeColor.WHITE;
        int bestDifference = 1024;
        for (DyeColor dyeColor : DyeColor.values()) {
            int textureColor = dyeColor.getTextureDiffuseColor();
            int textureRed = (textureColor >> 16) & 255;
            int textureGreen = (textureColor >> 8) & 255;
            int textureBlue = textureColor & 255;
            int difference = Math.abs(red - textureRed) + Math.abs(green - textureGreen) + Math.abs(blue - textureBlue);
            if (difference < bestDifference) {
                bestColor = dyeColor;
                bestDifference = difference;
            }
        }
        return bestColor;
    }

    private static BlockState getStainedGlass(DyeColor color) {
        return switch (color) {
            case ORANGE -> Blocks.ORANGE_STAINED_GLASS.defaultBlockState();
            case MAGENTA -> Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState();
            case YELLOW -> Blocks.YELLOW_STAINED_GLASS.defaultBlockState();
            case LIME -> Blocks.LIME_STAINED_GLASS.defaultBlockState();
            case PINK -> Blocks.PINK_STAINED_GLASS.defaultBlockState();
            case GRAY -> Blocks.GRAY_STAINED_GLASS.defaultBlockState();
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_STAINED_GLASS.defaultBlockState();
            case CYAN -> Blocks.CYAN_STAINED_GLASS.defaultBlockState();
            case PURPLE -> Blocks.PURPLE_STAINED_GLASS.defaultBlockState();
            case BLUE -> Blocks.BLUE_STAINED_GLASS.defaultBlockState();
            case BROWN -> Blocks.BROWN_STAINED_GLASS.defaultBlockState();
            case GREEN -> Blocks.GREEN_STAINED_GLASS.defaultBlockState();
            case RED -> Blocks.RED_STAINED_GLASS.defaultBlockState();
            case BLACK -> Blocks.BLACK_STAINED_GLASS.defaultBlockState();
            default -> Blocks.WHITE_STAINED_GLASS.defaultBlockState();
        };
    }

    private static BlockState getTerracotta(DyeColor color) {
        return switch (color) {
            case ORANGE -> Blocks.ORANGE_TERRACOTTA.defaultBlockState();
            case MAGENTA -> Blocks.MAGENTA_TERRACOTTA.defaultBlockState();
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState();
            case YELLOW -> Blocks.YELLOW_TERRACOTTA.defaultBlockState();
            case LIME -> Blocks.LIME_TERRACOTTA.defaultBlockState();
            case PINK -> Blocks.PINK_TERRACOTTA.defaultBlockState();
            case GRAY -> Blocks.GRAY_TERRACOTTA.defaultBlockState();
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();
            case CYAN -> Blocks.CYAN_TERRACOTTA.defaultBlockState();
            case PURPLE -> Blocks.PURPLE_TERRACOTTA.defaultBlockState();
            case BLUE -> Blocks.BLUE_TERRACOTTA.defaultBlockState();
            case BROWN -> Blocks.BROWN_TERRACOTTA.defaultBlockState();
            case GREEN -> Blocks.GREEN_TERRACOTTA.defaultBlockState();
            case RED -> Blocks.RED_TERRACOTTA.defaultBlockState();
            case BLACK -> Blocks.BLACK_TERRACOTTA.defaultBlockState();
            default -> Blocks.WHITE_TERRACOTTA.defaultBlockState();
        };
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.ICE_CORE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.ICE_CORE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.ICE_CORE_DEATH;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }
}