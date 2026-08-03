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

/**
 * Replaces the NeoForge ASM transformer
 * {@code ResolveNearestNonRandomSpreadMapStructureTransformer}
 * which doesn't work on Fabric.
 * <p>
 * Vanilla's {@link ChunkGenerator#findNearestMapStructure} only searches structures
 * whose placements are {@code RandomSpreadStructurePlacement} or
 * {@code ConcentricRingsStructurePlacement} subclasses. Twilight Forest landmarks use
 * the custom {@code LandmarkGridPlacement}, which is neither, so /locate (and the
 * /map_locator command) would always report "structure not found" for every
 * landmark_grid structure (Quest Grove, Naga Courtyard, Lich Tower, Labyrinth,
 * Final Castle, etc.). This mixin injects the TF landmark grid search as a fallback
 * right before the method returns, mirroring the NeoForge transformer.
 */
@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

	@Inject(
		method = "findNearestMapStructure",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$resolveNearestNonRandomSpreadMapStructure(
		ServerLevel level,
		HolderSet<Structure> targetStructures,
		BlockPos pos,
		int searchRadius,
		boolean skipKnownStructures,
		CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir
	) {
		cir.setReturnValue(MapHooks.resolveNearestNonRandomSpreadMapStructure(
			cir.getReturnValue(), level, targetStructures, pos, searchRadius, skipKnownStructures
		));
	}
}