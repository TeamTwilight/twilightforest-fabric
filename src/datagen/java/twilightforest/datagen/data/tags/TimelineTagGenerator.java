package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.timeline.Timeline;
import twilightforest.datagen.data.TFTimelineGenerator;
import twilightforest.tags.TFTimelineTags;

import java.util.concurrent.CompletableFuture;

public class TimelineTagGenerator extends KeyTagProvider<Timeline> {
	public TimelineTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, Registries.TIMELINE, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		this.tag(TFTimelineTags.IN_TWILIGHT).add(TFTimelineGenerator.TWILIGHT);
	}
}