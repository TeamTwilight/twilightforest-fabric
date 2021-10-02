package twilightforest.data;

import net.fabricmc.fabric.api.tag.TagRegistry;
import twilightforest.TFConstants;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;
import twilightforest.TwilightForestMod;

public class FluidTagGenerator extends FluidTagsProvider {

    public static final Tag.Named<Fluid> FIRE_JET_FUEL = (Tag.Named<Fluid>) TagRegistry.fluid(TFConstants.prefix("fire_jet_fuel"));

    public FluidTagGenerator(DataGenerator generatorIn) {
        super(generatorIn/*, TwilightForestMod.ID, existingFileHelper*/);
    }

    @Override
    protected void addTags() {
        //load the tag for cursed reasons
        tag(FluidTags.LAVA);
        tag(FluidTags.WATER);
        tag(FIRE_JET_FUEL).addTag(FluidTags.LAVA);
    }
}
