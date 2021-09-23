package twilightforest.network;

import net.fabricmc.api.EnvType;
import twilightforest.TFConstants;

public class TFPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final NetworkWrapper CHANNEL = new NetworkWrapper(TFConstants.prefix("channel"));

	@SuppressWarnings("UnusedAssignment")
	public static void init() {
		int id = 0;
		CHANNEL.registerPacket(AreaProtectionPacket.class, AreaProtectionPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(ChangeBiomePacket.class, ChangeBiomePacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(EnforceProgressionStatusPacket.class, EnforceProgressionStatusPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(StructureProtectionPacket.class, StructureProtectionPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(StructureProtectionClearPacket.class, StructureProtectionClearPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(ThrowPlayerPacket.class, ThrowPlayerPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(MagicMapPacket.class, MagicMapPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(MazeMapPacket.class, MazeMapPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(UpdateShieldPacket.class, UpdateShieldPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(UncraftingGuiPacket.class, UncraftingGuiPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(UpdateTFMultipartPacket.class, UpdateTFMultipartPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(SpawnFallenLeafFromPacket.class, SpawnFallenLeafFromPacket::new, EnvType.CLIENT);
	}
}
