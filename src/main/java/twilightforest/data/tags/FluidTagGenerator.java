package twilightforest.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import twilightforest.TwilightForestMod;

public class FluidTagGenerator extends FluidTagsProvider {

    public static final TagKey<Fluid> FIRE_JET_FUEL = TagKey.create(Registry.FLUID_REGISTRY, TwilightForestMod.prefix("fire_jet_fuel"));

    public FluidTagGenerator(FabricDataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void addTags() {
        tag(FIRE_JET_FUEL).addTag(FluidTags.LAVA);
    }
}
