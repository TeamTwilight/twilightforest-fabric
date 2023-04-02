package twilightforest.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbePlugin;

public class TopCompat implements ITheOneProbePlugin {
    @Override
    public void onLoad(ITheOneProbe api) {
        api.registerElementFactory(QuestRamWoolElement.Factory.INSTANCE);
        api.registerEntityProvider(TOPQuestRamWoolProvider.INSTANCE);
    }
}
