package twilightforest.util;

import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import net.minecraft.Util;
import io.github.fabricators_of_create.porting_lib.block.EntityDestroyBlock;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.common.accessor.LivingEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
		/* rude type limit */
		return hardness >= 0f && hardness < 50f && !state.isAir()
				&& canEntityDestroyBlock(state, world, pos, entity);
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
		double range = player.getAttribute(PortingLibAttributes.REACH_DISTANCE).getValue();
		return rayTrace(player, modifier == null ? range : modifier.applyAsDouble(range));
	}

	public static SoundEvent getDeathSound(LivingEntity living) {
		return ((LivingEntityAccessor)living).port_lib$getDeathSound();
	}

	public static void killLavaAround(Entity entity) {
		AABB bounds = entity.getBoundingBox().inflate(9D);
		for (double x = bounds.minX; x < bounds.maxX; x++) {
			for (double z = bounds.minZ; z < bounds.maxZ; z++) {
				for (double y = bounds.minY; y < bounds.maxY; y++) {
					BlockPos pos = BlockPos.containing(x, y, z);
					BlockState state = entity.getLevel().getBlockState(pos);
					if (state.is(Blocks.LAVA)) {
						entity.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}

	// [VanillaCopy] with modifications: StructureTemplate.createEntityIgnoreException
	@Nullable
	private static <T extends Entity> T createEntityIgnoreException(EntityType<T> type, ServerLevelAccessor levelAccessor) {
		try {
			return type.create(levelAccessor.getLevel());
		} catch (Exception exception) {
			return null;
		}
	}

	public static void tryHangPainting(WorldGenLevel world, BlockPos pos, Direction direction, int width, int height, DefaultedRegistry<PaintingVariant> paintingRegistry, TagKey<PaintingVariant> tagAllowed, RandomSource random) {
		List<Holder<PaintingVariant>> matchedVariants = new ArrayList<>();
		for (Holder<PaintingVariant> paintingVariantHolder : paintingRegistry.getTagOrEmpty(tagAllowed)) {
			PaintingVariant variant = paintingVariantHolder.value();

			if (variant.getWidth() == width && variant.getHeight() == height)
				matchedVariants.add(paintingVariantHolder);
		}

		if (matchedVariants.isEmpty()) return;

		// Finally, pick a painting
		Optional<Holder<PaintingVariant>> variant = Util.getRandomSafe(matchedVariants, random);

		if (variant.isEmpty()) return; // Magic

		Painting painting = createEntityIgnoreException(EntityType.PAINTING, world);

		painting.setPos(pos.getX(), pos.getY(), pos.getZ());
		painting.setDirection(direction);
		painting.setVariant(variant.get());

		world.addFreshEntity(painting);
	}
}
