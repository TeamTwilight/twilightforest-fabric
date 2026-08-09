package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

public final class CobbleVariants extends StructureProcessor {
	public static final CobbleVariants INSTANCE = new CobbleVariants();
	public static final MapCodec<CobbleVariants> CODEC = MapCodec.unit(() -> INSTANCE);

	private CobbleVariants() {
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(relativeBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 2);

		BlockState state = relativeBlockInfo.state();
		Block block = state.getBlock();

		if (block == Blocks.COBBLESTONE && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);

		if (block == Blocks.COBBLESTONE_STAIRS && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_STAIRS), null);

		if (block == Blocks.COBBLESTONE_SLAB && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_SLAB), null);

		if (block == Blocks.COBBLESTONE_WALL && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_WALL), null);

		return relativeBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.COBBLE_VARIANTS.get();
	}
}
