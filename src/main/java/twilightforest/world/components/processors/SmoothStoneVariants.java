package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

public class SmoothStoneVariants extends StructureProcessor {
	public static final SmoothStoneVariants INSTANCE = new SmoothStoneVariants();
	public static final MapCodec<SmoothStoneVariants> CODEC = MapCodec.unit(() -> INSTANCE);

	private SmoothStoneVariants() {
	}

	@Override
	public StructureTemplate.@Nullable StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(processedBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 4);

		if (processedBlockInfo.state().is(Blocks.SMOOTH_STONE_SLAB) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(processedBlockInfo.state(), Blocks.COBBLESTONE_SLAB), null);

		if (processedBlockInfo.state().is(Blocks.SMOOTH_STONE) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), Blocks.COBBLESTONE.defaultBlockState(), null);

		return processedBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SMOOTH_STONE_VARIANTS;
	}
}
