package twilightforest.world.components.processors;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;

import java.util.ArrayList;

public final class TargetedRotProcessor extends BlockRotProcessor {
	public static final MapCodec<TargetedRotProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.listOf().xmap(ImmutableSet::copyOf, ArrayList::new).fieldOf("blocks_to_rot").forGetter(p -> p.blocksToRot),
		Codec.FLOAT.fieldOf("integrity").orElse(1.0f).forGetter(p -> p.integrity)
	).apply(instance, TargetedRotProcessor::new));

	private final ImmutableSet<BlockState> blocksToRot;

	public TargetedRotProcessor(ImmutableSet<BlockState> blocksToRot, float integrity) {
		super(integrity);
		this.blocksToRot = blocksToRot;
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		if (!this.blocksToRot.contains(relativeBlockInfo.state())) return relativeBlockInfo;
		return super.processBlock(level, offset, pos, blockInfo, relativeBlockInfo, settings);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.TARGETED_ROT.get();
	}
}
