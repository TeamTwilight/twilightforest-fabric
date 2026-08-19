package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BeardifierDuck;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {

	@ModifyExpressionValue(
		method = "createNoiseChunk(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;)Lnet/minecraft/world/level/levelgen/NoiseChunk;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/Beardifier;forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"
		)
	)
	private Beardifier twilightforest$gatherCustomTerrain(
		Beardifier original,
		@Local(argsOnly = true) StructureManager structureManager,
		@Local(argsOnly = true) ChunkAccess chunk
	) {
		((BeardifierDuck) original).twilightforest$setCustomStructureDensities(WorldgenHooks.gatherCustomTerrain(structureManager, chunk.getPos()));
		return original;
	}
}