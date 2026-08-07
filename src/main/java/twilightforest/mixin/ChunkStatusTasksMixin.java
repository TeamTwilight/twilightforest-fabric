package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(ChunkStatusTasks.class)
public class ChunkStatusTasksMixin {

	@Inject(
		method = "generateSurface",
		at = @At("TAIL")
	)
	private static void twilightforest$chunkBlanketing(
		WorldGenContext worldGenContext,
		ChunkStep step,
		StaticCache2D<?> cache,
		ChunkAccess chunk,
		CallbackInfoReturnable<?> cir
	) {
		if (worldGenContext.level() instanceof ServerLevel serverLevel) {
			WorldgenHooks.chunkBlanketing(chunk, serverLevel);
		}
	}
}