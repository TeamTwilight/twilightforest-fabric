package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.tags.TFBlockTags;

public final class SoftReplaceProcessor extends StructureProcessor {
	public static final SoftReplaceProcessor INSTANCE = new SoftReplaceProcessor();
	public static final MapCodec<SoftReplaceProcessor> CODEC = MapCodec.unit(INSTANCE);

	private SoftReplaceProcessor() {
	}

	@Override
	public StructureTemplate.@Nullable StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
		BlockState blockAt = level.getBlockState(processedBlockInfo.pos());

		boolean isReplaceableAt = blockAt.canBeReplaced() || blockAt.is(TFBlockTags.WORLDGEN_REPLACEABLES);

		if (isReplaceableAt) {
			return processedBlockInfo;
		}

		// Replace partial blocks such as slabs or fences, if the replacement is a solid block
		if (!this.isFullBlock(blockAt) && this.isFullBlock(processedBlockInfo.state())) {
			return processedBlockInfo;
		}

		return null;
	}

	private boolean isFullBlock(BlockState state) {
		// the BlockState#isSolid() is not reliable in checking for a full block
		Block block = state.getBlock();
		return !(block instanceof FenceBlock || block instanceof WallBlock || block instanceof SlabBlock || block instanceof StairBlock);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SOFT_REPLACE;
	}
}