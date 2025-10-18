package twilightforest.asmhooks;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;

import java.util.function.BiPredicate;

@SuppressWarnings({"JavadocReference", "unused"})
public class EntityHooks {

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterWalkTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.LivingEntity#canStandOnFluid(FluidState)}
	 */
	@Nullable
	public static Boolean processWaterWalking(LivingEntity livingEntity, FluidState fluidState) {
		if (!fluidState.is(FluidTags.WATER))
			return null;

		if (!TravellersModifiersManager.isModifierActive(livingEntity, livingEntity.getItemBySlot(EquipmentSlot.FEET), TravellersModifiersManager.WATER_WALK_MODIFIER))
			return null;

		boolean isWaterWalking = TravellersGearLogic.isBelowMaxWaterWalkingSubmergedHeight(livingEntity) && !livingEntity.isShiftKeyDown();
		if (livingEntity.getFluidTypeHeight(NeoForgeMod.WATER_TYPE.value()) > 0 && isWaterWalking && livingEntity.level().getGameTime() % 3 == 1)
			TravellersGearLogic.waterWalkingSplashEffect(livingEntity);
		return isWaterWalking;
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterSprintTransformer}<p/>
	 * <p>
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.player.LocalPlayer#aiStep()}
	 * Targets: {@link Entity#isInWater()}
	 */
	public static boolean unrestrainedSprintingInWater(LivingEntity livingEntity, boolean isInWater) {
		ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		if (!TravellersModifiersManager.isModifierActive(livingEntity, stack, TravellersModifiersManager.UNRESTRAINED_MODIFIER))
			return isInWater;
		return !livingEntity.canStandOnFluid(livingEntity.level().getFluidState(livingEntity.blockPosition())) && isInWater;
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterSprintTransformer}<p/>
	 * <p>
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.player.LocalPlayer#aiStep()}
	 * Targets: {@link net.neoforged.neoforge.common.extensions.IEntityExtension#isInFluidType(java.util.function.BiPredicate<net.neoforged.neoforge.fluids.FluidType, Double>)}
	 */
	public static BiPredicate<FluidType, Double> unrestrainedSwimPredicate(LivingEntity livingEntity, BiPredicate<FluidType, Double> o) {
		return (fluidType, height) -> {
			FluidState fs = livingEntity.level().getFluidState(livingEntity.blockPosition());
			boolean oResult = o.test(fluidType, height);
			if (fluidType != NeoForgeMod.WATER_TYPE.value())
				return oResult;
			return unrestrainedSprintingInWater(livingEntity, oResult);
		};
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.PathFinderUnrestrainedByLeashTransformer} <p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.PathfinderMob#shouldStayCloseToLeashHolder()}<br/>
	 * Targets: IRETURN
	 */
	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		return prior && !mob.hasData(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.UnrestrainedBlockSpeedAndJumpFactorTransformer} <p/>
	 *
	 * Injection Points:<br/>
	 * {@link net.minecraft.world.entity.Entity#getBlockJumpFactor()}<br/>
	 * {@link net.minecraft.world.entity.Entity#getBlockSpeedFactor()}<br/>
	 * Targets: FRETURN
	 */
	public static float resetFactorWithUnrestrained(float o, Entity entity) {
		return entity instanceof LivingEntity living && TravellersModifiersManager.isModifierActive(entity, living.getItemBySlot(EquipmentSlot.FEET), TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 1.0F : o;
	}
}
