package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class FluidTagGenerator extends FluidTagsProvider {

	public static final TagKey<Fluid> FIRE_JET_FUEL = TagKey.create(Registries.FLUID, TwilightForestMod.prefix("fire_jet_fuel"));

	public FluidTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(FIRE_JET_FUEL).addOptional(FluidTags.LAVA.location());
	}

	@Override
	public String getName() {
		return "Twilight Forest Fluid Tags";
	}
}
