package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.hooks.EventHooks;
import twilightforest.fabric.interfaces.marker.ISpecialRunningEffectsBlock;

@Mixin(Entity.class)
public class EntityMixin {

	@Shadow
	private Level level;

	@Definition(
		id = "blockState",
		local = @Local(type = BlockState.class)
	)
	@Definition(
		id = "getRenderShape",
		method = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"
	)
	@Definition(
		id = "INVISIBLE",
		field = "Lnet/minecraft/world/level/block/RenderShape;INVISIBLE:Lnet/minecraft/world/level/block/RenderShape;"
	)
	@Expression("blockState.getRenderShape() != INVISIBLE")
	@ModifyExpressionValue(
		method = "spawnSprintParticle()V",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean twilightforest$addRunningEffects(
		boolean original,
		@Local(name = "pos") BlockPos pos,
		@Local(name = "blockState") BlockState blockState
	) {
		return original
			&& !(blockState.getBlock() instanceof ISpecialRunningEffectsBlock specialRunningEffectsBlock
			&& specialRunningEffectsBlock.addRunningEffects(blockState, this.level, pos, (Entity) (Object) this));
	}

	@WrapOperation(
		method = "rideTick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;tick()V"
		)
	)
	private void twilightforest$entityTick(
		Entity instance,
		Operation<Void> original
	) {
		if (!EventHooks.fireEntityTickPre(instance).isCanceled()) {
			original.call(instance);
			EventHooks.fireEntityTickPost(instance);
		}
	}
}