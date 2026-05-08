package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.util.iterators.RectangleLatticeIterator;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

public final class StructureSpeleothemConfigs {
    public static final RegistryFileCodec<StructureSpeleothemConfig> CODEC = RegistryFileCodec.create(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC, false);

    public static final ResourceKey<StructureSpeleothemConfig> SMALL_HILL = key("small_hollow_hill");
    public static final ResourceKey<StructureSpeleothemConfig> MEDIUM_HILL = key("medium_hollow_hill");
    public static final ResourceKey<StructureSpeleothemConfig> LARGE_HILL = key("large_hollow_hill");
    public static final ResourceKey<StructureSpeleothemConfig> HYDRA_LAIR = key("hydra_lair");
    public static final ResourceKey<StructureSpeleothemConfig> YETI_CAVE = key("yeti_cave");
    public static final ResourceKey<StructureSpeleothemConfig> TROLL_CAVE = key("troll_cave");

    private StructureSpeleothemConfigs() {
    }

    private static ResourceKey<StructureSpeleothemConfig> key(String path) {
        return ResourceKey.create(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, TwilightForestMod.prefix(path));
    }

    public static void bootstrap(BootstrapContext<StructureSpeleothemConfig> context) {
        bootstrapRegister(context, SMALL_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, MEDIUM_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, LARGE_HILL, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, HYDRA_LAIR, new RectangleLatticeIterator.TriangularLatticeConfig(4.5F));
        bootstrapRegister(context, YETI_CAVE, RectangleLatticeIterator.TriangularLatticeConfig.DEFAULT);
        bootstrapRegister(context, TROLL_CAVE, new RectangleLatticeIterator.TriangularLatticeConfig(4.5F));
    }

    private static void bootstrapRegister(BootstrapContext<StructureSpeleothemConfig> context, ResourceKey<StructureSpeleothemConfig> configKey, RectangleLatticeIterator.TriangularLatticeConfig latticeConfig) {
        context.register(configKey, StructureSpeleothemConfig.fromLocation(latticeConfig, configKey.location().getPath()));
    }

    public static Holder.Reference<StructureSpeleothemConfig> getConfigHolder(HolderLookup.Provider registryAccess, String id) {
        return getConfigHolder(registryAccess, key(ResourceLocation.parse(id).getPath()));
    }

    public static Holder.Reference<StructureSpeleothemConfig> getConfigHolder(HolderLookup.Provider registryAccess, ResourceKey<StructureSpeleothemConfig> key) {
        return registryAccess.lookupOrThrow(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(key);
    }
}
