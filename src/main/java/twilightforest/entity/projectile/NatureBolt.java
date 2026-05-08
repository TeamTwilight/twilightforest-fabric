package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFDamageTypes;
import twilightforest.util.entities.EntityUtil;

public class NatureBolt extends ThrowableProjectile implements ItemSupplier {
    public NatureBolt(EntityType<? extends NatureBolt> type, Level level) {
        super(type, level);
    }

    public NatureBolt(Level level, LivingEntity owner) {
        super(TFEntities.NATURE_BOLT.get(), owner, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        this.makeTrail();
        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.027D, 0.0D));
        }
    }

    private void makeTrail() {
        for (int i = 0; i < 5; i++) {
            double dx = this.getX() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            double dy = this.getY() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            double dz = this.getZ() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, dx, dy, dz, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_LEAVES.defaultBlockState()), false, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos hitPos = result.getBlockPos();
        BlockState hitState = this.level().getBlockState(hitPos);
        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            if (!this.level().isClientSide() && hitState.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(this.level(), hitPos, hitState)) {
                bonemealable.performBonemeal((ServerLevel) this.level(), this.random, hitPos, hitState);
            } else if (hitState.isSolid() && this.canReplaceBlock(this.level(), hitPos)) {
                this.level().setBlockAndUpdate(hitPos, Blocks.BIRCH_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity owner = this.getOwner();
        Entity hit = result.getEntity();
        if (hit instanceof LivingEntity living && (owner == null || (hit != owner && hit != owner.getVehicle()))) {
            if (hit.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.LEAF_BRAIN, this, this.getOwner()), 2.0F) && this.level().getDifficulty() != Difficulty.PEACEFUL) {
                int poisonTime = this.level().getDifficulty() == Difficulty.HARD ? 7 : 3;
                living.addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0));
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    private boolean canReplaceBlock(Level level, BlockPos pos) {
        return !level.getBlockState(pos).hasBlockEntity()
                && level.getBlockState(pos).isSolidRender(level, pos)
                && level.getBlockState(pos).is(BlockTagGenerator.DRUID_PROJECTILE_REPLACEABLE)
                && EntityUtil.canDestroyBlock(level, pos, this);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.WHEAT_SEEDS);
    }
}