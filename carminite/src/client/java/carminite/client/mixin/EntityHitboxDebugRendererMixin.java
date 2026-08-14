package carminite.client.mixin;

import carminite.entity.IMultiPartEntity;
import carminite.entity.PartEntity;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {

	@Inject(
		method = "showHitboxes(Lnet/minecraft/world/entity/Entity;FZ)V",
		at = @At("TAIL")
	)
	private void carminite$renderMultipartHitboxes(
		Entity entity,
		float partialTicks,
		boolean isServerEntity,
		CallbackInfo ci
	) {
		if (!isServerEntity && (entity instanceof IMultiPartEntity multipart && multipart.isMultipartEntity())) {
			for (PartEntity<?> part : multipart.getParts()) {
				Vec3 latestPartPosition = part.position();
				Vec3 currentPartPosition = part.getPosition(partialTicks);
				Vec3 partOffset = currentPartPosition.subtract(latestPartPosition);
				Gizmos.cuboid(part.getBoundingBox().move(partOffset), GizmoStyle.stroke(-16711936));
			}
		}
	}
}