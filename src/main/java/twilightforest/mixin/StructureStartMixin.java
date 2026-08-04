package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(StructureStart.class)
public class StructureStartMixin {

	@WrapOperation(
		method = "loadStaticStart",
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraft/world/level/levelgen/structure/Structure;Lnet/minecraft/world/level/ChunkPos;ILnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"
		)
	)
	private static StructureStart twilightforest$loadStaticStart(
		Structure structure,
		ChunkPos chunkPos,
		int references,
		PiecesContainer pieceContainer,
		Operation<StructureStart> original,
		StructurePieceSerializationContext context,
		CompoundTag tag,
		long seed
	) {
		StructureStart start = original.call(structure, chunkPos, references, pieceContainer);

		return WorldgenHooks.loadStaticStart(
			start,
			pieceContainer,
			tag
		);
	}
}