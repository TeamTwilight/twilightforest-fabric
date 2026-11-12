package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;

import java.util.Arrays;
import java.util.List;

public class UpdateMarkingProcessor implements StructureProcessor {
	public static final MapCodec<UpdateMarkingProcessor> CODEC = Block.CODEC.codec().listOf().xmap(UpdateMarkingProcessor::new, p -> p.blocksToMarkUpdate).fieldOf("mark_updates");

	private final List<Block> blocksToMarkUpdate;

	public static UpdateMarkingProcessor forBlocks(Block... blocks) {
		return new UpdateMarkingProcessor(Arrays.asList(blocks));
	}

	public UpdateMarkingProcessor(List<Block> blocksToMarkUpdate) {
		this.blocksToMarkUpdate = blocksToMarkUpdate;
	}

	@Override
	public StructureTemplate.@Nullable StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
		if (this.blocksToMarkUpdate.contains(processedBlockInfo.state().getBlock())) {
			if (level.getChunk(processedBlockInfo.pos()) instanceof ProtoChunk protoChunk) {
				protoChunk.markPosForPostProcessing(processedBlockInfo.pos());
			}
		}

		return processedBlockInfo;
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return TFStructureProcessors.UPDATE_MARKING_PROCESSOR;
	}
}