package twilightforest.entity;

import io.github.fabricators_of_create.porting_lib.block.EntityDestroyBlock;
import io.github.fabricators_of_create.porting_lib.entity.ExtraSpawnDataEntity;
import io.github.fabricators_of_create.porting_lib.entity.PartEntity;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.init.TFDamageSources;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.util.WorldUtil;

public class ChainBlock extends ThrowableProjectile implements ExtraSpawnDataEntity {

	private static final int MAX_SMASH = 12;
	private static final int MAX_CHAIN = 16;

	private static final EntityDataAccessor<Boolean> HAND = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_FOIL = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private boolean isReturning = false;
	private boolean canSmashBlocks;
	private ItemStack stack;
	private int blocksSmashed = 0;
	private double velX;
	private double velY;
	private double velZ;

	public final Chain chain1;
	public final Chain chain2;
	public final Chain chain3;
	public final Chain chain4;
	public final Chain chain5;
	private final BlockChainGoblin.MultipartGenericsAreDumb[] partsArray;

	public ChainBlock(EntityType<? extends ChainBlock> type, Level world) {
		super(type, world);

		this.chain1 = new Chain(this);
		this.chain2 = new Chain(this);
		this.chain3 = new Chain(this);
		this.chain4 = new Chain(this);
		this.chain5 = new Chain(this);
		this.partsArray = new BlockChainGoblin.MultipartGenericsAreDumb[]{this.chain1, this.chain2, this.chain3, this.chain4, this.chain5};
	}

	public ChainBlock(EntityType<? extends ChainBlock> type, Level world, LivingEntity thrower, InteractionHand hand, ItemStack stack) {
		super(type, thrower, world);
		this.isReturning = false;
		this.canSmashBlocks = EnchantmentHelper.getItemEnchantmentLevel(TFEnchantments.DESTRUCTION.get(), stack) > 0;
		this.stack = stack;
		this.setHand(hand);
		this.chain1 = new Chain(this);
		this.chain2 = new Chain(this);
		this.chain3 = new Chain(this);
		this.chain4 = new Chain(this);
		this.chain5 = new Chain(this);
		this.partsArray = new BlockChainGoblin.MultipartGenericsAreDumb[]{this.chain1, this.chain2, this.chain3, this.chain4, this.chain5};
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
		this.entityData.set(IS_FOIL, stack.hasFoil());
	}

	private void setHand(InteractionHand hand) {
		this.entityData.set(HAND, hand == InteractionHand.MAIN_HAND);
	}

	public InteractionHand getHand() {
		return this.entityData.get(HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public boolean isFoil() {
		return this.entityData.get(IS_FOIL);
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public void shoot(double x, double y, double z, float speed, float accuracy) {
		super.shoot(x, y, z, speed, accuracy);

		// save velocity
		this.velX = this.getDeltaMovement().x();
		this.velY = this.getDeltaMovement().y();
		this.velZ = this.getDeltaMovement().z();
	}

	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		// only hit living things
		if (!this.getLevel().isClientSide() && (result.getEntity() instanceof LivingEntity || result.getEntity() instanceof PartEntity<?>) && result.getEntity() != this.getOwner()) {
			if (result.getEntity().hurt(TFDamageSources.spiked(this, this.getOwner()), 10)) {
				this.playSound(TFSounds.BLOCKCHAIN_HIT.get(), 1.0f, this.random.nextFloat());
				// age when we hit a monster so that we go back to the player faster
				this.tickCount += 60;
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.getLevel().isClientSide() && !this.getLevel().isEmptyBlock(result.getBlockPos())) {

			if (!this.isReturning) {
				this.playSound(TFSounds.BLOCKCHAIN_COLLIDE.get(), 0.125f, this.random.nextFloat());
				this.gameEvent(GameEvent.HIT_GROUND);
			}

			if (this.blocksSmashed < MAX_SMASH) {
				if (this.getLevel().getBlockState(result.getBlockPos()).getDestroySpeed(this.getLevel(), result.getBlockPos()) < 0.0F ||
						this.getLevel().getBlockState(result.getBlockPos()).getDestroySpeed(this.getLevel(), result.getBlockPos()) > 0.3F) {
					// riccochet
					double bounce = 0.6;
					this.velX *= bounce;
					this.velY *= bounce;
					this.velZ *= bounce;


					switch (result.getDirection()) {
						case DOWN:
							if (this.velY > 0) {
								this.velY *= -bounce;
							}
							break;
						case UP:
							if (this.velY < 0) {
								this.velY *= -bounce;
							}
							break;
						case NORTH:
							if (this.velZ > 0) {
								this.velZ *= -bounce;
							}
							break;
						case SOUTH:
							if (this.velZ < 0) {
								this.velZ *= -bounce;
							}
							break;
						case WEST:
							if (this.velX > 0) {
								this.velX *= -bounce;
							}
							break;
						case EAST:
							if (this.velX < 0) {
								this.velX *= -bounce;
							}
							break;
					}
				}

				if (this.canSmashBlocks) {
					// demolish some blocks
					this.affectBlocksInAABB(this.getBoundingBox().inflate(0.5D));
				}
			}

			this.isReturning = true;

			// if we have smashed enough, add to ticks so that we go back faster
			if (this.blocksSmashed > MAX_SMASH && this.tickCount < 60) {
				this.tickCount += 60;
			}
		}
	}

	private void affectBlocksInAABB(AABB box) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			BlockState state = this.getLevel().getBlockState(pos);
			Block block = state.getBlock();

			if (!state.isAir() && this.stack.isCorrectToolForDrops(state)/* && block.canEntityDestroy(state, this.getLevel(), pos, this)*/) {
				if (this.getOwner() instanceof Player player) {
					if (PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, player, pos, state, null)) {
						if (!state.requiresCorrectToolForDrops() || player.getItemInHand(this.getHand()).isCorrectToolForDrops(state)) {
							block.playerDestroy(this.getLevel(), player, pos, state, this.getLevel().getBlockEntity(pos), player.getItemInHand(this.getHand()));

							this.getLevel().destroyBlock(pos, false);
							this.blocksSmashed++;
						}
					}
				}
			}
		}
	}

