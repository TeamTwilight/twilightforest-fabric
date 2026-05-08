package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import twilightforest.TwilightForestMod;

/**
 * Fabric port of upstream {@code TFStats} — registers TF custom stat ids into
 * {@link BuiltInRegistries#CUSTOM_STAT} and primes {@link Stats#CUSTOM} so
 * advancement JSONs (e.g. {@code experiment_115_115}) resolve their
 * {@code minecraft:custom_stat} predicates without «Unknown registry key».
 */
public final class TFStats {

    public static final ResourceLocation BUGS_SQUISHED = makeTFStat("bugs_squished");
    public static final ResourceLocation UNCRAFTING_TABLE_INTERACTIONS = makeTFStat("uncrafting_table_interactions");
    public static final ResourceLocation TROPHY_PEDESTALS_ACTIVATED = makeTFStat("trophy_pedestals_activated");
    public static final ResourceLocation E115_SLICES_EATEN = makeTFStat("e115_slices_eaten");
    public static final ResourceLocation TORCHBERRIES_HARVESTED = makeTFStat("torchberries_harvested");
    public static final ResourceLocation BLOCKS_CRUMBLED = makeTFStat("blocks_crumbled");
    public static final ResourceLocation LIFE_CHARMS_ACTIVATED = makeTFStat("life_charms_activated");
    public static final ResourceLocation KEEPING_CHARMS_ACTIVATED = makeTFStat("keeping_charms_activated");
    public static final ResourceLocation SKULL_CANDLES_MADE = makeTFStat("skull_candles_made");
    public static final ResourceLocation TF_SHIELDS_BROKEN = makeTFStat("tf_shields_broken");

    private TFStats() {
    }

    public static void bootstrap() {
        // Force class init: any field touch initializes them all.
        BUGS_SQUISHED.toString();
    }

    private static ResourceLocation makeTFStat(String key) {
        ResourceLocation loc = TwilightForestMod.prefix(key);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, loc, loc);
        Stats.CUSTOM.get(loc, StatFormatter.DEFAULT);
        return loc;
    }
}
