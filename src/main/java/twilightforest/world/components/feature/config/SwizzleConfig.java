package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.jetbrains.annotations.NotNull;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.WoodPalette;
import twilightforest.world.components.processors.WoodPaletteSwizzle;

public record SwizzleConfig(HolderSet<WoodPalette> targets, WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> paletteChoices) implements FeatureConfiguration {
    public static final Codec<SwizzleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(WoodPalettes.WOOD_PALETTE_TYPE_KEY).fieldOf("target_palettes").forGetter(SwizzleConfig::targets),
            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(RegistryCodecs.homogeneousList(WoodPalettes.WOOD_PALETTE_TYPE_KEY))).fieldOf("palette_choices").forGetter(SwizzleConfig::paletteChoices)
    ).apply(instance, SwizzleConfig::new));

    @NotNull
    public static WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> buildPaletteChoices(HolderGetter<WoodPalette> paletteHolders) {
        // Old code with chances:
        //  getRandomWeighted(RandomSource random) {
        //	  int randomVal = random.nextInt();
        //	  if ((randomVal & 0b1) == 0) return ArrayUtil.wrapped(COMMON, randomVal >> 1); // 50% chance
        //	  if ((randomVal & 0b10) == 0) return ArrayUtil.wrapped(UNCOMMON, randomVal >> 2); // 25% chance
        //	  if ((randomVal & 0b1100) != 0) return ArrayUtil.wrapped(RARE, randomVal >> 4); // 18.75% chance
        //	  return ArrayUtil.wrapped(TREASURE, randomVal >> 4); // 6.25% chance
        //  }

        WeightedEntry.Wrapper<HolderSet<WoodPalette>> common = // 50% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.COMMON_PALETTES).get(), 8);
        WeightedEntry.Wrapper<HolderSet<WoodPalette>> uncommon = // 25% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.UNCOMMON_PALETTES).get(), 4);
        WeightedEntry.Wrapper<HolderSet<WoodPalette>> rare = // 18.75% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.RARE_PALETTES).get(), 3);
        WeightedEntry.Wrapper<HolderSet<WoodPalette>> treasure = // 6.25% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.TREASURE_PALETTES).get(), 1);

        return WeightedRandomList.create(common, uncommon, rare, treasure);
    }

    public static SwizzleConfig generateForWell(HolderGetter<WoodPalette> paletteHolders, WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> paletteChoices) {
        return new SwizzleConfig(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.WELL_SWIZZLE_MASK).get(), paletteChoices);
    }

    public static SwizzleConfig generateForHut(HolderGetter<WoodPalette> paletteHolders, WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> paletteChoices) {
        return new SwizzleConfig(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.DRUID_HUT_SWIZZLE_MASK).get(), paletteChoices);
    }

    public void buildAddProcessors(StructurePlaceSettings settings, RandomSource random) {
        for (Holder<WoodPalette> targetPalette : this.targets) {
            settings.addProcessor(new WoodPaletteSwizzle(targetPalette, this.paletteChoices().getRandom(random).get().getData().getRandomElement(random).get()));
        }
    }
}
