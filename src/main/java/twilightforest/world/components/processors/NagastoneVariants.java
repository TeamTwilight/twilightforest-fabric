package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

public final class NagastoneVariants extends StructureProcessor {
	public static final NagastoneVariants INSTANCE = new NagastoneVariants();
	public static final MapCodec<NagastoneVariants> CODEC = MapCodec.unit(() -> INSTANCE);

	private NagastoneVariants() {
	}

	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(processedBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 5);

		BlockState state = processedBlockInfo.state();
		Block block = state.getBlock();

		if (block == TFBlocks.ETCHED_NAGASTONE && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_ETCHED_NAGASTONE : TFBlocks.CRACKED_ETCHED_NAGASTONE), null);

		if (block == TFBlocks.NAGASTONE_PILLAR && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_NAGASTONE_PILLAR : TFBlocks.CRACKED_NAGASTONE_PILLAR), null);

		if (block == TFBlocks.NAGASTONE_STAIRS_LEFT && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT : TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT), null);

		if (block == TFBlocks.NAGASTONE_STAIRS_RIGHT && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT : TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT), null);

		return processedBlockInfo;
	}

	@Override
	public StructureProcessorType<?> getType() {
		return TFStructureProcessors.NAGASTONE_VARIANTS;
	}
}
