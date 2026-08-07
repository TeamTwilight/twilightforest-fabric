package twilightforest.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.MapHooks;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

	@Inject(
		method = "findNearestMapStructure",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$resolveNearestNonRandomSpreadMapStructure(
		ServerLevel level,
		HolderSet<Structure> structure,
		BlockPos pos,
		int searchRadius,
		boolean skipKnownStructures,
		CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir
	) {
		cir.setReturnValue(MapHooks.resolveNearestNonRandomSpreadMapStructure(
			cir.getReturnValue(), level, structure, pos, searchRadius, skipKnownStructures
		));
	}
}