package twilightforest.world.components.feature.templates;

import com.google.common.math.StatsAccumulator;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.Wraith;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureProcessors;
import twilightforest.loot.TFLootTables;
import twilightforest.util.features.FeatureLogic;

import java.util.ArrayList;
import java.util.List;

public class GraveyardFeature extends Feature<NoneFeatureConfiguration> {
	private static final ResourceLocation GRAVEYARD = TwilightForestMod.prefix("feature/graveyard/graveyard");
	private static final ResourceLocation TRAP = TwilightForestMod.prefix("feature/graveyard/grave_trap");

	public GraveyardFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	private static boolean offsetToAverageGroundLevel(WorldGenLevel world, BlockPos.MutableBlockPos startPos, Vec3i size) {
		StatsAccumulator heights = new StatsAccumulator();

		for (int dx = 0; dx < size.getX(); dx++) {
			for (int dz = 0; dz < size.getZ(); dz++) {
				int x = startPos.getX() + dx;
				int z = startPos.getZ() + dz;
				int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

				while (y >= 0) {
					BlockState state = world.getBlockState(new BlockPos(x, y, z));
					if (FeatureLogic.isBlockNotOk(state)) {
						return false;
					}
					if (FeatureLogic.isBlockOk(state)) {
						break;
					}
					y--;
				}

				if (y < 0) {
					return false;
				}

				heights.add(y);
			}
		}

		if (heights.populationStandardDeviation() > 2.0D) {
			return false;
		}

		int baseY = (int) Math.round(heights.mean());
		int maxY = (int) heights.max();
		startPos.setY(baseY);

		return FeatureLogic.isAreaClear(world, startPos.above(maxY - baseY + 1), startPos.offset(size));
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource random = ctx.random();
		int flags = Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS;

		StructureTemplateManager templateManager = world.getLevel().getServer().getStructureManager();
		StructureTemplate base = templateManager.getOrCreate(GRAVEYARD);
		StructureTemplate trap = templateManager.getOrCreate(TRAP);
		if (base == null || trap == null) {
			return false;
		}

		List<GraveTemplate> graves = new ArrayList<>();
		for (GraveType type : GraveType.VALUES) {
			StructureTemplate grave = templateManager.getOrCreate(type.resourceLocation);
			if (grave == null) {
				return false;
			}
			graves.add(new GraveTemplate(type, grave));
		}

		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Mirror[] mirrors = Mirror.values();
		Mirror mirror = mirrors[random.nextInt(mirrors.length + 1) % mirrors.length];

		Vec3i transformedSize = base.getSize(rotation);
		Vec3i transformedGraveSize = graves.get(0).template.getSize(rotation);
		ChunkPos chunkPos = new ChunkPos(pos.offset(-8, 0, -8));
		ChunkPos chunkEndPos = new ChunkPos(pos.offset(-8, 0, -8).offset(transformedSize));
		BoundingBox boundingBox = new BoundingBox(
				chunkPos.getMinBlockX() + 8,
				world.getMinBuildHeight(),
				chunkPos.getMinBlockZ() + 8,
				chunkEndPos.getMaxBlockX() + 8,
				world.getMaxBuildHeight(),
				chunkEndPos.getMaxBlockZ() + 8);
		StructurePlaceSettings placementSettings = new StructurePlaceSettings()
				.setMirror(mirror)
				.setRotation(rotation)
				.setBoundingBox(boundingBox)
				.setRandom(random);

		BlockPos posSnap = chunkPos.getWorldPosition().offset(8, pos.getY() - 1, 8);
		BlockPos.MutableBlockPos startPos = new BlockPos.MutableBlockPos(posSnap.getX(), posSnap.getY(), posSnap.getZ());
		if (!offsetToAverageGroundLevel(world, startPos, transformedSize)) {
			return false;
		}

		BlockPos placementPos = base.getZeroPositionWithTransform(startPos, mirror, rotation).offset(1, -1, 0);
		Vec3i size = transformedSize.offset(-1, 0, -1);
		Vec3i graveSize = transformedGraveSize.offset(-1, 0, -1);

		base.placeInWorld(world, placementPos, placementPos, placementSettings.addProcessor(WebTemplateProcessor.INSTANCE), random, flags);
		List<StructureTemplate.StructureBlockInfo> data = new ArrayList<>(base.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK));

		BlockPos start = startPos.offset(1, 1, 0);
		BlockPos end = start.offset(size.getX(), 0, size.getZ());

		for (int x = 1; x <= size.getX() - 1; x++) {
			for (int z = 1; z <= size.getZ() - 1; z++) {
				if (world.isEmptyBlock(start.offset(x, 0, z)) && random.nextInt(12) == 0) {
					world.setBlock(start.offset(x, 0, z), Blocks.COBWEB.defaultBlockState(), flags);
				}
			}
		}

