package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.TFBeanRegistry;
import twilightforest.util.TFStructureHelper;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.SortablePiece;

public class LichTowerRoomDecor extends TwilightJigsawPiece implements SortablePiece {
	private static LichTowerUtil lichTowerUtil;

	private static LichTowerUtil getLichTowerUtil() {
		if (lichTowerUtil == null) {
			lichTowerUtil = TFBeanRegistry.get(LichTowerUtil.class);
		}
		return lichTowerUtil;
	}

	public LichTowerRoomDecor(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_TOWER_DECOR.value(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(getLichTowerUtil().getRoomSpawnerProcessor()));
	}

	public LichTowerRoomDecor(int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_TOWER_DECOR.value(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(getLichTowerUtil().getRoomSpawnerProcessor()));
	}

	public static void addDecor(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int newDepth, StructureTemplateManager structureManager) {
		ResourceLocation decorId = getLichTowerUtil().rollRandomDecor(context.random(), false);
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), connection.orientation(), structureManager, decorId, "twilightforest:lich_tower/decor", context.random());

		if (placeableJunction != null) {
			LichTowerRoomDecor decor = new LichTowerRoomDecor(newDepth, structureManager, decorId, placeableJunction);
			pieceAccessor.addPiece(decor);
			decor.addJigsaws(parent, pieceAccessor, context);
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);

		switch (label) {
			case "sapling" -> {
				level.setBlock(pos, TFStructureHelper.randomPlant(random), Block.UPDATE_CLIENTS);
			}
			case "tree" -> {
				ResourceKey<ConfiguredFeature<?, ?>> randomTree = TFStructureHelper.randomTree(random.nextInt(4));
				Registry<ConfiguredFeature<?, ?>> featureRegistry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
				if (!featureRegistry.get(randomTree).place(level, chunkGen, random, pos)) {
					level.setBlock(pos, TFStructureHelper.randomPlant(random), Block.UPDATE_CLIENTS);
				}
			}
		}
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
	}

	@Override
	public int getSortKey() {
		return 2; // This piece must generate after LichTowerBase, which has 1
	}
}
