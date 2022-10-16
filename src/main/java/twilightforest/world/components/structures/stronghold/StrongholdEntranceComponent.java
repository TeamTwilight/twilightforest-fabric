package twilightforest.world.components.structures.stronghold;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.init.TFLandmark;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

import java.util.List;


public class StrongholdEntranceComponent extends StructureTFStrongholdComponent {

	public final StrongholdPieces lowerPieces;

	public StrongholdEntranceComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSEnter.get(), nbt);

		this.deco = new StrongholdDecorator();

		lowerPieces = new StrongholdPieces();
	}

	public StrongholdEntranceComponent(TFLandmark feature, int i, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSEnter.get(), feature, i, Direction.SOUTH, x, y - 10, z);

		this.deco = new StrongholdDecorator();

		lowerPieces = new StrongholdPieces();
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor old, RandomSource random) {
		super.addChildren(parent, old, random);

		if (old instanceof StructurePiecesBuilder start) {
			List<StructurePiece> list = start.pieces;

			// make a random component in each direction
			lowerPieces.prepareStructurePieces();
			addNewComponent(parent, old, random, Rotation.NONE, 4, 1, 18);
			lowerPieces.prepareStructurePieces();
			if (listContainsBossRoom(list)) {
				lowerPieces.markBossRoomUsed();
			}
			addNewComponent(parent, old, random, Rotation.CLOCKWISE_90, -1, 1, 13);
			lowerPieces.prepareStructurePieces();
			if (listContainsBossRoom(list)) {
				lowerPieces.markBossRoomUsed();
			}
			addNewComponent(parent, old, random, Rotation.CLOCKWISE_180, 13, 1, -1);
			lowerPieces.prepareStructurePieces();
			if (listContainsBossRoom(list)) {
				lowerPieces.markBossRoomUsed();
			}
			addNewComponent(parent, old, random, Rotation.COUNTERCLOCKWISE_90, 18, 1, 4);
			if (!listContainsBossRoom(list)) {
				TwilightForestMod.LOGGER.fatal("Did not find boss room from exit 3 - EPIC FAIL");
			}
			BoundingBox shieldBox = BoundingBoxUtils.clone(this.boundingBox);

			int tStairs = 0;
			int tCorridors = 0;
			int deadEnd = 0;
			int tRooms = 0;
			int bossRooms = 0;

			// compute and generate MEGASHIELD
			for (StructurePiece component : list) {
				shieldBox.encapsulate(component.getBoundingBox());


				if (component instanceof StrongholdSmallStairsComponent && ((StrongholdSmallStairsComponent) component).hasTreasure) {
					tStairs++;
				}
				if (component instanceof StrongholdTreasureCorridorComponent) {
					tCorridors++;
				}
				if (component instanceof StrongholdDeadEndComponent) {
					deadEnd++;
				}
				if (component instanceof StrongholdTreasureRoomComponent) {
					tRooms++;
				}
				if (component instanceof StrongholdBossRoomComponent) {
					bossRooms++;
				}
			}

//		System.out.printf("MEGASHIELD computed!  %d, %d, %d to %d, %d, %d.\n", shieldBox.minX, shieldBox.minY, shieldBox.minZ, shieldBox.maxX, shieldBox.maxY, shieldBox.maxZ);
//		System.out.printf("Stronghold at this point contains %d elements.\n", pieceList.size());
//
//		StructureTFStrongholdShield shield = new StructureTFStrongholdShield(shieldBox.minX - 1, shieldBox.minY, shieldBox.minZ - 1, shieldBox.maxX, shieldBox.maxY, shieldBox.maxZ);
//		list.add(shield);

			// add the upper stronghold
			StructureTFStrongholdComponent accessChamber = new StrongholdAccessChamberComponent(getFeatureType(), 2, this.getOrientation(), boundingBox.minX() + 8, boundingBox.minY() + 7, boundingBox.minZ() + 4);
			list.add(accessChamber);
			accessChamber.addChildren(this, old, random);
		}
	}

	private boolean listContainsBossRoom(List<StructurePiece> list) {
		for (StructurePiece component : list) {
			if (component instanceof StrongholdBossRoomComponent) {
				return true;
			}
		}

		return false;
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return BoundingBox.orientBox(x, y, z, -1, -1, 0, 18, 7, 18, facing);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 6, 17, rand, deco.randomBlocks);

		// statues
		placeCornerStatue(world, 5, 1, 5, 0, sbb);
		placeCornerStatue(world, 5, 1, 12, 1, sbb);
		placeCornerStatue(world, 12, 1, 5, 2, sbb);
		placeCornerStatue(world, 12, 1, 12, 3, sbb);

		// statues
		this.placeWallStatue(world, 9, 1, 16, Rotation.NONE, sbb);
		this.placeWallStatue(world, 1, 1, 9, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 8, 1, 1, Rotation.CLOCKWISE_180, sbb);
		this.placeWallStatue(world, 16, 1, 8, Rotation.COUNTERCLOCKWISE_90, sbb);

		// doors
		this.placeDoors(world, sbb);
	}
}
