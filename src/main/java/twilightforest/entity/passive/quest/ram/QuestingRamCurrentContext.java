package twilightforest.entity.passive.quest.ram;

public class QuestingRamCurrentContext {
	public static final QuestingRamCurrentContext INSTANCE = new QuestingRamCurrentContext();

	private QuestingRamContext context = QuestingRamContext.FALLBACK;

	public void setContext(QuestingRamContext context) {
		this.context = context;
	}

	public QuestingRamContext getContext() {
		return context;
	}
}