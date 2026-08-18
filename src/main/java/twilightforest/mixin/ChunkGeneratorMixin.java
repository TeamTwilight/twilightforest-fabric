package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.MapHooks;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

	@ModifyReturnValue(
		method = "findNearestMapStructure",
		at = @At("RETURN")
	)
	private Pair<BlockPos, Holder<Structure>> twilightforest$resolveNearestNonRandomSpreadMapStructure(
		Pair<BlockPos, Holder<Structure>> original,
		ServerLevel level,
		HolderSet<Structure> structure,
		BlockPos pos,
		int searchRadius,
		boolean skipKnownStructures
	) {
		return MapHooks.resolveNearestNonRandomSpreadMapStructure(
			original, level, structure, pos, searchRadius, skipKnownStructures
		);
	}
}
