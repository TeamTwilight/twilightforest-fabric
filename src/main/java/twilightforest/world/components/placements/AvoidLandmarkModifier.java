package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFFeatureModifiers;

import java.util.stream.Stream;

public class AvoidLandmarkModifier extends PlacementModifier {

	public static final Codec<AvoidLandmarkModifier> CODEC = RecordCodecBuilder.<AvoidLandmarkModifier>create(instance -> instance.group(
			Codec.BOOL.fieldOf("occupies_surface").forGetter(o -> o.occupiesSurface),
			Codec.BOOL.fieldOf("occupies_underground").forGetter(o -> o.occupiesUnderground),
			Codec.INT.fieldOf("additional_clearance").forGetter(o -> o.additionalClearance)
	).apply(instance, AvoidLandmarkModifier::new)).flatXmap(AvoidLandmarkModifier::validate, AvoidLandmarkModifier::validate);

	private final boolean occupiesSurface;
	private final boolean occupiesUnderground;
	private final int additionalClearance;

	public AvoidLandmarkModifier(boolean occupiesSurface, boolean occupiesUnderground, int additionalClearance) {
		this.occupiesSurface = occupiesSurface;
		this.occupiesUnderground = occupiesUnderground;
		this.additionalClearance = additionalClearance;
	}

	public static AvoidLandmarkModifier checkSurface() {
		return new AvoidLandmarkModifier(true, false, 0);
	}

	public static AvoidLandmarkModifier checkUnderground() {
		return new AvoidLandmarkModifier(false, true, 0);
	}

	public static AvoidLandmarkModifier checkBoth() {
		return new AvoidLandmarkModifier(true, true, 0);
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext worldDecoratingHelper, RandomSource random, BlockPos blockPos) {
		if (!(worldDecoratingHelper.getLevel().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorTwilight tfChunkGen))
			return Stream.of(blockPos);

		// Feature Center
		BlockPos.MutableBlockPos featurePos = LegacyLandmarkPlacements.getNearestCenterXZ(blockPos.getX() >> 4, blockPos.getZ() >> 4).mutable();

		final TFLandmark landmark = tfChunkGen.pickLandmarkForChunk(new ChunkPos(featurePos), worldDecoratingHelper.getLevel());

		if ((!occupiesSurface || landmark.isSurfaceDecorationsAllowed()) && (!occupiesUnderground || landmark.isUndergroundDecoAllowed()))
			return Stream.of(blockPos);

		// Turn Feature Center into Feature Offset
		featurePos.set(Math.abs(featurePos.getX() - blockPos.getX()), 0, Math.abs(featurePos.getZ() - blockPos.getZ()));
		int size = landmark.size * 16 + additionalClearance;

		return featurePos.getX() < size && featurePos.getZ() < size ? Stream.empty() : Stream.of(blockPos);
	}

	@Override
	public PlacementModifierType<?> type() {
		return TFFeatureModifiers.NO_STRUCTURE.get();
	}

	private static DataResult<AvoidLandmarkModifier> validate(AvoidLandmarkModifier config) {
		return config.occupiesSurface || config.occupiesUnderground ? DataResult.success(config) : DataResult.error("Feature Decorator cannot occupy neither surface nor underground");
	}
}
