package twilightforest.mixin;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(Beardifier.class)
public abstract class BeardifierMixin {
	@Inject(method = "forStructuresInChunk", at = @At("RETURN"), cancellable = true)
	private static void codexTwilight$addPieceBeardifierModifiers(StructureManager structureManager, ChunkPos chunkPos, CallbackInfoReturnable<Beardifier> callback) {
		callback.setReturnValue(WorldgenHooks.addPieceBeardifierModifiers(structureManager, chunkPos, callback.getReturnValue()));
	}
}
