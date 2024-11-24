package twilightforest.capabilities;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;

public class ModCapabilityEntry implements EntityComponentInitializer, WorldComponentInitializer {
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		CapabilityList.attachEntityCapability(registry);

	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		CapabilityList.attachLevelCapability(registry);
	}
}
