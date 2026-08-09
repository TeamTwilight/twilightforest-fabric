package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

public final class StoneBricksVariants extends StructureProcessor {
	public static final StoneBricksVariants INSTANCE = new StoneBricksVariants();
	public static final MapCodec<StoneBricksVariants> CODEC = MapCodec.unit(() -> INSTANCE);

	private StoneBricksVariants() {
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(relativeBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 3);

		BlockState state = relativeBlockInfo.state();

		if (state.is(Blocks.STONE_BRICKS) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), random.nextBoolean() ? Blocks.MOSSY_STONE_BRICKS.defaultBlockState() : Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), null);

		if (state.is(Blocks.STONE_BRICK_STAIRS) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_STONE_BRICK_STAIRS), null);

		if (state.is(Blocks.STONE_BRICK_SLAB) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_STONE_BRICK_SLAB), null);

		if (state.is(Blocks.STONE_BRICK_WALL) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_STONE_BRICK_WALL), null);

		return relativeBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.STONE_BRICK_VARIANTS.get();
	}
}
