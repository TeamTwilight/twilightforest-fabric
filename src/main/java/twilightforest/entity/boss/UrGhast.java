package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.ai.control.NoClipMoveControl;
import twilightforest.entity.ai.goal.GhastguardHomedFlightGoal;
import twilightforest.entity.ai.goal.UrGhastFlightGoal;
import twilightforest.entity.monster.CarminiteGhastguard;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.entity.projectile.UrGhastFireball;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//ghastguards already set home points so theres no need to here
public class UrGhast extends CarminiteGhastguard {

	private static final EntityDataAccessor<Boolean> DATA_TANTRUM = SynchedEntityData.defineId(UrGhast.class, EntityDataSerializers.BOOLEAN);

	private List<BlockPos> trapLocations;
	private int nextTantrumCry;

	private float damageUntilNextPhase = 10; // how much damage can we take before we toggle tantrum mode
	private boolean noTrapMode; // are there no traps nearby?  just float around
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public UrGhast(EntityType<? extends UrGhast> type, Level world) {
		super(type, world);
		this.wanderFactor = 32.0F;
		this.noPhysics = true;
		this.setInTantrum(false);
		this.xpReward = 317;
		this.moveControl = new NoClipMoveControl(this);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return CarminiteGhastguard.registerAttributes()
				.add(Attributes.MAX_HEALTH, 250)
				.add(Attributes.FOLLOW_RANGE, 128.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_TANTRUM, false);
	}

	public List<BlockPos> getTrapLocations() {
		return this.trapLocations;
	}

