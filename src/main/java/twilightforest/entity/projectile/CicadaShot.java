package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import org.jetbrains.annotations.Nullable;

//railgun time
//falling blocks dont work well with bugs, so I made this
public class CicadaShot extends TFThrowable {

	public CicadaShot(EntityType<? extends CicadaShot> type, Level level) {
		super(type, level);
	}

	public CicadaShot(Level level, @Nullable LivingEntity living, double x, double y, double z) {
		super(TFEntities.CICADA_SHOT.get(), level);
		float yaw = living != null ? living.getYRot() : 0;
		float pitch = living != null ? living.getXRot() : 0;
		this.moveTo(living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), yaw, pitch);
		this.setPos(this.getX(), this.getY(), this.getZ());
		setDeltaMovement(x, y, z);
		setOwner(living);
		Vec3 motion = getDeltaMovement();
		this.shoot(motion.x(), motion.y(), motion.z(), 2 * 1.5F, 1.0F);
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getPickRadius() {
		return 1.0F;
	}

	@Override
	protected float getGravity() {
		return 0.03F;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.getLevel().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, TFBlocks.CICADA.get().defaultBlockState()), false, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BlockPos pos = result.getBlockPos().relative(result.getDirection());
        BlockState currentState = this.getLevel().getBlockState(pos);

        if (currentState.getMaterial().isReplaceable() && !currentState.is(BlockTags.FIRE) && !currentState.is(Blocks.LAVA)) {
            this.getLevel().setBlockAndUpdate(pos, TFBlocks.CICADA.get().defaultBlockState().setValue(DirectionalBlock.FACING, result.getDirection()));
			this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        } else {
            ItemEntity squish = new ItemEntity(this.getLevel(), pos.getX(), pos.getY(), pos.getZ(), Items.GRAY_DYE.getDefaultInstance());
            squish.spawnAtLocation(squish.getItem());
			this.gameEvent(GameEvent.ENTITY_DIE);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof Player player && !player.hasItemInSlot(EquipmentSlot.HEAD)) {
            player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(TFBlocks.CICADA.get()));
            if (!TFConfig.CLIENT_CONFIG.silentCicadas.get())
                player.playSound(TFSounds.CICADA.get(), 1.0F, 1.0F);
        } else {
            result.getEntity().hurt(new IndirectEntityDamageSource("cicada", this, null), 2);
        }
    }

    @Override
	protected void onHit(HitResult result) {
        super.onHit(result);
		if (!this.getLevel().isClientSide()) {
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}
}
