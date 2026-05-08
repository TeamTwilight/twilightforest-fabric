package twilightforest.entity.projectile;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.monster.Troll;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

import java.util.List;

public class ThrownBlock extends ThrowableProjectile {
    private BlockState state = Blocks.STONE.defaultBlockState();

    public ThrownBlock(EntityType<? extends ThrownBlock> type, Level level) {
        super(type, level);
    }

    public ThrownBlock(Level level, LivingEntity owner, @Nullable BlockState state) {
        super(TFEntities.THROWN_BLOCK.get(), owner, level);
        if (state != null) {
            this.state = state;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("BlockState", NbtUtils.writeBlockState(this.state));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.state = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("BlockState"));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, this.state);
            for (int i = 0; i < 20; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide() && result.getEntity() instanceof LivingEntity living && !(living instanceof Troll)) {
            living.hurt(TFDamageTypes.indirectSource(this.level(), TFDamageTypes.THROWN_BLOCK, this, this.getOwner()), 6.0F);
            this.die();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide()) {
            this.gameEvent(GameEvent.BLOCK_DESTROY, this.getOwner());
            this.die();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.die();
        }
    }

    private void die() {
        this.playSound(SoundEvents.STONE_BREAK, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    public BlockState getBlockState() {
        return this.state;
    }

    @Override
    public Component getTypeName() {
        return this.state.getBlock().getName();
    }
}