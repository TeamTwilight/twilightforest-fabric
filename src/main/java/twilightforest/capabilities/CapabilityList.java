package twilightforest.capabilities;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import twilightforest.TwilightForestMod;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CapabilityList implements EntityComponentInitializer {

	public static final ComponentKey<IShieldCapability> SHIELD_CAPABILITY_COMPONENT_KEY = ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(TwilightForestMod.ID,"shield"), IShieldCapability.class);

	//@CapabilityInject(IShieldCapability.class)
	public static final IShieldCapability SHIELDS;

	static {
		SHIELDS = null;
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, SHIELD_CAPABILITY_COMPONENT_KEY, ShieldCapabilityHandler::new);
	}
}
