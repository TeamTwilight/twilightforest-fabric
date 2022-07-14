package twilightforest.capabilities;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.capabilities.fan.FeatherFanFallCapability;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;

public class CapabilityList implements EntityComponentInitializer {
	public static final ComponentKey<IShieldCapability> SHIELDS = ComponentRegistry.getOrCreate(IShieldCapability.ID, IShieldCapability.class);
	public static final ComponentKey<FeatherFanFallCapability> FEATHER_FAN_FALLING = ComponentRegistry.getOrCreate(FeatherFanFallCapability.ID, FeatherFanFallCapability.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, SHIELDS, ShieldCapabilityHandler::new);
	}
}
