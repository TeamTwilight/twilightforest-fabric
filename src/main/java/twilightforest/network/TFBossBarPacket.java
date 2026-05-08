package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.world.BossEvent;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.bar.ServerTFBossBar;

import java.util.UUID;

/**
 * 1:1 port of upstream {@code twilightforest.network.TFBossBarPacket} — abstract
 * common base for the two custom Twilight Forest boss-bar payloads:
 * {@link AddTFBossBarPacket} (initial bar registration) and
 * {@link UpdateTFBossBarStylePacket} (color/overlay/lerp toggle).
 *
 * <p>Codex Fabric port note: NF {@code handle(packet, IPayloadContext)} static
 * receivers are dropped — codex's networking convention registers handlers in
 * {@code CodexNetworking} via {@code ClientPlayNetworking.registerGlobalReceiver}
 * instead of attaching them to the payload class itself.</p>
 */
public abstract class TFBossBarPacket implements CustomPacketPayload {
	protected final UUID id;

	protected TFBossBarPacket(ServerTFBossBar bossEvent) {
		this.id = bossEvent.getId();
	}

	public TFBossBarPacket(RegistryFriendlyByteBuf buf) {
		this.id = buf.readUUID();
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeUUID(this.id);
	}

	public UUID id() {
		return this.id;
	}

	public static class AddTFBossBarPacket extends TFBossBarPacket {
		private final Component name;
		private final float progress;
		private final int color;
		private final BossEvent.BossBarOverlay overlay;
		private final boolean darkenScreen;
		private final boolean playMusic;
		private final boolean createWorldFog;

		public static final Type<AddTFBossBarPacket> TYPE = new Type<>(TwilightForestMod.prefix("add_tf_boss_bar"));
		// Codex Fabric: method-ref to {@code AddTFBossBarPacket::write} is ambiguous because
		// the inner class also inherits {@code write} from the abstract base — disambiguate
		// with an explicit lambda.
		public static final StreamCodec<RegistryFriendlyByteBuf, AddTFBossBarPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), AddTFBossBarPacket::new);

		public AddTFBossBarPacket(ServerTFBossBar bossEvent) {
			super(bossEvent);
			this.name = bossEvent.getName();
			this.progress = bossEvent.getProgress();
			this.color = bossEvent.getBarColor();
			this.overlay = bossEvent.getOverlay();
			this.darkenScreen = bossEvent.shouldDarkenScreen();
			this.playMusic = bossEvent.shouldPlayBossMusic();
			this.createWorldFog = bossEvent.shouldCreateWorldFog();
		}

		public AddTFBossBarPacket(RegistryFriendlyByteBuf buf) {
			super(buf);
			this.name = ComponentSerialization.STREAM_CODEC.decode(buf);
			this.progress = buf.readFloat();
			this.color = buf.readInt();
			this.overlay = buf.readEnum(BossEvent.BossBarOverlay.class);
			int i = buf.readUnsignedByte();
			this.darkenScreen = (i & 1) > 0;
			this.playMusic = (i & 2) > 0;
			this.createWorldFog = (i & 4) > 0;
		}

		@Override
		public void write(RegistryFriendlyByteBuf buf) {
			super.write(buf);
			ComponentSerialization.STREAM_CODEC.encode(buf, this.name);
			buf.writeFloat(this.progress);
			buf.writeInt(this.color);
			buf.writeEnum(this.overlay);
			buf.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public Component name() { return this.name; }
		public float progress() { return this.progress; }
		public int color() { return this.color; }
		public BossEvent.BossBarOverlay overlay() { return this.overlay; }
		public boolean darkenScreen() { return this.darkenScreen; }
		public boolean playMusic() { return this.playMusic; }
		public boolean createWorldFog() { return this.createWorldFog; }
	}

	public static class UpdateTFBossBarStylePacket extends TFBossBarPacket {
		private final int color;
		private final BossEvent.BossBarOverlay overlay;
		private final boolean allowLerp;

		public static final Type<UpdateTFBossBarStylePacket> TYPE = new Type<>(TwilightForestMod.prefix("update_tf_boss_bar_style"));
		public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTFBossBarStylePacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), UpdateTFBossBarStylePacket::new);

		public UpdateTFBossBarStylePacket(ServerTFBossBar bossEvent, boolean allowLerp) {
			super(bossEvent);
			this.color = bossEvent.getBarColor();
			this.overlay = bossEvent.getOverlay();
			this.allowLerp = allowLerp;
		}

		public UpdateTFBossBarStylePacket(RegistryFriendlyByteBuf buf) {
			super(buf);
			this.color = buf.readInt();
			this.overlay = buf.readEnum(BossEvent.BossBarOverlay.class);
			this.allowLerp = buf.readBoolean();
		}

		@Override
		public void write(RegistryFriendlyByteBuf buf) {
			super.write(buf);
			buf.writeInt(this.color);
			buf.writeEnum(this.overlay);
			buf.writeBoolean(this.allowLerp);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		public int color() { return this.color; }
		public BossEvent.BossBarOverlay overlay() { return this.overlay; }
		public boolean allowLerp() { return this.allowLerp; }
	}
}
