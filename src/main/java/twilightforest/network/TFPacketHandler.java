package twilightforest.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import twilightforest.TwilightForestMod;

public class TFPacketHandler {
	// Bump this number every time theres a breaking change, to ensure people dont mess things up when joining on the wrong version
	private static final String PROTOCOL_VERSION = "2";
	public static final SimpleChannel CHANNEL = new SimpleChannel(TwilightForestMod.prefix("channel"));

	@SuppressWarnings("UnusedAssignment")
	public static void init() {
		int id = 0;
		CHANNEL.registerS2CPacket(AreaProtectionPacket.class, id++);
		CHANNEL.registerS2CPacket(ChangeBiomePacket.class, id++);
		CHANNEL.registerS2CPacket(EnforceProgressionStatusPacket.class, id++);
		CHANNEL.registerS2CPacket(StructureProtectionPacket.class, id++);
		CHANNEL.messageBuilder(StructureProtectionClearPacket.class, id++).encoder(StructureProtectionClearPacket::encode).decoder(StructureProtectionClearPacket::new).consumer(StructureProtectionClearPacket.Handler::onMessage).add();
		CHANNEL.registerS2CPacket(ThrowPlayerPacket.class, id++);
		CHANNEL.registerS2CPacket(MagicMapPacket.class, id++);
		CHANNEL.registerS2CPacket(MazeMapPacket.class, id++);
		CHANNEL.registerS2CPacket(UpdateShieldPacket.class, id++);
		CHANNEL.registerC2SPacket(UncraftingGuiPacket.class, id++);
		CHANNEL.registerS2CPacket(UpdateTFMultipartPacket.class, id++);
		CHANNEL.registerS2CPacket(SpawnFallenLeafFromPacket.class, id++);
		CHANNEL.registerS2CPacket(MissingAdvancementToastPacket.class, id++);
	}
}
