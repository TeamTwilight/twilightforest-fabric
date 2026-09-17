package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.MultipartHooks;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@ModifyExpressionValue(
		method = "sendDirtyEntityData()V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/level/ServerEntity;entity:Lnet/minecraft/world/entity/Entity;",
			opcode = Opcodes.GETFIELD,
			ordinal = 0
		)
	)
	private Entity twilightforest$sendDirtyEntityData(Entity original) {
		return MultipartHooks.sendDirtyEntityData(original);
	}
}