	public boolean isInNoTrapMode() {
		return this.noTrapMode;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.trapLocations = new ArrayList<>();
		this.goalSelector.availableGoals.removeIf(e -> e.getGoal() instanceof GhastguardHomedFlightGoal);
		this.goalSelector.addGoal(5, new UrGhastFlightGoal(this));
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.getRestrictCenter() != BlockPos.ZERO) {
				this.getLevel().setBlockAndUpdate(this.getRestrictCenter(), TFBlocks.UR_GHAST_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.URGHAST_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.URGHAST_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.URGHAST_DEATH.get();
	}

	@Override
	public SoundEvent getFireSound() {
		return TFSounds.URGHAST_SHOOT.get();
	}

	@Override
	public SoundEvent getWarnSound() {
		return TFSounds.URGHAST_WARN.get();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!this.getLevel().isClientSide()) {
			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		} else {
			if (this.isInTantrum()) {
				this.getLevel().addParticle(TFParticleType.BOSS_TEAR.get(),
						this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
						this.getY() + this.getRandom().nextDouble() * this.getBbHeight() * 0.5D,
						this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
						0, 0, 0
				);
			}

			// extra death explosions
			if (this.deathTime > 0) {
				for (int k = 0; k < 5; k++) {

					double d = this.getRandom().nextGaussian() * 0.02D;
					double d1 = this.getRandom().nextGaussian() * 0.02D;
					double d2 = this.getRandom().nextGaussian() * 0.02D;

					this.getLevel().addParticle(this.getRandom().nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.POOF,
							(this.getX() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
							this.getY() + this.getRandom().nextFloat() * this.getBbHeight(),
							(this.getZ() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
							d, d1, d2
					);
				}
			}
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src == DamageSource.IN_WALL || src == DamageSource.IN_FIRE || src == DamageSource.ON_FIRE || super.isInvulnerableTo(src);
	}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {
		// Don't take knockback
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		// in tantrum mode take only 1/10 damage
		if (this.isInTantrum()) {
			damage /= 10;
		}

		float oldHealth = getHealth();
		boolean attackSuccessful;

		if ("fireball".equals(source.getMsgId()) && source.getEntity() instanceof Player) {
			// 'hide' fireball attacks so that we don't take 1000 damage.
			attackSuccessful = super.hurt(DamageSource.thrown(source.getEntity(), source.getDirectEntity()), damage);
		} else {
			attackSuccessful = super.hurt(source, damage);
		}

		float lastDamage = oldHealth - this.getHealth();

		if (source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}

		if (!this.getLevel().isClientSide()) {
			if (this.hurtTime == this.hurtDuration) {
				this.damageUntilNextPhase -= lastDamage;

				//TwilightForestMod.LOGGER.debug("Urghast Attack successful, {} damage until phase switch.", this.damageUntilNextPhase);

				if (this.damageUntilNextPhase <= 0) {
					this.switchPhase();
				}
			} else {
				//TwilightForestMod.LOGGER.debug("Urghast Attack fail with {} type attack for {} damage", source.msgId, damage);
			}
		}

		return attackSuccessful;
	}

	private void switchPhase() {
		if (this.isInTantrum()) {
			this.setInTantrum(false);
		} else {
			this.startTantrum();
		}

		this.resetDamageUntilNextPhase();
	}

	public void resetDamageUntilNextPhase() {
		damageUntilNextPhase = 18;
	}

	private void startTantrum() {
		this.setInTantrum(true);

		// FIXME Use custom rain rendering instead like we do for blocking-off biomes, this is ridiculous especially for multiplayer
		// start raining
		//int rainTime = 300 * 20;

		//PrimaryLevelData worldInfo = (PrimaryLevelData) level.getServer().getLevel(Level.OVERWORLD).getLevelData(); // grab the overworld to set weather properly

		//worldInfo.setClearWeatherTime(0);
		//worldInfo.setRainTime(rainTime);
		//worldInfo.setThunderTime(rainTime);
		//worldInfo.setRaining(true);
		//worldInfo.setThundering(true);

		this.spawnGhastsAtTraps();
	}

	/**
	 * Spawn ghasts at two of the traps
	 */
	public void spawnGhastsAtTraps() {
		// spawn ghasts around two of the traps
		List<BlockPos> ghastSpawns = new ArrayList<>(this.trapLocations);
		Collections.shuffle(ghastSpawns);

		int numSpawns = Math.min(2, ghastSpawns.size());

		for (int i = 0; i < numSpawns; i++) {
			BlockPos spawnCoord = ghastSpawns.get(i);
			this.spawnMinionGhastsAt(spawnCoord.getX(), spawnCoord.getY(), spawnCoord.getZ());
		}
	}

	/**
	 * Spawn up to 6 minion ghasts around the indicated area
	 */
	private void spawnMinionGhastsAt(int x, int y, int z) {
		int tries = 24;
		int spawns = 0;
		int maxSpawns = 6;

		int rangeXZ = 4;
		int rangeY = 8;

		// lightning strike
		LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.getLevel());
		bolt.setPos(x, y + 4, z);
		bolt.setVisualOnly(true);
		this.getLevel().addFreshEntity(bolt);

		for (int i = 0; i < tries; i++) {
			CarminiteGhastling minion = TFEntities.CARMINITE_GHASTLING.get().create(this.getLevel());

			double sx = x + ((this.getRandom().nextDouble() - this.getRandom().nextDouble()) * rangeXZ);
			double sy = y + (this.getRandom().nextDouble() * rangeY);
			double sz = z + ((this.getRandom().nextDouble() - this.getRandom().nextDouble()) * rangeXZ);

			minion.moveTo(sx, sy, sz, this.getLevel().getRandom().nextFloat() * 360.0F, 0.0F);
			minion.makeBossMinion();

			if (minion.checkSpawnRules(this.getLevel(), MobSpawnType.MOB_SUMMONED)) {
				this.getLevel().addFreshEntity(minion);
				minion.spawnAnim();
			}

			if (++spawns >= maxSpawns) {
				break;
			}
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		this.hasRestriction();

		// despawn mini ghasts that are in our AABB
		for (CarminiteGhastling ghast : this.getLevel().getEntitiesOfClass(CarminiteGhastling.class, this.getBoundingBox().inflate(1.0D))) {
			ghast.spawnAnim();
			ghast.discard();
			this.heal(2);
		}

		// trap locations?
		if (this.getTrapLocations().isEmpty() && !this.isInNoTrapMode()) {
			this.scanForTrapsTwice();

			if (this.getTrapLocations().isEmpty()) {
				this.noTrapMode = true;
			}
		}

		if (this.isInTantrum()) {
			this.setTarget(null);

			// cry?
			if (--this.nextTantrumCry <= 0) {
				this.playSound(this.getHurtSound(null), this.getSoundVolume(), this.getVoicePitch());
				this.nextTantrumCry = 20 + this.getRandom().nextInt(30);
			}

			if (this.tickCount % 10 == 0) {
				this.doTantrumDamageEffects();
			}
		}
	}

	private void doTantrumDamageEffects() {
		// harm player below
		AABB below = this.getBoundingBox().move(0, -16, 0).inflate(0, 16, 0);

		for (Player player : this.getLevel().getEntitiesOfClass(Player.class, below)) {
			if (this.getLevel().canSeeSkyFromBelowWater(player.blockPosition())) {
				player.hurt(TFDamageSources.GHAST_TEAR, 3);
			}
		}

		// also suck up mini ghasts
		for (CarminiteGhastling ghast : this.getLevel().getEntitiesOfClass(CarminiteGhastling.class, below)) {
			ghast.push(0, 1, 0);
		}
	}

	/**
	 * Check if there are at least 4 ghasts near at least 2 traps.  Return false if not.
	 */
	public boolean checkGhastsAtTraps() {
		int trapsWithEnoughGhasts = 0;

		for (BlockPos trap : this.getTrapLocations()) {
			AABB aabb = new AABB(trap, trap.offset(1, 1, 1)).inflate(8D, 16D, 8D);

			List<CarminiteGhastling> nearbyGhasts = this.getLevel().getEntitiesOfClass(CarminiteGhastling.class, aabb);

			if (nearbyGhasts.size() >= 4) {
				trapsWithEnoughGhasts++;
			}
		}

		return trapsWithEnoughGhasts >= 1;
	}

	@Override
	public void spitFireball() {
		double offsetX = this.getTarget().getX() - this.getX();
		double offsetY = this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F - (this.getY() + this.getBbHeight() / 2.0F);
		double offsetZ = this.getTarget().getZ() - this.getZ();

		UrGhastFireball entityFireball = new UrGhastFireball(this.getLevel(), this, offsetX, offsetY, offsetZ, 1);
		double shotSpawnDistance = 8.5D;
		Vec3 lookVec = this.getViewVector(1.0F);
		entityFireball.setPos(
				this.getX() + lookVec.x() * shotSpawnDistance,
				this.getY() + this.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance,
				this.getZ() + lookVec.z() * shotSpawnDistance
		);
		this.getLevel().addFreshEntity(entityFireball);

		for (int i = 0; i < 2; i++) {
			entityFireball = new UrGhastFireball(this.getLevel(), this, offsetX + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 8, offsetY, offsetZ + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 8, 1);
			entityFireball.setPos(
					this.getX() + lookVec.x() * shotSpawnDistance,
					this.getY() + this.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance,
					this.getZ() + lookVec.z() * shotSpawnDistance
			);
			this.getLevel().addFreshEntity(entityFireball);
		}
	}

	/**
	 * Scan a few chunks around us for ghast trap blocks and if we find any, add them to our list
	 */
	private void scanForTrapsTwice() {
		int scanRangeXZ = 48;
		int scanRangeY = 32;

		this.scanForTraps(scanRangeXZ, scanRangeY, this.blockPosition());

		if (this.getTrapLocations().size() > 0) {
			// average the location of the traps we've found, and scan again from there
			int ax = 0, ay = 0, az = 0;

			for (BlockPos trapCoords : this.getTrapLocations()) {
				ax += trapCoords.getX();
				ay += trapCoords.getY();
				az += trapCoords.getZ();
			}

			ax /= this.getTrapLocations().size();
			ay /= this.getTrapLocations().size();
			az /= this.getTrapLocations().size();

			this.scanForTraps(scanRangeXZ, scanRangeY, new BlockPos(ax, ay, az));
		}
	}

	private void scanForTraps(int scanRangeXZ, int scanRangeY, BlockPos pos) {
		for (int sx = -scanRangeXZ; sx <= scanRangeXZ; sx++) {
			for (int sz = -scanRangeXZ; sz <= scanRangeXZ; sz++) {
				for (int sy = -scanRangeY; sy <= scanRangeY; sy++) {
					BlockPos trapCoords = pos.offset(sx, sy, sz);
					if (this.isTrapAt(trapCoords)) {
						this.getTrapLocations().add(trapCoords);
					}
				}
			}
		}
	}

	private boolean isTrapAt(BlockPos pos) {
		return this.getLevel().hasChunkAt(pos)
				&& (this.getLevel().getBlockState(pos).is(TFBlocks.GHAST_TRAP.get()));
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
	public boolean isOnFire() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public boolean isInTantrum() {
		return this.entityData.get(DATA_TANTRUM);
	}

	public void setInTantrum(boolean inTantrum) {
		this.entityData.set(DATA_TANTRUM, inTantrum);
		this.resetDamageUntilNextPhase();
	}

	@Override
	protected float getSoundVolume() {
		return 16.0F;
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.5F;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("inTantrum", this.isInTantrum());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setInTantrum(compound.getBoolean("inTantrum"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the tower as defeated
		if (!this.getLevel().isClientSide()) {
			TFGenerationSettings.markStructureConquered(this.getLevel(), this.findChestCoords(), TFLandmark.DARK_TOWER);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			TFLootTables.entityDropsIntoContainer(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), TFBlocks.DARKWOOD_CHEST.get().defaultBlockState(), this.findChestCoords());
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		// Invoked the mob's loot during die, this will avoid duplicating during the actual drop phase
		return false;
	}

	private BlockPos findChestCoords() {
		if (this.getTrapLocations().size() > 0) {
			// average the location of the traps we've found, and scan again from there
			int ax = 0, ay = 0, az = 0;

			for (BlockPos trapCoords : this.getTrapLocations()) {
				ax += trapCoords.getX();
				ay += trapCoords.getY();
				az += trapCoords.getZ();
			}

			ax /= this.getTrapLocations().size();
			ay /= this.getTrapLocations().size();
			az /= this.getTrapLocations().size();


			return new BlockPos(ax, ay + 2, az);
		} else {
			return EntityUtil.bossChestLocation(this);
		}
	}

	// Don't attack (or even think about attacking) things while we're throwing a tantrum
	@Override
	public boolean shouldAttack(LivingEntity living) {
		return !this.isInTantrum();
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}
}
