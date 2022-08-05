package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;
import twilightforest.init.TFSounds;

import java.util.UUID;

public class TransformationDispenseBehavior extends DefaultDispenseItemBehavior {

	boolean fired = false;

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.getLevel();
		RandomSource random = level.getRandom();
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		if (!level.isClientSide()) {
			for (LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS)) {
				level.getRecipeManager().getAllRecipesFor(TFRecipes.TRANSFORM_POWDER_RECIPE.get()).forEach((recipe) -> {
					if (recipe.input() == livingentity.getType() || (recipe.isReversible() && recipe.result() == livingentity.getType())) {
						EntityType<?> type = recipe.isReversible() && recipe.result() == livingentity.getType() ? recipe.input() : recipe.result();
						Entity newEntity = type.create(level);
						if (newEntity != null) {
							newEntity.moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), livingentity.getYRot(), livingentity.getXRot());
							if (newEntity instanceof Mob mob && livingentity.getLevel() instanceof ServerLevelAccessor accessor) {
								mob.finalizeSpawn(accessor, livingentity.getLevel().getCurrentDifficultyAt(livingentity.blockPosition()), MobSpawnType.CONVERSION, null, null);
							}

							try {
								UUID uuid = newEntity.getUUID();
								newEntity.load(livingentity.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
								newEntity.setUUID(uuid);
							} catch (Exception e) {
								TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
							}

							livingentity.getLevel().addFreshEntity(newEntity);
							livingentity.discard();

							if (livingentity instanceof Mob && livingentity.getLevel().isClientSide()) {
								((Mob) livingentity).spawnAnim();
								((Mob) livingentity).spawnAnim();
							}
							livingentity.playSound(TFSounds.POWDER_USE.get(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
							stack.shrink(1);
							this.fired = true;
						}
					}
				});
			}
		}
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		if (this.fired) {
			super.playSound(source);
		} else {
			source.getLevel().levelEvent(1001, source.getPos(), 0);
		}
	}
}
