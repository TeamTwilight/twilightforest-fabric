package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.util.ConquerableStructure;

@Deprecated
public class LegacyStructure extends ConquerableStructure {
	public final TFLandmark feature;

	public static final Codec<LegacyStructure> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(TFLandmark.CODEC.fieldOf("legacy_landmark_start").forGetter(LegacyStructure::getFeatureType))
			.and(conquerStatusCodec(instance))
			.apply(instance, LegacyStructure::new)
	);

	public LegacyStructure(TFLandmark landmark, ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, HintConfig hintConfig, DecorationConfig decorationConfig, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, structureSettings);
		this.feature = landmark;
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return this.feature.provideFirstPiece(context.structureTemplateManager(), context.chunkGenerator(), random, x, y, z);
	}

	@Override
	@Deprecated
	protected boolean dontCenter() {
		return this.feature == TFLandmark.LICH_TOWER || this.feature == TFLandmark.TROLL_CAVE || this.feature == TFLandmark.YETI_CAVE;
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.LEGACY_LANDMARK.get();
	}

	public TFLandmark getFeatureType() {
		return this.feature;
	}
}
