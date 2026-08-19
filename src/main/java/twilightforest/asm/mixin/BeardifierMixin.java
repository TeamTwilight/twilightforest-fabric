package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.world.IPieceBeardifierModifier;

import java.util.List;

@Mixin(Beardifier.class)
public class BeardifierMixin {

	@WrapOperation(
		method = "forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;isCloseToChunk(Lnet/minecraft/world/level/ChunkPos;I)Z"
		)
	)
	private static boolean twilightforest$handleBeardifierModifier(
		StructurePiece instance,
		ChunkPos pos,
		int distance,
		Operation<Boolean> original,
		@Local(name = "rigids") List<Beardifier.Rigid> rigids,
		@Local(name = "anyPieceBoundingBox") LocalRef<BoundingBox> anyPieceBoundingBox
	) {
		boolean close = original.call(instance, pos, distance);

		if (!(instance instanceof IPieceBeardifierModifier modifier)) {
			return close;
		}

		if (!close) {
			return false;
		}

		TerrainAdjustment adjustment = modifier.getTerrainAdjustment();

		if (adjustment != TerrainAdjustment.NONE) {
			BoundingBox box = modifier.getBeardifierBox();

			rigids.add(new Beardifier.Rigid(
				box,
				adjustment,
				modifier.getGroundLevelDelta()
			));

			BoundingBox current = anyPieceBoundingBox.get();

			anyPieceBoundingBox.set(
				current == null
					? box
					: BoundingBox.encapsulating(current, box)
			);
		}

		return false;
	}
}