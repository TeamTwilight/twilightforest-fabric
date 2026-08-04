package twilightforest.asmhooks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

@SuppressWarnings({"JavadocReference", "unused"})
public class WorldgenHooks {

	/**
	 * Duck-typing interface implemented by {@link BeardifierMixin} to store custom density
	 * functions directly on the Beardifier instance. This avoids the thread-safety and
	 * memory-leak issues of a static IdentityHashMap, since chunk generation is
	 * multi-threaded in 1.21.1.
	 */
	public interface CustomBeardifier {
		void tf$setCustomDensities(ObjectListIterator<DensityFunction> densities);
		ObjectListIterator<DensityFunction> tf$getCustomDensities();
	}

	public static void setBeardifierCustomDensities(Beardifier beardifier, ObjectListIterator<DensityFunction> customDensities) {
		((CustomBeardifier) beardifier).tf$setCustomDensities(customDensities);
	}

	@Nullable
	public static ObjectListIterator<DensityFunction> getBeardifierCustomDensities(Beardifier beardifier) {
		return ((CustomBeardifier) beardifier).tf$getCustomDensities();
	}

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
			double density = customDensities.next().compute(context);
			newDensity += density;
		}
		customDensities.back(Integer.MAX_VALUE);

		return o + newDensity;
	}

	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	public static void chunkBlanketing(ChunkAccess access, ServerLevel serverLevel) {
		ChunkBlanketProcessors.chunkBlanketing(access, serverLevel);
	}

	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}
}
