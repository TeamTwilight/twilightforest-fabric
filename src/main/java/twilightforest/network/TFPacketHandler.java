package twilightforest.network;

import net.fabricmc.api.EnvType;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public class TFPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final NetworkWrapper CHANNEL = new NetworkWrapper(TwilightForestMod.prefix("channel"));

	@SuppressWarnings("UnusedAssignment")
	public static void init() {
		int id = 0;
		CHANNEL.messageBuilder(AreaProtectionPacket.class, id++).encoder(AreaProtectionPacket::encode).decoder(AreaProtectionPacket::new).consumer(new SimpleChannel.MessageBuilder.ToBooleanBiFunction<AreaProtectionPacket, Supplier<NetworkEvent.Context>>() {
			@Override
			public boolean applyAsBool(AreaProtectionPacket message, Supplier<NetworkEvent.Context> ctx) {
				return AreaProtectionPacket.Handler.onMessage(message, ctx);
			}
		}).add();
		CHANNEL.registerPacket(ChangeBiomePacket.class, ChangeBiomePacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(EnforceProgressionStatusPacket.class, EnforceProgressionStatusPacket::new, EnvType.CLIENT);
		CHANNEL.registerPacket(StructureProtectionPacket.class, StructureProtectionPacket::new, EnvType.CLIENT);
		CHANNEL.messageBuilder(StructureProtectionPacket.class, id++).encoder(StructureProtectionPacket::encode).decoder(StructureProtectionPacket::new).consumer(StructureProtectionPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(StructureProtectionClearPacket.class, id++).encoder(StructureProtectionClearPacket::encode).decoder(StructureProtectionClearPacket::new).consumer(StructureProtectionClearPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(ThrowPlayerPacket.class, id++).encoder(ThrowPlayerPacket::encode).decoder(ThrowPlayerPacket::new).consumer(ThrowPlayerPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(MagicMapPacket.class, id++).encoder(MagicMapPacket::encode).decoder(MagicMapPacket::new).consumer(MagicMapPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(MazeMapPacket.class, id++).encoder(MazeMapPacket::encode).decoder(MazeMapPacket::new).consumer(MazeMapPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(UpdateShieldPacket.class, id++).encoder(UpdateShieldPacket::encode).decoder(UpdateShieldPacket::new).consumer(UpdateShieldPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(SetSkylightEnabledPacket.class, id++).encoder(SetSkylightEnabledPacket::encode).decoder(SetSkylightEnabledPacket::new).consumer(SetSkylightEnabledPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(UncraftingGuiPacket.class, id++).encoder(UncraftingGuiPacket::encode).decoder(UncraftingGuiPacket::new).consumer(UncraftingGuiPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(UpdateTFMultipartPacket.class, id++).encoder(UpdateTFMultipartPacket::encode).decoder(UpdateTFMultipartPacket::new).consumer(UpdateTFMultipartPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(SpawnFallenLeafFromPacket.class, id++).encoder(SpawnFallenLeafFromPacket::encode).decoder(SpawnFallenLeafFromPacket::new).consumer(SpawnFallenLeafFromPacket.Handler::onMessage).add();
	}
}
