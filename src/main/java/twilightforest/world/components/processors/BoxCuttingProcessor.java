package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TFMain;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.BoundingBoxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Despite the name, any facts about actual Box Cutters being involved are lies
public final class BoxCuttingProcessor extends StructureProcessor {
	public static final MapCodec<BoxCuttingProcessor> CODEC = BoundingBox.CODEC.listOf().xmap(BoxCuttingProcessor::new, p -> p.cutouts).fieldOf("boxes");

	public final List<BoundingBox> cutouts;

	public BoxCuttingProcessor(List<BoundingBox> cutouts) {
		this.cutouts = cutouts;
	}

	@Override
	public StructureTemplate.@org.jspecify.annotations.Nullable StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
		for (BoundingBox cutout : this.cutouts)
			if (cutout.isInside(processedBlockInfo.pos()))
				return null;

		return processedBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.BOX_CUTTING_PROCESSOR;
	}

	public static BoxCuttingProcessor fromNBT(ListTag tag) {
		List<BoundingBox> boxes = new ArrayList<>();

		for (Tag tagElement : tag) {
			if (!(tagElement instanceof CompoundTag boxCompound)) continue;

			try {
				boxes.add(BoundingBoxUtils.NBTToBoundingBox(boxCompound));
			} catch (Throwable e) {
				TFMain.LOGGER.error("Invalid BoundingBox found in list", e);
			}
		}

		return new BoxCuttingProcessor(boxes);
	}

	public static BoxCuttingProcessor forLichTower(Map<BlockPos, Direction> sideTowerStarts) {
		return new BoxCuttingProcessor(sideTowerStarts
			.entrySet()
			.stream()
			.map(e -> BoundingBox.fromCorners(e.getKey(), e.getKey().relative(e.getValue(), 1).relative(e.getValue().getClockWise(), 4).above(6)))
			.collect(Collectors.toList())
		);
	}
}
