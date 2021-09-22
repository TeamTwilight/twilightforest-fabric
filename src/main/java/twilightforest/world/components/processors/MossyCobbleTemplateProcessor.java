package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public class MossyCobbleTemplateProcessor extends RandomizedTemplateProcessor {
	public static final Codec<MossyCobbleTemplateProcessor> codecMossyProcessor = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(MossyCobbleTemplateProcessor::new, (obj) -> obj.integrity).codec();

	public static final MossyCobbleTemplateProcessor INSTANCE = new MossyCobbleTemplateProcessor(1.0f);

	public MossyCobbleTemplateProcessor(float integrity) {
        super(integrity);
    }

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.MOSSY_COBBLE;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_215194_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn) {
		Random random = placementSettingsIn.getRandom(pos);

		if (shouldPlaceBlock(random)) {
			BlockState state = blockInfo.state;
			Block block = state.getBlock();

			if (block == Blocks.COBBLESTONE)
				return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);

			if (block == Blocks.COBBLESTONE_STAIRS)
				return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_STAIRS), null);

			if (block == Blocks.COBBLESTONE_SLAB)
				return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_SLAB), null);

			if (block == Blocks.COBBLESTONE_WALL)
				return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_WALL), null);

			return blockInfo;
		}

		return null;
	}
}
