package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.init.TFSounds;

public class SpikeBlock extends BlockChainGoblin.MultipartGenericsAreDumb {
    private final Entity goblin;
    private boolean collidedBlock;

    public SpikeBlock(Entity goblin) {
        super(goblin);
        this.goblin = goblin;
        this.setSize(EntityDimensions.scalable(0.75F, 0.75F));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.goblin.isAlive()) {
            this.doFall();
        }
    }

    public void doFall() {
        if (this.onGround() && !this.collidedBlock) {
            this.playSound(TFSounds.BLOCK_AND_CHAIN_COLLIDE, 0.65F, 0.75F);
            this.gameEvent(GameEvent.HIT_GROUND);
            this.collidedBlock = true;
        } else {
            this.setDeltaMovement(0.0D, this.getDeltaMovement().y() - 0.04D, 0.0D);
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
