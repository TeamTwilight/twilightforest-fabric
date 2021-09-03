package twilightforest.world.components.structures.courtyard;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.SlabType;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CourtyardTerraceTemplateProcessor extends RandomizedTemplateProcessor {

	public static final Codec<CourtyardTerraceTemplateProcessor> codecTerraceProcessor = Codec.FLOAT.fieldOf("integrity").orElse(1.0F).xmap(CourtyardTerraceTemplateProcessor::new, (obj) -> obj.integrity).codec();

    public CourtyardTerraceTemplateProcessor(float integrity) {
        super(integrity);
    }

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.COURTYARD_TERRACE;
	}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo oldinfo, StructureTemplate.StructureBlockInfo newinfo, StructurePlaceSettings settings) {
        Random random = settings.getRandom(newinfo.pos);

        if (!shouldPlaceBlock(random))
            return null;

        boolean shouldMakeNewBlockInfo = false;
        BlockState state = newinfo.state;

        final BlockState SMOOTHBRICK_SLAB_STATE = Blocks.STONE_BRICK_SLAB.defaultBlockState();
        final BlockState SMOOTHBRICK_STATE = Blocks.STONE_BRICKS.defaultBlockState();

        if (state == Blocks.SANDSTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE)) {
            BlockState stateCheck = world.getBlockState(newinfo.pos);
            if (stateCheck == SMOOTHBRICK_SLAB_STATE)
                return new StructureTemplate.StructureBlockInfo(newinfo.pos, SMOOTHBRICK_SLAB_STATE, null);
            else if (stateCheck.getMaterial() == Material.AIR)
                return null;
            else
                state = SMOOTHBRICK_STATE;

            shouldMakeNewBlockInfo = true;
        }

        if (state.getBlock() == Blocks.SANDSTONE_SLAB) {
            BlockState stateCheck = world.getBlockState(newinfo.pos);

            if (stateCheck.getMaterial() == Material.AIR)
                return null;
            else
                return new StructureTemplate.StructureBlockInfo(newinfo.pos, SMOOTHBRICK_SLAB_STATE, null);
        }

        Block block = state.getBlock();

        if (state == Blocks.STONE_BRICKS.defaultBlockState())
            return random.nextBoolean() ? (shouldMakeNewBlockInfo ? new StructureTemplate.StructureBlockInfo(newinfo.pos, state, null) : newinfo) :
                    new StructureTemplate.StructureBlockInfo(newinfo.pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.defaultBlockState() : Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), null);

        if (state == Blocks.SMOOTH_STONE_SLAB.defaultBlockState())
            return random.nextBoolean() ? newinfo : new StructureTemplate.StructureBlockInfo(newinfo.pos, Blocks.COBBLESTONE_SLAB.defaultBlockState(), null);

        if (block == TFBlocks.etched_nagastone)
            return random.nextBoolean() ? newinfo : new StructureTemplate.StructureBlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.etched_nagastone_mossy, TFBlocks.etched_nagastone_weathered), DirectionalBlock.FACING), null);

        if (block == TFBlocks.nagastone_pillar)
            return random.nextBoolean() ? newinfo : new StructureTemplate.StructureBlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_pillar_mossy, TFBlocks.nagastone_pillar_weathered), RotatedPillarBlock.AXIS), null);

        if (block == TFBlocks.nagastone_stairs_left)
            return random.nextBoolean() ? newinfo : new StructureTemplate.StructureBlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_left, TFBlocks.nagastone_stairs_weathered_left), StairBlock.FACING, StairBlock.HALF, StairBlock.SHAPE), null);

        if (block == TFBlocks.nagastone_stairs_right)
            return random.nextBoolean() ? newinfo : new StructureTemplate.StructureBlockInfo(newinfo.pos, translateState(state, randomBlock(random, TFBlocks.nagastone_stairs_mossy_right, TFBlocks.nagastone_stairs_weathered_right), StairBlock.FACING, StairBlock.HALF, StairBlock.SHAPE), null);

        return newinfo;
    }
}
