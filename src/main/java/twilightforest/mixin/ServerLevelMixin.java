package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.MultipartHooks;

/**
 * Handles server-level hooks for Twilight Forest dimension transitions.
 * Injects into addEntity to ensure multipart entity tracking is properly set up
 * when entities enter or are created in the TF dimension.
 * <p>
 * Uses {@link MultipartHooks} instead of direct @Autowired injection because
 * Mixin classes cannot be referenced directly by the beanification framework.
 */
@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	/**
	 * When an entity is added to the level, ensure multipart entity data
	 * is properly synced. This is critical for TF bosses like Hydra, Naga,
	 * Snow Queen whose parts need to be tracked by the server.
	 * Note: addEntity returns boolean in 1.21.1, so CallbackInfoReturnable is required.
	 */
	@Inject(
		method = "addEntity",
		at = @At("HEAD")
	)
	private void twilightforest$onAddEntity(
		Entity entity,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (entity != null) {
			MultipartHooks.sendDirtyEntityData(entity);
		}
	}
}