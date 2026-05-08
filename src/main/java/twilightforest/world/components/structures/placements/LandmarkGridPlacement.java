package twilightforest.world.components.structures.placements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.init.TFStructurePlacementTypes;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.Optional;

public class LandmarkGridPlacement extends StructurePlacement {
    public static final MapCodec<LandmarkGridPlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure_grid_lock").forGetter(placement -> placement.landmark)
    ).apply(instance, LandmarkGridPlacement::new));

    private final Optional<ResourceKey<Structure>> landmark;

    public static LandmarkGridPlacement forceStructureForCenters() {
        return new LandmarkGridPlacement(Optional.empty());
    }

    public LandmarkGridPlacement(Optional<ResourceKey<Structure>> landmark) {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1.0F, 0, Optional.empty());
        this.landmark = landmark;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        if (!LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkX, chunkZ)) {
            return false;
        }
        return this.landmark.isEmpty() || LegacyLandmarkPlacements.pickVarietyLandmark(chunkX, chunkZ) == this.landmark.get();
    }

    @Override
    public StructurePlacementType<?> type() {
        return TFStructurePlacementTypes.GRID_LANDMARK_PLACEMENT_TYPE.get();
    }
}
