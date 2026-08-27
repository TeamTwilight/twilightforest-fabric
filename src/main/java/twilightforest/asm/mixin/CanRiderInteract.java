package twilightforest.asm.mixin;

/**
 * Recreates NeoForge's {@code IEntityExtension.canRiderInteract} on Fabric,
 * where vanilla {@link net.minecraft.world.entity.Entity} has no such method.
 * Injected onto Entity via {@link EntityCanRiderInteractMixin}.
 */
public interface CanRiderInteract {
	default boolean canRiderInteract() {
		return false;
	}
}
