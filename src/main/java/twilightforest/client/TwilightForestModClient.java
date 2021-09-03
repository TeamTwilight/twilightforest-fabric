package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import twilightforest.client.particle.TFParticleType;

public class TwilightForestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TFParticleType.registerFactories();
        twilightforest.client.TFClientSetup.addLegacyPack();
    }
}
