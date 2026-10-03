package twilightforest.entity.passive.quest;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TFCommon;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.world.components.structures.util.CodecResourceReloadListener;

public class QuestReloadListener extends CodecResourceReloadListener<QuestingRamContext> {
	private static final QuestingRamCurrentContext questingRamCurrentContext = QuestingRamCurrentContext.INSTANCE;
	private boolean found;

	public QuestReloadListener() {
		super("twilight/quests", QuestingRamContext.CODEC);
	}

	@Override
	protected void forLocation(ResourceManager manager, Identifier location, QuestingRamContext context) {
		if (location.getPath().equals("questing_ram")) {
			questingRamCurrentContext.setContext(context);
			TFCommon.LOGGER.debug("Questing Ram quest set by mod {}", location.getNamespace());
			found = true;
		}
	}

	@Override
	protected void afterApply(ResourceManager manager, ProfilerFiller profiler) {
		if (!found) {
			TFCommon.LOGGER.error("Questing Ram quest file not found. Defaulting to fallback");
			questingRamCurrentContext.setContext(QuestingRamContext.FALLBACK);
		}

		found = false;
	}
}