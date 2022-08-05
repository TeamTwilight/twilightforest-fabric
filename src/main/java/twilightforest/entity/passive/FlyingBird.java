package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFSounds;

//flying logic moved out of TinyBird, put here instead.
//had to migrate this code because for some odd reason, ravens extend TinyBird, and fail to save the variant because they dont have them
public abstract class FlyingBird extends Bird {

	private static final EntityDataAccessor<Byte> DATA_BIRDFLAGS = SynchedEntityData.defineId(FlyingBird.class, EntityDataSerializers.BYTE);

	// [VanillaCopy] Bat field
	private BlockPos spawnPosition;
	private int currentFlightTime;

	public FlyingBird(EntityType<? extends Bird> entity, Level world) {
		super(entity, world);
		this.setIsBirdLanded(true);
		this.setAge(0);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_BIRDFLAGS, (byte) 0);
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos) {
		// prefer standing on leaves
		Material underMaterial = this.getLevel().getBlockState(pos.below()).getMaterial();
		if (underMaterial == Material.LEAVES) {
			return 200.0F;
		}
		if (underMaterial == Material.WOOD) {
			return 15.0F;
		}
		if (underMaterial == Material.GRASS) {
			return 9.0F;
		}
		// default to just preferring lighter areas
		return this.getLevel().getMaxLocalRawBrightness(pos) - 0.5F;
	}

	@Override
	public void tick() {
		super.tick();
		// while we are flying, try to level out somewhat
		if (!this.isBirdLanded()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.6D, 1.0F));
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (this.isBirdLanded()) {
			this.currentFlightTime = 0;

			boolean flag = this.isSilent();
			if (this.isSpooked() || this.isInWater() || this.getLevel().containsAnyLiquid(getBoundingBox()) || (this.getRandom().nextInt(200) == 0 && !this.isLandableBlock(this.blockPosition().below()))) {
				this.setIsBirdLanded(false);
				if (!flag) {
					this.playSound(TFSounds.TINYBIRD_TAKEOFF.get(), 0.05F, this.getVoicePitch());
				}
			}
		} else {
			this.currentFlightTime++;
			this.gameEvent(GameEvent.FLAP);

			// [VanillaCopy] Modified version of last half of Bat.customServerAiStep(). Edits noted
			if (this.spawnPosition != null && (!this.getLevel().isEmptyBlock(this.spawnPosition) || this.spawnPosition.getY() <= this.getLevel().getMinBuildHeight())) {
				this.spawnPosition = null;
			}

			//TF - no drowning birds
			if (this.isInWater() || this.getLevel().containsAnyLiquid(getBoundingBox())) {
				this.currentFlightTime = 0; // reset timer for MAX FLIGHT :v
				this.setDeltaMovement(this.getDeltaMovement().x(), 0.1F, this.getDeltaMovement().z());
			}

			if (this.spawnPosition == null || this.getRandom().nextInt(30) == 0 || this.spawnPosition.closerToCenterThan(this.position(), 2.0D)) {
				// TF - modify shift factor of Y
				int yTarget = this.currentFlightTime < 100 ? 2 : 4;
				this.spawnPosition = new BlockPos(
						this.getX() + (double) this.getRandom().nextInt(7) - (double) this.getRandom().nextInt(7),
						this.getY() + (double) this.getRandom().nextInt(6) - yTarget,
						this.getZ() + (double) this.getRandom().nextInt(7) - (double) this.getRandom().nextInt(7));
			}

			double d2 = (double) this.spawnPosition.getX() + 0.5D - this.getX();
			double d0 = (double) this.spawnPosition.getY() + 0.1D - this.getY();
			double d1 = (double) this.spawnPosition.getZ() + 0.5D - this.getZ();
			Vec3 vec3 = this.getDeltaMovement();
			Vec3 vec31 = vec3.add((Math.signum(d2) * 0.5D - vec3.x()) * (double) 0.1F, (Math.signum(d0) * (double) 0.7F - vec3.y()) * (double) 0.1F, (Math.signum(d1) * 0.5D - vec3.z()) * (double) 0.1F);
			this.setDeltaMovement(vec31);
			float f = (float) (Mth.atan2(vec31.z(), vec31.x()) * (double) (180F / (float) Math.PI)) - 90.0F;
			float f1 = Mth.wrapDegrees(f - this.getYRot());
			this.zza = 0.5F;
			this.setYRot(this.getYRot() + f1);
			// TF - change chance 100 -> 10; change check to isLandable
			if (this.getRandom().nextInt(10) == 0 && this.isLandableBlock(this.blockPosition().below())) {
				//TF - land the bird
				this.setIsBirdLanded(true);
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F));
			}
			// End copy
		}
	}

	public boolean isLandableBlock(BlockPos pos) {
		BlockState state = this.getLevel().getBlockState(pos);
		return !state.isAir()
				&& (state.is(BlockTags.LEAVES) || state.isFaceSturdy(this.getLevel(), pos, Direction.UP));
	}

	@Override
	public boolean isBirdLanded() {
		return (this.entityData.get(DATA_BIRDFLAGS) & 1) != 0;
	}

	public void setIsBirdLanded(boolean landed) {
		byte flags = this.entityData.get(DATA_BIRDFLAGS);
		this.entityData.set(DATA_BIRDFLAGS, (byte) (landed ? flags | 1 : flags & ~1));
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
	}

	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	public abstract boolean isSpooked();
}
