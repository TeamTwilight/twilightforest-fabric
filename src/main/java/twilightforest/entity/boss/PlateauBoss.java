package twilightforest.entity.boss;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerBossEvent;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFBlocks;
import twilightforest.world.registration.TFGenerationSettings;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlateauBoss extends Monster implements EnforcedHomePoint {

	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public PlateauBoss(EntityType<? extends PlateauBoss> type, Level world) {
		super(type, world);
		this.xpReward = 647;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes();
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	public void aiStep() {
		if (!this.getLevel().isClientSide()) {
			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
			if (!this.hasRestriction()) {
				this.getLevel().setBlockAndUpdate(getRestrictCenter(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if(source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!this.getLevel().isClientSide()) {
			TFGenerationSettings.markStructureConquered(this.getLevel(), this.blockPosition(), TFLandmark.FINAL_CASTLE);
			for(ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}
		}
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		BlockPos home = this.getRestrictCenter();
		this.saveHomePointToNbt(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.loadHomePointFromNbt(compound, 30);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 1.0F;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public BlockPos getRestrictionCenter() {
		return this.getRestrictCenter();
	}

	@Override
	public void setRestriction(BlockPos pos, int dist) {
		this.restrictTo(pos, dist);
	}
}
