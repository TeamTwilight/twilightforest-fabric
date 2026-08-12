package twilightforest.world.components.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.init.TFStructureProcessors;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.woods.WoodPalette;

import java.util.List;

public final class WoodMultiPaletteSwizzle extends StructureProcessor {
	private final List<Pair<Holder<WoodPalette>, Holder<WoodPalette>>> palettes;

	public static final MapCodec<WoodMultiPaletteSwizzle> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		Codec.pair(WoodPalettes.CODEC, WoodPalettes.CODEC).listOf().fieldOf("palettes").forGetter(s -> s.palettes)
	).apply(instance, WoodMultiPaletteSwizzle::new));

	public WoodMultiPaletteSwizzle(List<Pair<Holder<WoodPalette>, Holder<WoodPalette>>> palettes) {
		this.palettes = palettes;
	}

	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
		for (Pair<Holder<WoodPalette>, Holder<WoodPalette>> palettePair : this.palettes) {
			StructureTemplate.StructureBlockInfo newInfo = palettePair.getSecond().value().modifyBlockWithType(palettePair.getFirst().value(), processedBlockInfo);

			if (newInfo != processedBlockInfo) {
				return newInfo;
			}
		}

		return processedBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.PLANK_MULTISWIZZLE;
	}
}