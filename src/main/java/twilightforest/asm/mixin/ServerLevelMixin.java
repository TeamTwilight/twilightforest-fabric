package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.entity.PartEntity;
import twilightforest.fabric.interfaces.extension.ILevelExtension;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements ILevelExtension {

	@ModifyReturnValue(
		method = "getEntityOrPart(I)Lnet/minecraft/world/entity/Entity;",
		at = @At("RETURN")
	)
	public Entity twilightforest$getMultipart(Entity entity, int id) {
		if (entity == null) {
			Int2ObjectMap<PartEntity<?>> partEntityMap = twilightforest$getPartEntityMap();
			if (partEntityMap != null) {
				return partEntityMap.get(id);
			}
		}
		return entity;
	}
}