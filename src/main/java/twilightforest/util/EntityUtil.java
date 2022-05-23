package twilightforest.util;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.github.fabricators_of_create.porting_lib.mixin.common.accessor.LivingEntityAccessor;
import io.github.fabricators_of_create.porting_lib.util.EntityDestroyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.DoubleUnaryOperator;

public class EntityUtil {

	public static BlockPos bossChestLocation(Mob boss) {
		return boss.getRestrictCenter() == BlockPos.ZERO ? boss.blockPosition() : boss.getRestrictCenter().below();
	}

	public static boolean canDestroyBlock(Level world, BlockPos pos, Entity entity) {
		return canDestroyBlock(world, pos, world.getBlockState(pos), entity);
	}

	public static boolean canDestroyBlock(Level world, BlockPos pos, BlockState state, Entity entity) {
		float hardness = state.getDestroySpeed(world, pos);
		return hardness >= 0f && hardness < 50f && !state.isAir()
				&& canEntityDestroyBlock(state, world, pos, entity)
				&& (/* rude type limit */!(entity instanceof LivingEntity)
				/*|| ForgeEventFactory.onEntityDestroyBlock((LivingEntity) entity, pos, state)*/);
	}

	public static boolean canEntityDestroyBlock(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
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

	/**
	 * [VanillaCopy] Entity.pick
	 */
	public static BlockHitResult rayTrace(Entity entity, double range) {
		Vec3 position = entity.getEyePosition(1.0F);
		Vec3 look = entity.getViewVector(1.0F);
		Vec3 dest = position.add(look.x * range, look.y * range, look.z * range);
		return entity.level.clip(new ClipContext(position, dest, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
	}

	public static BlockHitResult rayTrace(Player player) {
		return rayTrace(player, null);
	}

	public static BlockHitResult rayTrace(Player player, @Nullable DoubleUnaryOperator modifier) {
		double range = player.getAttribute(ReachEntityAttributes.REACH).getValue();
		return rayTrace(player, modifier == null ? range : modifier.applyAsDouble(range));
	}

	public static SoundEvent getDeathSound(LivingEntity living) {
		return ((LivingEntityAccessor)living).port_lib$getDeathSound();
	}
}
