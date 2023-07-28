package twilightforest.capabilities;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import twilightforest.capabilities.fan.FeatherFanCapabilityHandler;
import twilightforest.capabilities.fan.FeatherFanFallCapability;
import twilightforest.capabilities.giant_pick.GiantPickMineCapability;
import twilightforest.capabilities.giant_pick.GiantPickMineCapabilityHandler;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;
import twilightforest.capabilities.teleporter_cache.TeleporterCacheCapability;
import twilightforest.capabilities.teleporter_cache.TeleporterCacheCapabilityHandler;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.capabilities.thrown.YetiThrowCapabilityHandler;

public class CapabilityList {

	public static final ComponentKey<TeleporterCacheCapability> TELEPORTER_CACHE = ComponentRegistry.getOrCreate(TeleporterCacheCapability.ID, TeleporterCacheCapability.class);
	public static final ComponentKey<IShieldCapability> SHIELDS = ComponentRegistry.getOrCreate(IShieldCapability.ID, IShieldCapability.class);
	public static final ComponentKey<FeatherFanFallCapability> FEATHER_FAN_FALLING = ComponentRegistry.getOrCreate(FeatherFanFallCapability.ID, FeatherFanFallCapability.class);
	public static final ComponentKey<YetiThrowCapability> YETI_THROWN = ComponentRegistry.getOrCreate(YetiThrowCapability.ID, YetiThrowCapability.class);
	public static final ComponentKey<GiantPickMineCapability> GIANT_PICK_MINE = ComponentRegistry.getOrCreate(GiantPickMineCapability.ID, GiantPickMineCapability.class);

	public static void attachLevelCapability(WorldComponentFactoryRegistry e) {
		e.register(TELEPORTER_CACHE, TeleporterCacheCapabilityHandler::new);
	}

	public static void attachEntityCapability(EntityComponentFactoryRegistry e) {
		e.registerFor(LivingEntity.class, SHIELDS, ShieldCapabilityHandler::new);
		e.registerFor(Player.class, FEATHER_FAN_FALLING, FeatherFanCapabilityHandler::new);
		e.registerFor(LivingEntity.class, YETI_THROWN, YetiThrowCapabilityHandler::new);
		e.registerFor(LivingEntity.class, GIANT_PICK_MINE, GiantPickMineCapabilityHandler::new);
	}
}
