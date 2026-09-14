package twilightforest.asm.hooks.coremod;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.chunkgenerators.CustomTerrainBeardifier;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

import java.util.List;

public final class WorldgenHooks {
	public static DensityFunctions.BeardifierOrMarker gatherCustomTerrain(DensityFunctions.BeardifierOrMarker vanilla, StructureManager structureManager, ChunkAccess chunkAccess) {
		if (!(vanilla instanceof Beardifier beardifier))
			return vanilla;

		ChunkPos chunkPos = chunkAccess.getPos();
		List<StructureStart> structureStarts = structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource);

		if (structureStarts.isEmpty())
			return beardifier;

		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(structureStarts.size());

		for (StructureStart structureStart : structureStarts)
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		return customStructureTerraforms.isEmpty() ? beardifier : new CustomTerrainBeardifier(beardifier, customStructureTerraforms);
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