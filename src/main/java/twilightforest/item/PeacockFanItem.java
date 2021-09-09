package twilightforest.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import twilightforest.TFSounds;
import twilightforest.util.WorldUtil;

import javax.annotation.Nonnull;

public class PeacockFanItem extends Item {

	boolean launched = false;

	PeacockFanItem(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {

		if (!world.isClientSide && !player.getCooldowns().isOnCooldown(this)) {
			if (!player.isOnGround() && !player.isSwimming() && !player.isCreative() && !player.isFallFlying()) {
				player.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 0, false, false));
				player.getItemInHand(hand).hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(hand));
			} else {
				int fanned = doFan(world, player);
				if (fanned > 0) {
					player.getItemInHand(hand).hurtAndBreak(fanned, player, (user) -> user.broadcastBreakEvent(hand));
				}
			}
		} else {
			if(player.isFallFlying()) {
				Vec3 look = player.getLookAngle();
				Vec3 movement = player.getDeltaMovement();
				//add a directional boost similar to the rocket, but slightly faster and always add a little more upwards
				player.setDeltaMovement(movement.add(
						look.x * 0.1D + (look.x * 2.0D - movement.x) * 0.5D,
						(look.y * 0.1D + (look.y * 2.0D - movement.y) * 0.5D) + 1.25D,
						look.z * 0.1D + (look.z * 2.0D - movement.z) * 0.5D));
			}
			// jump if the player is in the air
			if (!player.isOnGround() && !player.hasEffect(MobEffects.JUMP) && !player.isSwimming() && !launched) {
				player.setDeltaMovement(new Vec3(
						player.getDeltaMovement().x() * 1.05F,
						1.5F,
						player.getDeltaMovement().z() * 1.05F
				));
				launched = true;
			} else {
				AABB fanBox = getEffectAABB(player);
				Vec3 lookVec = player.getLookAngle();

				// particle effect
				for (int i = 0; i < 30; i++) {
					world.addParticle(ParticleTypes.CLOUD, fanBox.minX + world.random.nextFloat() * (fanBox.maxX - fanBox.minX),
							fanBox.minY + world.random.nextFloat() * (fanBox.maxY - fanBox.minY),
							fanBox.minZ + world.random.nextFloat() * (fanBox.maxZ - fanBox.minZ),
							lookVec.x, lookVec.y, lookVec.z);
				}
			}
			player.playSound(TFSounds.FAN_WOOSH, 1.0F + world.random.nextFloat(), world.random.nextFloat() * 0.7F + 0.3F);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
		}

		player.startUsingItem(hand);

		return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof Player player && player.isFallFlying() && (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == this || isSelected)) {
			entityIn.fallDistance = 0.0F;
		}
		if(entityIn instanceof Player player && player.hasEffect(MobEffects.JUMP)) {
			entityIn.fallDistance = 0.0F;
		}
		if (entityIn instanceof Player player && player.isOnGround() && launched) {
			player.removeEffect(MobEffects.JUMP);
			launched = false;
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
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

	private int doFan(Level world, Player player) {
		AABB fanBox = getEffectAABB(player);

		fanBlocksInAABB(world, fanBox, player);

		fanEntitiesInAABB(world, player, fanBox);

		return 1;
	}

	private void fanEntitiesInAABB(Level world, Player player, AABB fanBox) {
		Vec3 moveVec = player.getLookAngle().scale(2);
		Item fan = player.getUseItem().getItem();

		for (Entity entity : world.getEntitiesOfClass(Entity.class, fanBox)) {
			if (entity.isPushable() || entity instanceof ItemEntity || entity instanceof Projectile) {
				entity.setDeltaMovement(moveVec.x, moveVec.y, moveVec.z);
			}

			if(entity instanceof Player && !entity.isShiftKeyDown()) {
				entity.setDeltaMovement(moveVec.x, moveVec.y, moveVec.z);
				player.getCooldowns().addCooldown(fan, 40);
			}
		}
	}

	private AABB getEffectAABB(Player player) {
		double range = 3.0D;
		double radius = 2.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle().scale(range);
		Vec3 destVec = srcVec.add(lookVec.x, lookVec.y, lookVec.z);

		return new AABB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}

	private int fanBlocksInAABB(Level world, AABB box, Player player) {
		int fan = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			fan += fanBlock(world, pos, player);
		}
		return fan;
	}

	private int fanBlock(Level world, BlockPos pos, Player player) {
		int cost = 0;

		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof FlowerBlock) {
			if (world.random.nextInt(3) == 0) {
				if (PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, pos, state, null)) {
					world.destroyBlock(pos, true);
				}
			}
		}

		return cost;
	}
}
