package twilightforest.world.components.structures.courtyard;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;

import java.util.HashSet;

public final class CourtyardTerraceTemplateProcessor extends StructureProcessor {
	public static final CourtyardTerraceTemplateProcessor INSTANCE = new CourtyardTerraceTemplateProcessor();
	public static final MapCodec<CourtyardTerraceTemplateProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

	private static final HashSet<BlockState> BLOCKS_REPLACE_TO_SLAB = new HashSet<>();

	static {
		BLOCKS_REPLACE_TO_SLAB.add(Blocks.STONE_BRICKS.defaultBlockState());
		BLOCKS_REPLACE_TO_SLAB.add(Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
		BLOCKS_REPLACE_TO_SLAB.add(Blocks.CRACKED_STONE_BRICKS.defaultBlockState());
		BLOCKS_REPLACE_TO_SLAB.add(Blocks.STONE_BRICK_SLAB.defaultBlockState());
		BLOCKS_REPLACE_TO_SLAB.add(Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState());
	}

	private CourtyardTerraceTemplateProcessor() {
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		BlockState newState = relativeBlockInfo.state();

		if (newState.getBlock() == Blocks.SANDSTONE_SLAB) {
			BlockState stateAt = level.getBlockState(relativeBlockInfo.pos());

			if (newState == Blocks.SANDSTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE)) {
				if (BLOCKS_REPLACE_TO_SLAB.contains(stateAt))
					return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), Blocks.STONE_BRICK_SLAB.defaultBlockState(), null);
				else if (stateAt.is(Blocks.AIR)) // Do not skip Cave Air blocks
					return null;
				else
					return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), Blocks.STONE_BRICKS.defaultBlockState(), null);
			}

			if (stateAt.isAir())
				return null;
			else
				return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), Blocks.STONE_BRICK_SLAB.defaultBlockState(), null);
		}

		return relativeBlockInfo;
	}

	@Override
	public StructureProcessorType<?> getType() {
		return TFStructureProcessors.COURTYARD_TERRACE.get();
	}
}
