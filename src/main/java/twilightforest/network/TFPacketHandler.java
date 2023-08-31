package twilightforest.network;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import twilightforest.TwilightForestMod;

public class TFPacketHandler {
	// Bump this number every time theres a breaking change, to ensure people dont mess things up when joining on the wrong version
	private static final String PROTOCOL_VERSION = "2";
	public static final SimpleChannel CHANNEL = new SimpleChannel(TwilightForestMod.prefix("channel"));

	@SuppressWarnings({"UnusedAssignment"})
	public static void init() {
		CHANNEL.initServerListener();
		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> CHANNEL::initClientListener);
		int id = 0;

		CHANNEL.registerS2CPacket(AreaProtectionPacket.class, id++);
		CHANNEL.registerS2CPacket(CreateMovingCicadaSoundPacket.class, id++);
		CHANNEL.registerS2CPacket(EnforceProgressionStatusPacket.class, id++);
		CHANNEL.registerS2CPacket(MagicMapPacket.class, id++);
		CHANNEL.registerS2CPacket(MazeMapPacket.class, id++);
		CHANNEL.registerS2CPacket(MissingAdvancementToastPacket.class, id++);
		CHANNEL.registerS2CPacket(ParticlePacket.class, id++);
		CHANNEL.registerS2CPacket(SpawnFallenLeafFromPacket.class, id++);
		CHANNEL.registerS2CPacket(StructureProtectionClearPacket.class, id++);
		CHANNEL.registerS2CPacket(StructureProtectionPacket.class, id++);
		CHANNEL.registerS2CPacket(SyncUncraftingTableConfigPacket.class, id++);
		CHANNEL.registerS2CPacket(ThrowPlayerPacket.class, id++);
		CHANNEL.registerC2SPacket(UncraftingGuiPacket.class, id++);
		CHANNEL.registerS2CPacket(UpdateFeatherFanFallPacket.class, id++);
		CHANNEL.registerC2SPacket(UpdateGamerulePacket.class, id++);
		CHANNEL.registerS2CPacket(UpdateShieldPacket.class, id++);
		CHANNEL.registerS2CPacket(UpdateTFMultipartPacket.class, id++);
		CHANNEL.registerS2CPacket(UpdateThrownPacket.class, id++);
	}
}
