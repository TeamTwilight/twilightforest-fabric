package twilightforest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.client.event.ClientGameEvents;
import twilightforest.client.event.CloudEvents;
import twilightforest.client.event.LockedBiomeToastHandler;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.item.mapdata.MapDataManager;
import twilightforest.network.*;

public final class TFClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerPackets();

		CloudEvents.init();
		LockedBiomeToastHandler.init();
		TFSkyRenderer.init();
		MapDataManager.init();
		ClientGameEvents.init();
	}

	private static void registerPackets() {
		ClientPlayNetworking.registerGlobalReceiver(AreaProtectionPacket.TYPE, AreaProtectionPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MagicMapPacket.TYPE, MagicMapPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MazeMapPacket.TYPE, MazeMapPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(MovePlayerPacket.TYPE, MovePlayerPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(ParticlePacket.TYPE, ParticlePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, GogglesZoomPacket::handleClient);
		ClientPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, GradualGlidePacket::handleClient);
		ClientPlayNetworking.registerGlobalReceiver(SpawnCharmPacket.TYPE, SpawnCharmPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(StructureProtectionPacket.TYPE, StructureProtectionPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateThrownPacket.TYPE, UpdateThrownPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(LifedrainParticlePacket.TYPE, LifedrainParticlePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncQuestsPacket.TYPE, SyncQuestsPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket::handle);
	}
}