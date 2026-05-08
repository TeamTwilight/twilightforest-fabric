package twilightforest.asmhooks;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;

public class EntityHooks {

	@Nullable
	public static Boolean processWaterWalking(boolean original, LivingEntity livingEntity, FluidState fluidState) {
		if (!fluidState.is(FluidTags.WATER)) {
			return original;
		}
		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.WATER_WALK_MODIFIER)) {
			return original;
		}

		boolean waterWalking = TravellersGearLogic.isBelowMaxWaterWalkingSubmergedHeight(livingEntity) && !livingEntity.isShiftKeyDown();
		if (livingEntity.getFluidHeight(FluidTags.WATER) > 0.0D && waterWalking && livingEntity.level().getGameTime() % 3L == 1L) {
			TravellersGearLogic.waterWalkingSplashEffect(livingEntity);
		}
		return waterWalking;
	}

	public static boolean unrestrainedSprintingInWater(boolean isInWater, LivingEntity livingEntity) {
		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return isInWater;
		}
		return !livingEntity.canStandOnFluid(livingEntity.level().getFluidState(livingEntity.blockPosition())) && isInWater;
	}

	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		return prior && !((AttachmentTarget) mob).hasAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
	}

	public static float resetFactorWithUnrestrained(float original, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 1.0F : original;
	}

	public static Entity resetStuckUnrestrained(Entity entity) {
		if (!(entity instanceof LivingEntity living) || living.stuckSpeedMultiplier.lengthSqr() <= 1.0E-7D || !TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return entity;
		}
		living.stuckSpeedMultiplier = Vec3.ZERO;
		return entity;
	}
}
