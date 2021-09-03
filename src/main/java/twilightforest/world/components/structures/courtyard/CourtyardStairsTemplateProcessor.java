package twilightforest.world.components.structures.courtyard;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.processors.RandomizedTemplateProcessor;
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CourtyardStairsTemplateProcessor extends RandomizedTemplateProcessor {

	public static final Codec<CourtyardStairsTemplateProcessor> codecStairsProcessor = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(CourtyardStairsTemplateProcessor::new, (obj) -> obj.integrity).codec();

    public CourtyardStairsTemplateProcessor(float integrity) {
        super(integrity);
    }

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.COURTYARD_STAIRS;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo oldInfo, StructureTemplate.StructureBlockInfo newInfo, StructurePlaceSettings placementSettingsIn) {
		Random random = placementSettingsIn.getRandom(newInfo.pos);

		if (shouldPlaceBlock(random)) {
			BlockState state = newInfo.state;
			Block block = state.getBlock();

			if (block == TFBlocks.nagastone_stairs_left)
				return random.nextBoolean() ? newInfo : new StructureTemplate.StructureBlockInfo(newInfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_weathered_left), StairBlock.FACING, StairBlock.HALF, StairBlock.SHAPE), null);

			if (block == TFBlocks.nagastone_stairs_right)
				return random.nextBoolean() ? newInfo : new StructureTemplate.StructureBlockInfo(newInfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_right, TFBlocks.nagastone_stairs_weathered_right), StairBlock.FACING, StairBlock.HALF, StairBlock.SHAPE), null);

			return newInfo;
		}

		return null;
	}
}