		BlockPos inner = start.offset(2, 0, 2);
		BlockPos bound = end.offset(-2, 0, -2);
		BlockPos innerSize = new BlockPos(bound.getX() - inner.getX(), bound.getY() - inner.getY(), bound.getZ() - inner.getZ());
		BlockPos fixed = inner.offset(
				(rotation == Rotation.CLOCKWISE_180 ? graveSize.getX() : 0) + (mirror == Mirror.FRONT_BACK ? transformedGraveSize.getX() - 1 : 0) * (rotation == Rotation.CLOCKWISE_180 ? -1 : 1),
				0,
				(rotation == Rotation.COUNTERCLOCKWISE_90 ? graveSize.getZ() : 0) + (mirror == Mirror.FRONT_BACK ? transformedGraveSize.getZ() - 1 : 0) * (rotation == Rotation.COUNTERCLOCKWISE_90 ? -1 : 1)
		);
		BlockPos fixedSize = innerSize.offset(-graveSize.getX(), 0, -graveSize.getZ());
		BlockPos chestOffset = new BlockPos(random.nextInt(2) - (mirror == Mirror.FRONT_BACK ? 1 : 0), 1, 0).rotate(rotation);

		for (int x = 0; x <= fixedSize.getX(); x += (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90 ? 2 : 5)) {
			for (int z = 0; z <= fixedSize.getZ(); z += (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180 ? 2 : 5)) {
				if (x == innerSize.getX() / 2 || z == innerSize.getZ() / 2) {
					continue;
				}

				BlockPos placement = fixed.offset(x, -2, z);
				GraveTemplate grave = graves.get(random.nextInt(graves.size()));
				grave.template.placeInWorld(world, placement, placement, placementSettings, random, flags);
				data.addAll(grave.template.filterBlocks(placement, placementSettings, Blocks.STRUCTURE_BLOCK));

				if (grave.type == GraveType.Full && random.nextBoolean()) {
					if (random.nextInt(3) == 0) {
						placement = placement.offset(new BlockPos(mirror == Mirror.FRONT_BACK ? 1 : -1, 0, mirror == Mirror.LEFT_RIGHT ? 1 : -1).rotate(rotation));
						trap.placeInWorld(world, placement, placement, placementSettings, random, flags);
					}
					data.addAll(trap.filterBlocks(placement, placementSettings, Blocks.STRUCTURE_BLOCK));

					BlockPos chestPos = placement.offset(chestOffset);
					if (world.setBlock(chestPos, Blocks.TRAPPED_CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST).rotate(rotation).mirror(mirror), flags)) {
						TFLootTables.generateChestContents(world, chestPos, TFLootTables.GRAVEYARD);
						world.setBlock(chestPos.below(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
					}

					Wraith wraith = new Wraith(TFEntities.WRAITH.get(), world.getLevel());
					wraith.setPos(placement.getX(), placement.getY(), placement.getZ());
					wraith.finalizeSpawn(world, world.getCurrentDifficultyAt(placement), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(wraith);
				}
			}
		}

		for (StructureTemplate.StructureBlockInfo info : data) {
			if (info.nbt() != null && StructureMode.valueOf(info.nbt().getString("mode")) == StructureMode.DATA) {
				String metadata = info.nbt().getString("metadata");
				BlockPos markerPos = info.pos();
				if ("spawner".equals(metadata)) {
					world.removeBlock(markerPos, false);
					if (random.nextInt(4) == 0 && world.setBlock(markerPos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_ALL)) {
						BlockEntity blockEntity = world.getBlockEntity(markerPos);
						if (blockEntity instanceof SpawnerBlockEntity spawner) {
							spawner.setEntityId(TFEntities.RISING_ZOMBIE.get(), random);
						}
					}
				}
			}
		}

		return true;
	}

	private record GraveTemplate(GraveType type, StructureTemplate template) {
	}

	private enum GraveType {
		Full(TwilightForestMod.prefix("feature/graveyard/grave_full")),
		Upper(TwilightForestMod.prefix("feature/graveyard/grave_upper")),
		Lower(TwilightForestMod.prefix("feature/graveyard/grave_lower"));

		private static final GraveType[] VALUES = values();
		private final ResourceLocation resourceLocation;

		GraveType(ResourceLocation resourceLocation) {
			this.resourceLocation = resourceLocation;
		}
	}

	public static class WebTemplateProcessor extends StructureProcessor {
		public static final WebTemplateProcessor INSTANCE = new WebTemplateProcessor();
		public static final Codec<WebTemplateProcessor> CODEC = Codec.unit(() -> INSTANCE);

		private WebTemplateProcessor() {
		}

		@Override
		public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos piecePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
			return modifiedBlockInfo.state().getBlock() == Blocks.GRASS_BLOCK || settings.getRandom(modifiedBlockInfo.pos()).nextInt(5) != 0
					? modifiedBlockInfo
					: new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), Blocks.COBWEB.defaultBlockState(), null);
		}

		@Override
		protected StructureProcessorType<?> getType() {
			return TFStructureProcessors.WEB.get();
		}
	}
}
