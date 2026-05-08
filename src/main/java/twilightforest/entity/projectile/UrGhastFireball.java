package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItemVisuals;

import java.util.List;

public class UrGhastFireball extends LargeFireball {
    private int explosionPower = 1;

    public UrGhastFireball(EntityType<? extends UrGhastFireball> type, Level level) {
        super(type, level);
    }

    public UrGhastFireball(Level level, LivingEntity owner, double deltaX, double deltaY, double deltaZ, int power) {
        super(level, owner, new Vec3(deltaX, deltaY, deltaZ), power);
        this.explosionPower = power;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) result);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        } else if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) result;
            this.onHitBlock(blockHit);
            BlockPos blockPos = blockHit.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Context.of(this, this.level().getBlockState(blockPos)));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (!this.level().isClientSide() && !(entity instanceof AbstractHurtingProjectile)) {
            entity.hurt(this.damageSources().fireball(this, this.getOwner()), 16.0F);
            boolean griefing = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            this.level().explode(null, this.getX(), this.getY(), this.getZ(), this.explosionPower, griefing, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        boolean griefing = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        this.level().explode(null, this.getX(), this.getY(), this.getZ(), this.explosionPower, griefing, Level.ExplosionInteraction.NONE);
        this.discard();
    }
}