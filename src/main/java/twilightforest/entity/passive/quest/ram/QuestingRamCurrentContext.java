package twilightforest.entity.passive.quest.ram;

import twilightforest.util.TFBeanRegistry;

public class QuestingRamCurrentContext {

	public static final QuestingRamCurrentContext INSTANCE = new QuestingRamCurrentContext();

	static {
		TFBeanRegistry.register(QuestingRamCurrentContext.class, INSTANCE);
	}

	private QuestingRamContext context = QuestingRamContext.FALLBACK;

	public void setContext(QuestingRamContext context) {
		this.context = context;
	}

	public QuestingRamContext getContext() {
		return context;
	}

}
