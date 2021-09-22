package twilightforest.util;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.StructureFeatureManager;

/**
 * Class to help manage entity spawns inside of structures
 */
public class StructureSpawnManager
{
    private static Map<StructureFeature<?>, StructureSpawnInfo> structuresWithSpawns = Collections.emptyMap();

    /**
     * Looks up if a given position is within a structure and returns any entity spawns that structure has for the given classification, or null if
     * none are found.
     * @param structureManager Structure Manager, used to check if a position is within a structure.
     * @param classification   Entity classification
     * @param pos              Position to get entity spawns of
     */
    @Nullable
    public static WeightedRandomList<MobSpawnSettings.SpawnerData> getStructureSpawns(StructureFeatureManager structureManager, MobCategory classification, BlockPos pos)
    {
        for (Entry<StructureFeature<?>, StructureSpawnInfo> entry : structuresWithSpawns.entrySet())
        {
            StructureFeature<?> structure = entry.getKey();
            StructureSpawnInfo spawnInfo = entry.getValue();
            //Note: We check if the structure has spawns for a type first before looking at the world as it should be a cheaper check
            if (spawnInfo.spawns.containsKey(classification) && structureManager.getStructureAt(pos, spawnInfo.insideOnly, structure).isValid())
                return spawnInfo.spawns.get(classification);
        }
        return null;
    }

    /**
     * Gets the entity spawn lists for entities of a given classification for a given structure.
     * @param structure      The Structure
     * @param classification The classification to lookup
     */
    public static WeightedRandomList<MobSpawnSettings.SpawnerData> getSpawnList(StructureFeature<?> structure, MobCategory classification)
    {
        StructureSpawnInfo info = structuresWithSpawns.get(structure);
        if (info != null)
            return info.spawns.getOrDefault(classification, WeightedRandomList.create());
        return WeightedRandomList.create();
    }

    /**
     * Helper class to keep track of spawns and if the spawns should be restricted to inside the structure pieces.
     */
    private static class StructureSpawnInfo
    {
        private final Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawns;
        private final boolean insideOnly;

        private StructureSpawnInfo(ImmutableMap<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawns, boolean insideOnly)
        {
            this.spawns = spawns;
            this.insideOnly = insideOnly;
        }
    }
}