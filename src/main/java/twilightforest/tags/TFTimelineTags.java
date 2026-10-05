package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.timeline.Timeline;
import twilightforest.TFCommon;

public class TFTimelineTags {

	public static final TagKey<Timeline> IN_TWILIGHT = TagKey.create(Registries.TIMELINE, TFCommon.prefix("in_twilight"));
}