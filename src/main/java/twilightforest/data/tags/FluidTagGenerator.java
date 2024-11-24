package twilightforest.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class FluidTagGenerator extends FabricTagProvider.FluidTagProvider {

	public static final TagKey<Fluid> FIRE_JET_FUEL = TagKey.create(Registries.FLUID, TwilightForestMod.prefix("fire_jet_fuel"));

	public FluidTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		getOrCreateTagBuilder(FIRE_JET_FUEL).forceAddTag(FluidTags.LAVA);
	}

	@Override
	public String getName() {
		return "Twilight Forest Fluid Tags";
	}
}
