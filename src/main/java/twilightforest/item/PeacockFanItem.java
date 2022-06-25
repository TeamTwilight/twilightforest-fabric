package twilightforest.item;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFSounds;
import twilightforest.util.WorldUtil;

import javax.annotation.Nonnull;

public class PeacockFanItem extends Item {

	boolean launched = false;

	public PeacockFanItem(Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {

		if (player.getCooldowns().isOnCooldown(this))
			return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));

		if (!level.isClientSide()) {
			int fanned = this.doFan(level, player);
			player.getItemInHand(hand).hurtAndBreak(fanned + 1, player, (user) -> user.broadcastBreakEvent(hand));
		} else {
			if (player.isFallFlying()) {
				Vec3 look = player.getLookAngle();
				Vec3 movement = player.getDeltaMovement();
				//add a directional boost similar to the rocket, but slightly faster and always add a little more upwards
				player.setDeltaMovement(movement.add(
						look.x() * 0.1D + (look.x() * 2.0D - movement.x()) * 0.5D,
						(look.y() * 0.1D + (look.y() * 2.0D - movement.y()) * 0.5D) + 1.25D,
						look.z() * 0.1D + (look.z() * 2.0D - movement.z()) * 0.5D));
			}
			// jump if the player is in the air
			if (!player.isOnGround() && !player.isSwimming() && !this.launched) {
				player.setDeltaMovement(new Vec3(
						player.getDeltaMovement().x() * 1.05F,
						1.5F,
						player.getDeltaMovement().z() * 1.05F
				));
				this.launched = true;
			} else {
				AABB fanBox = getEffectAABB(player);
				Vec3 lookVec = player.getLookAngle();

				// particle effect
				for (int i = 0; i < 30; i++) {
					level.addParticle(ParticleTypes.CLOUD, fanBox.minX + level.getRandom().nextFloat() * (fanBox.maxX - fanBox.minX),
							fanBox.minY + level.random.nextFloat() * (fanBox.maxY - fanBox.minY),
							fanBox.minZ + level.random.nextFloat() * (fanBox.maxZ - fanBox.minZ),
							lookVec.x(), lookVec.y(), lookVec.z());
				}
			}
			player.playSound(TFSounds.FAN_WOOSH.get(), 1.0F + level.getRandom().nextFloat(), level.getRandom().nextFloat() * 0.7F + 0.3F);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
		}

		player.startUsingItem(hand);

		return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		if (entity instanceof Player player && player.isFallFlying() && (player.getItemInHand(InteractionHand.OFF_HAND).is(this) || isSelected)) {
			player.fallDistance = 0.0F;
		}
		if (entity instanceof Player player && this.launched) {
			player.fallDistance = 0.0F;
		}
		if (entity instanceof Player player && player.isOnGround() && launched) {
			launched = false;
		}
		super.inventoryTick(stack, level, entity, slot, isSelected);
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 20;
	}

	private int doFan(Level level, Player player) {
		AABB fanBox = this.getEffectAABB(player);
		return this.fanBlocksInAABB(level, fanBox, player) + this.fanEntitiesInAABB(level, player, fanBox);
	}

	private int fanEntitiesInAABB(Level level, Player player, AABB fanBox) {
		Vec3 moveVec = player.getLookAngle().scale(2);
		Item fan = player.getUseItem().getItem();
		int fannedEntities = 0;

		for (Entity entity : level.getEntitiesOfClass(Entity.class, fanBox)) {
			if (entity.isPushable() || entity instanceof ItemEntity || entity instanceof Projectile) {
				entity.setDeltaMovement(moveVec.x(), moveVec.y(), moveVec.z());
				fannedEntities++;
			}

			if (entity instanceof Player pushedPlayer && pushedPlayer != player && !entity.isShiftKeyDown()) {
				pushedPlayer.setDeltaMovement(moveVec.x(), moveVec.y(), moveVec.z());
				player.getCooldowns().addCooldown(fan, 40);
				fannedEntities += 2;
			}
		}
		return fannedEntities;
	}

	private AABB getEffectAABB(Player player) {
		double range = 3.0D;
		double radius = 2.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle().scale(range);
		Vec3 destVec = srcVec.add(lookVec.x(), lookVec.y(), lookVec.z());

		return new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius, destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
	}

	private int fanBlocksInAABB(Level level, AABB box, Player player) {
		int fan = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			fan += this.fanBlock(level, pos, player);
		}
		return fan;
	}

	private int fanBlock(Level level, BlockPos pos, Player player) {
		int cost = 0;

		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof FlowerBlock) {
			if (level.getRandom().nextInt(3) == 0) {
				if (PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, player, pos, state, null)) {
					level.destroyBlock(pos, true);
					cost++;
				}
			}
		}

		return cost;
	}
}