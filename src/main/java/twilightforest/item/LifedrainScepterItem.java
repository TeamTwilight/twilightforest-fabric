package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.ReequipAnimationItem;
import io.github.fabricators_of_create.porting_lib.util.EnchantableItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFSounds;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.util.EntityUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class LifedrainScepterItem extends Item implements EnchantableItem, ReequipAnimationItem {

	protected LifedrainScepterItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.success(player.getItemInHand(hand));
		}
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	/**
	 * Animates the target falling apart into a rain of shatter particles
	 */
	private void animateTargetShatter(ServerLevel world, LivingEntity target) {
		ItemStack itemId = Items.ROTTEN_FLESH.getDefaultInstance();
		for (int i = 0; i < 50 + (target.dimensions.width * 100); ++i) {
			double gaussX = world.random.nextGaussian() * 0.05D;
			double gaussY = world.random.nextGaussian() * 0.05D;
			double gaussZ = world.random.nextGaussian() * 0.05D;
			double gaussFactor = 10.0D;
			world.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemId), target.getX() + world.random.nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth() - gaussX * gaussFactor, target.getY() + world.random.nextFloat() * target.getBbHeight() - gaussY * gaussFactor, target.getZ() + world.random.nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth() - gaussZ * gaussFactor, 1, gaussX, gaussY, gaussZ, 0);
		}
	}

	/**
	 * What, if anything, is the player currently looking at?
	 */
	@Nullable
	private Entity getPlayerLookTarget(Level world, LivingEntity living) {
		Entity pointedEntity = null;
		double range = 20.0D;
		Vec3 srcVec = living.getEyePosition();
		Vec3 lookVec = living.getViewVector(1.0F);
		Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
		float var9 = 1.0F;
		List<Entity> possibleList = world.getEntities(living, living.getBoundingBox().expandTowards(lookVec.x * range, lookVec.y * range, lookVec.z * range).inflate(var9, var9, var9));
		double hitDist = 0;

		for (Entity possibleEntity : possibleList) {

			if (possibleEntity.isPickable()) {
				float borderSize = possibleEntity.getPickRadius();
				AABB collisionBB = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
				Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = 0.0D;
					}
				} else if (interceptPos.isPresent()) {
					double possibleDist = srcVec.distanceTo(interceptPos.get());

					if (possibleDist < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = possibleDist;
					}
				}
			}
		}
		return pointedEntity;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		Level level = living.level;

		if (stack.getDamageValue() == this.getMaxDamage(/*stack*/)) {
			// do not use
			living.stopUsingItem();
			return;
		}

		if (count % 5 == 0) {

			// is the player looking at an entity
			Entity pointedEntity = getPlayerLookTarget(level, living);

			if (pointedEntity instanceof LivingEntity target && !(target instanceof ArmorStand)) {
				if(level.isClientSide) {
					this.makeRedMagicTrail(level, living, target.eyeBlockPosition());
				}

				if (target.getHealth() <= 3 && target.hurt(DamageSource.indirectMagic(living, living).setProjectile(), 1)) {
					// make it explode
					if (!level.isClientSide) {
						if(!target.getType().is(EntityTagGenerator.LIFEDRAIN_DROPS_NO_FLESH)) {
							target.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH, level.random.nextInt(3)));
							this.animateTargetShatter((ServerLevel) level, target);
						}
						if (target instanceof Mob mob) {
							mob.spawnAnim();
						}
						target.playSound(TFSounds.SCEPTER_DRAIN, 1.0F, living.getVoicePitch());
						level.playSound(null, target.blockPosition(), EntityUtil.getDeathSound(target), SoundSource.HOSTILE, 1.0F, target.getVoicePitch());
						target.die(DamageSource.indirectMagic(living, living).setProjectile());
						target.discard();

						if (living instanceof Player player && !player.isCreative()) {
							stack.hurt(1, level.random, null);
						}
					}
					living.stopUsingItem();
				} else {
					// we have hit this creature recently
					if (target.hurt(DamageSource.indirectMagic(living, living).setProjectile(), 3)) {
						if (!level.isClientSide) {
							// only do lifting effect on creatures weaker than the player
							if (target.getHealth() <= living.getHealth()) {
								target.setDeltaMovement(0, 0.2D, 0);
							}

							target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
							if (count % 10 == 0) {
								// heal the player
								living.heal(1.0F);
								// and give foods
								if (living instanceof Player player)
									player.getFoodData().eat(1, 0.1F);
							}

							if (living instanceof Player player && !player.isCreative()) {
								stack.hurt(1, level.random, null);
							}
						}
					}
				}
			}
		}
	}

	private void makeRedMagicTrail(Level world, LivingEntity source, BlockPos target) {
		// make particle trail
		int particles = 32;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 1.0F;
			float f1 = 0.5F;
			float f2 = 0.5F;
			double handOffset = source.getItemInHand(InteractionHand.OFF_HAND).is(this) ? -0.35D : 0.35D;
			double tx = source.getX() + (target.getX() - source.getX()) * trailFactor + world.random.nextGaussian() * 0.005D + (handOffset * source.getDirection().get2DDataValue());
			double ty = source.getEyeY() - 0.1D + (target.getY() - source.getEyeY()) * trailFactor + world.random.nextGaussian() * 0.005D - 0.1D;
			double tz = source.getZ() - 0.35D + (target.getZ() - source.getZ()) * trailFactor + world.random.nextGaussian() * 0.005D + (0.35D * source.getDirection().get2DDataValue());
			world.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() == newStack.getItem();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}
