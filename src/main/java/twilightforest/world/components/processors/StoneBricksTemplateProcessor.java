package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public class StoneBricksTemplateProcessor extends RandomizedTemplateProcessor {
	public static final Codec<StoneBricksTemplateProcessor> codecBricksProcessor = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(StoneBricksTemplateProcessor::new, (obj) -> obj.integrity).codec();

	public static final StoneBricksTemplateProcessor INSTANCE = new StoneBricksTemplateProcessor(1.0f);

	public StoneBricksTemplateProcessor(float integrity) {
        super(integrity);
    }

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.STONE_BRICKS;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo originalBlock, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn) {
		Random random = placementSettingsIn.getRandom(blockInfo.pos);

		if (this.shouldPlaceBlock(random)) {
			if (blockInfo.state.getBlock() == Blocks.STONE_BRICKS && random.nextBoolean())
				return new StructureTemplate.StructureBlockInfo(blockInfo.pos, random.nextBoolean() ? Blocks.MOSSY_STONE_BRICKS.defaultBlockState() : Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), blockInfo.nbt);

			return blockInfo;
		}

		return null;
	}
}
