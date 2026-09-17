package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.MapHooks;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

	@ModifyReturnValue(
		method = "findNearestMapStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;",
		at = @At("RETURN")
	)
	private @Nullable Pair<BlockPos, Holder<Structure>> twilightforest$resolveNearestNonRandomSpreadMapStructure(
		@Nullable Pair<BlockPos, Holder<Structure>> original,
		@Local(argsOnly = true, name = "level") ServerLevel level,
		@Local(argsOnly = true, name = "wantedStructures") HolderSet<Structure> wantedStructures,
		@Local(argsOnly = true, name = "pos") BlockPos pos,
		@Local(name = "maxSearchRadius", argsOnly = true) int maxSearchRadius,
		@Local(name = "createReference", argsOnly = true) boolean createReference
	) {
		return MapHooks.resolveNearestNonRandomSpreadMapStructure(original, level, wantedStructures, pos, maxSearchRadius, createReference);
	}
}