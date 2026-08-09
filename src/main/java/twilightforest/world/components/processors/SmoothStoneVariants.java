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
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

public class SmoothStoneVariants extends StructureProcessor {
	public static final SmoothStoneVariants INSTANCE = new SmoothStoneVariants();
	public static final MapCodec<SmoothStoneVariants> CODEC = MapCodec.unit(() -> INSTANCE);

	private SmoothStoneVariants() {
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(relativeBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 4);

		if (relativeBlockInfo.state().is(Blocks.SMOOTH_STONE_SLAB) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(relativeBlockInfo.state(), Blocks.COBBLESTONE_SLAB), null);

		if (relativeBlockInfo.state().is(Blocks.SMOOTH_STONE) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), Blocks.COBBLESTONE.defaultBlockState(), null);

		return relativeBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SMOOTH_STONE_VARIANTS.get();
	}
}
