package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.WorldgenHooks;

@Mixin(StructureStart.class)
public class StructureStartMixin {

	@ModifyReturnValue(
		method = "loadStaticStart(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Lnet/minecraft/nbt/CompoundTag;J)Lnet/minecraft/world/level/levelgen/structure/StructureStart;",
		at = @At(
			value = "RETURN",
			ordinal = 2
		)
	)
	private static StructureStart twilightforest$loadStaticStart(
		StructureStart original,
		@Local(argsOnly = true, name = "tag") CompoundTag tag,
		@Local(name = "pieces") PiecesContainer pieces
	) {
		return WorldgenHooks.loadStaticStart(original, pieces, tag);
	}
}