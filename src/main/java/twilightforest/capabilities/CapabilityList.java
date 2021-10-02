package twilightforest.capabilities;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.Entity;
import twilightforest.TFConstants;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CapabilityList implements EntityComponentInitializer {

	public static final ComponentKey<IShieldCapability> SHIELD_CAPABILITY_COMPONENT_KEY = ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(TFConstants.ID,"shield"), IShieldCapability.class);

	//@CapabilityInject(IShieldCapability.class)
	public static final IShieldCapability SHIELDS;

	static {
		SHIELDS = null;
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, SHIELD_CAPABILITY_COMPONENT_KEY, ShieldCapabilityHandler::new);
	}

	/*
	public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof LivingEntity) {
			e.addCapability(IShieldCapability.ID, new ICapabilitySerializable<CompoundTag>() {

				LazyOptional<IShieldCapability> inst = LazyOptional.of(() -> {
					ShieldCapabilityHandler i = new ShieldCapabilityHandler();
					i.setEntity((LivingEntity) e.getObject());
					return i;
				});

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
					return SHIELDS.orEmpty(capability, inst.cast());
				}

				@Override
				public CompoundTag serializeNBT() {
					return inst.orElseThrow(NullPointerException::new).serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt) {
					inst.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
				}
			});
		}
	}

	 */
}
