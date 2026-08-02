package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.WorldgenHooks;

/**
 * Replaces the NeoForge ASM transformer
 * {@code InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer}
 * which doesn't work on Fabric.
 * <p>
 * Uses {@link ModifyExpressionValue} to intercept the Beardifier creation and set
 * custom density functions on it. This approach is confirmed to work on Fabric
 * (matching the 1.20.1 Fabric port).
 */
@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {

	@ModifyExpressionValue(
		method = "createNoiseChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/Beardifier;forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"
		)
	)
	private Beardifier twilightforest$initializeCustomFields(
		Beardifier original,
		@Local(argsOnly = true) StructureManager structureManager,
		@Local(argsOnly = true) ChunkAccess chunkAccess
	) {
		((WorldgenHooks.CustomBeardifier) original).tf$setCustomDensities(
			WorldgenHooks.gatherCustomTerrain(structureManager, chunkAccess.getPos())
		);
		return original;
	}
}