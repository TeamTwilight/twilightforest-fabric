package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.EntityHooks;

@Mixin(Entity.class)
public class EntityMixin {

	@ModifyExpressionValue(
		method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/Entity;stuckSpeedMultiplier:Lnet/minecraft/world/phys/Vec3;",
			opcode = Opcodes.GETFIELD
		)
	)
	private Vec3 twilightforest$resetStuckUnrestrained(Vec3 original) {
		return EntityHooks.resetStuckUnrestrained((Entity) (Object) this).stuckSpeedMultiplier;
	}
}