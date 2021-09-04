package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.TFEntities;

public class TwilightForestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client -> {


        }));
    }
}