	public boolean canDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		if (state.getBlock() instanceof EntityDestroyBlock destroyBlock)
			return destroyBlock.canEntityDestroy(state, level, pos, entity);
		if (entity instanceof EnderDragon) {
			return !state.getBlock().defaultBlockState().is(BlockTags.DRAGON_IMMUNE);
		}
		else if ((entity instanceof WitherBoss) ||
				(entity instanceof WitherSkull))
		{
			return state.isAir() || WitherBoss.canDestroy(state);
		}

		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getLevel().isClientSide()) {
			this.chain1.tick();
			this.chain2.tick();
			this.chain3.tick();
			this.chain4.tick();
			this.chain5.tick();

			// set chain positions
			if (this.getOwner() != null) {
				// interpolate chain position
				Vec3 handVec = this.getOwner().getLookAngle().yRot(getHand() == InteractionHand.MAIN_HAND ? -0.4F : 0.4F);

				double sx = this.getOwner().getX() + handVec.x();
				double sy = this.getOwner().getY() + handVec.y() - 0.4F + this.getOwner().getEyeHeight();
				double sz = this.getOwner().getZ() + handVec.z();

				double ox = sx - this.getX();
				double oy = sy - this.getY() - 0.25F;
				double oz = sz - this.getZ();

				this.chain1.setPos(sx - ox * 0.05, sy - oy * 0.05, sz - oz * 0.05);
				this.chain2.setPos(sx - ox * 0.25, sy - oy * 0.25, sz - oz * 0.25);
				this.chain3.setPos(sx - ox * 0.45, sy - oy * 0.45, sz - oz * 0.45);
				this.chain4.setPos(sx - ox * 0.65, sy - oy * 0.65, sz - oz * 0.65);
				this.chain5.setPos(sx - ox * 0.85, sy - oy * 0.85, sz - oz * 0.85);
			}
		} else {
			if (this.getOwner() == null) {
				this.discard();
			} else {
				double distToPlayer = this.distanceTo(this.getOwner());
				// return if far enough away
				if (!this.isReturning && distToPlayer > MAX_CHAIN) {
					this.isReturning = true;
				}

				if (this.isReturning) {
					// despawn if close enough
					if (distToPlayer < 2F) {
						this.discard();
					}

					LivingEntity returnTo = (LivingEntity) this.getOwner();

					Vec3 back = new Vec3(returnTo.getX(), returnTo.getY() + returnTo.getEyeHeight(), returnTo.getZ()).subtract(this.position()).normalize();
					float age = Math.min(this.tickCount * 0.03F, 1.0F);

					// separate the return velocity from the normal bouncy velocity
					this.setDeltaMovement(new Vec3(
							this.velX * (1.0 - age) + (back.x() * 2F * age),
							this.velY * (1.0 - age) + (back.y() * 2F * age) - this.getGravity(),
							this.velZ * (1.0 - age) + (back.z() * 2F * age)
					));
				}
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(HAND, true);
		this.entityData.define(IS_FOIL, false);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower != null && thrower.getUseItem().is(TFItems.BLOCK_AND_CHAIN.get())) {
			thrower.stopUsingItem();
		}
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getOwner() != null ? this.getOwner().getId() : -1);
		buffer.writeBoolean(this.getHand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		Entity e = this.getLevel().getEntity(buf.readInt());
		if (e instanceof LivingEntity) {
			this.setOwner(e);
		}
		this.setHand(buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
