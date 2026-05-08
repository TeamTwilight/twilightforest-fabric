package twilightforest.entity.passive.quest.ram;

import tamaized.beanification.Component;

/**
 * 1:1 port of upstream {@code twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext}.
 * Beanification-managed singleton holding the currently-loaded Questing Ram quest
 * context (datapack-driven via {@code QuestReloadListener}, falls back to
 * {@link QuestingRamContext#FALLBACK}).
 */
@Component
public class QuestingRamCurrentContext {

	private QuestingRamContext context = QuestingRamContext.FALLBACK;

	public void setContext(QuestingRamContext context) {
		this.context = context;
	}

	public QuestingRamContext getContext() {
		return context;
	}

}
