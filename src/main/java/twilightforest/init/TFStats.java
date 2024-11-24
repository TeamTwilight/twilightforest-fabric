package twilightforest.init;


import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

public class TFStats {

	public static final LazyRegistrar<ResourceLocation> STATS = LazyRegistrar.create(Registries.CUSTOM_STAT, TwilightForestMod.ID);
	private static final List<Runnable> STAT_SETUP = new ArrayList<>();

	public static final RegistryObject<ResourceLocation> BUGS_SQUISHED = makeTFStat("bugs_squished");
	public static final RegistryObject<ResourceLocation> UNCRAFTING_TABLE_INTERACTIONS = makeTFStat("uncrafting_table_interactions");
	public static final RegistryObject<ResourceLocation> TROPHY_PEDESTALS_ACTIVATED = makeTFStat("trophy_pedestals_activated");
	public static final RegistryObject<ResourceLocation> E115_SLICES_EATEN = makeTFStat("e115_slices_eaten");
	public static final RegistryObject<ResourceLocation> TORCHBERRIES_HARVESTED = makeTFStat("torchberries_harvested");
	public static final RegistryObject<ResourceLocation> BLOCKS_CRUMBLED = makeTFStat("blocks_crumbled");
	public static final RegistryObject<ResourceLocation> LIFE_CHARMS_ACTIVATED = makeTFStat("life_charms_activated");
	public static final RegistryObject<ResourceLocation> KEEPING_CHARMS_ACTIVATED = makeTFStat("keeping_charms_activated");
	public static final RegistryObject<ResourceLocation> SKULL_CANDLES_MADE = makeTFStat("skull_candles_made");
	public static final RegistryObject<ResourceLocation> TF_SHIELDS_BROKEN = makeTFStat("tf_shields_broken");

	private static RegistryObject<ResourceLocation> makeTFStat(String key) {
		ResourceLocation resourcelocation = TwilightForestMod.prefix(key);
		STAT_SETUP.add(() -> Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT));
		return STATS.register(key, () -> resourcelocation);
	}

	public static void init() {
		STAT_SETUP.forEach(Runnable::run);
	}
}
