package twilightforest.asmhooks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.mixin.BeardifierAccessor;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

public class WorldgenHooks {

	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, structure -> structure instanceof CustomDensitySource)) {
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource) {
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));
			}
		}

		return customStructureTerraforms.iterator();
	}

	public static double getCustomDensity(double original, DensityFunction.FunctionContext context, @Nullable ObjectListIterator<DensityFunction> customDensities) {
		if (customDensities == null) {
			return original;
		}

		double density = 0;
		while (customDensities.hasNext()) {
			density += customDensities.next().compute(context);
		}
		customDensities.back(Integer.MAX_VALUE);
		return original + density;
	}

	public static Beardifier addPieceBeardifierModifiers(StructureManager structureManager, ChunkPos chunkPos, Beardifier original) {
		ObjectArrayList<Beardifier.Rigid> pieces = copyIterator(((BeardifierAccessor) (Object) original).codexTwilight$getPieceIterator());
		ObjectArrayList<JigsawJunction> junctions = copyIterator(((BeardifierAccessor) (Object) original).codexTwilight$getJunctionIterator());
		boolean changed = false;

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, structure -> true)) {
			TerrainAdjustment structureAdjustment = structureStart.getStructure().terrainAdaptation();
			for (StructurePiece piece : structureStart.getPieces()) {
				if (piece instanceof PieceBeardifierModifier modifier && piece.isCloseToChunk(chunkPos, 12)) {
					changed |= removeVanillaRigidForPiece(pieces, piece, structureAdjustment);
					TerrainAdjustment pieceAdjustment = modifier.getTerrainAdjustment();
					if (pieceAdjustment != TerrainAdjustment.NONE) {
						Beardifier.Rigid customRigid = new Beardifier.Rigid(modifier.getBeardifierBox(), pieceAdjustment, modifier.getGroundLevelDelta());
						if (!pieces.contains(customRigid)) {
							pieces.add(customRigid);
							changed = true;
						}
					}
				}
			}
		}

		return changed ? BeardifierAccessor.codexTwilight$create(pieces.iterator(), junctions.iterator()) : original;
	}

	private static <T> ObjectArrayList<T> copyIterator(ObjectListIterator<T> iterator) {
		ObjectArrayList<T> copy = new ObjectArrayList<>();
		while (iterator.hasNext()) {
			copy.add(iterator.next());
		}
		iterator.back(Integer.MAX_VALUE);
		return copy;
	}

	private static boolean removeVanillaRigidForPiece(ObjectArrayList<Beardifier.Rigid> pieces, StructurePiece piece, TerrainAdjustment structureAdjustment) {
		if (structureAdjustment == TerrainAdjustment.NONE) {
			return false;
		}

		int vanillaGroundLevelDelta = 0;
		if (piece instanceof PoolElementStructurePiece poolPiece) {
			if (poolPiece.getElement().getProjection() != StructureTemplatePool.Projection.TERRAIN_MATCHING) {
				return false;
			}
			vanillaGroundLevelDelta = poolPiece.getGroundLevelDelta();
		}

		Beardifier.Rigid vanillaRigid = new Beardifier.Rigid(piece.getBoundingBox(), structureAdjustment, vanillaGroundLevelDelta);
		return pieces.remove(vanillaRigid);
	}

	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData customStructureData) {
			return customStructureData.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		}
		return start;
	}
}
