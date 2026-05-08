package twilightforest.data.custom.stalactites.entry;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StalactiteReloadListener {
    public static final String STALACTITE_DIRECTORY = "twilight/stalactites";
    public static final Map<String, SpeleothemVarietyConfig> HILL_CONFIGS = new HashMap<>();
    public static final Map<String, List<Stalactite>> STALACTITES_PER_HILL = new HashMap<>();
    public static final Map<String, List<Stalactite>> ORE_STALACTITES_PER_HILL = new HashMap<>();
    public static final Map<String, List<Stalactite>> STALAGMITES_PER_HILL = new HashMap<>();

    static {
        register("small_hollow_hill", 0.55F, 0.35F);
        register("medium_hollow_hill", 0.65F, 0.45F);
        register("large_hollow_hill", 0.75F, 0.55F);
        register("hydra_lair", 0.55F, 0.35F);
        register("yeti_cave", 0.45F, 0.55F);
        register("troll_cave", 0.65F, 0.55F);
    }

    private StalactiteReloadListener() {
    }

    private static void register(String type, float stalactiteChance, float stalagmiteChance) {
        Stalactite stone = new Stalactite(Either.right(Blocks.STONE), 0.25F, 11, 1);
        HILL_CONFIGS.put(type, new SpeleothemVarietyConfig(type, List.of(), List.of(), List.of(), 0.0F, stalactiteChance, stalagmiteChance, false));
        STALACTITES_PER_HILL.put(type, List.of(stone));
        ORE_STALACTITES_PER_HILL.put(type, List.of());
        STALAGMITES_PER_HILL.put(type, List.of(stone));
    }
}
