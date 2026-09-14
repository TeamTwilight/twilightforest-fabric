package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import twilightforest.asm.hooks.coremod.WorldgenHooks;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {

	@ModifyArg(
		method = "createNoiseChunk(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;)Lnet/minecraft/world/level/levelgen/NoiseChunk;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseChunk;forChunk(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/DensityFunctions$BeardifierOrMarker;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Lnet/minecraft/world/level/levelgen/Aquifer$FluidPicker;Lnet/minecraft/world/level/levelgen/blending/Blender;)Lnet/minecraft/world/level/levelgen/NoiseChunk;"
		),
		index = 2
	)
	private DensityFunctions.BeardifierOrMarker twilightforest$gatherCustomTerrain(
		DensityFunctions.BeardifierOrMarker beardifierOrMarker,
		@Local(argsOnly = true, name = "structureManager") StructureManager structureManager,
		@Local(argsOnly = true, name = "chunk") ChunkAccess chunk
	) {
		return WorldgenHooks.gatherCustomTerrain(beardifierOrMarker, structureManager, chunk);
	}
}