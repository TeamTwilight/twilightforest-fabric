package twilightforest.asm.hooks.coremod;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.jspecify.annotations.Nullable;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

public final class WorldgenHooks {
	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		return customStructureTerraforms.iterator();
	}

	public static double getCustomDensity(double o, DensityFunction.FunctionContext context, @Nullable ObjectListIterator<DensityFunction> customDensities) {
		if (customDensities == null)
			return o;

		double newDensity = 0;

		while (customDensities.hasNext()) {
			newDensity += customDensities.next().compute(context);
		}
		customDensities.back(Integer.MAX_VALUE);

		return o + newDensity;
	}

	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}
}