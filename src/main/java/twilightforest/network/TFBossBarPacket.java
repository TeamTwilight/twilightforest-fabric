package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.world.BossEvent;
import twilightforest.TFCommon;
import twilightforest.entity.boss.bar.ServerTFBossBar;

import java.util.UUID;

public abstract class TFBossBarPacket implements CustomPacketPayload {
	public final UUID id;
	public boolean createWorldFog;
	public boolean playMusic;

	protected TFBossBarPacket(ServerTFBossBar bossEvent) {
		this.id = bossEvent.getId();
	}

	public TFBossBarPacket(RegistryFriendlyByteBuf buf) {
		this.id = buf.readUUID();
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeUUID(this.id);
	}

	public static class AddTFBossBarPacket extends TFBossBarPacket {
		public final Component name;
		public final float progress;
		public final int color;
		public final BossEvent.BossBarOverlay overlay;
		public final boolean darkenScreen;
		public final boolean playMusic;
		public final boolean createWorldFog;

		public static final Type<AddTFBossBarPacket> TYPE = new Type<>(TFCommon.prefix("add_tf_boss_bar"));
		public static final StreamCodec<RegistryFriendlyByteBuf, AddTFBossBarPacket> STREAM_CODEC = CustomPacketPayload.codec(AddTFBossBarPacket::write, AddTFBossBarPacket::new);

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
	}

	public static class UpdateTFBossBarStylePacket extends TFBossBarPacket {
		public final int color;
		public final BossEvent.BossBarOverlay overlay;
		public final boolean allowLerp;

		public static final Type<UpdateTFBossBarStylePacket> TYPE = new Type<>(TFCommon.prefix("update_tf_boss_bar_style"));
		public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTFBossBarStylePacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateTFBossBarStylePacket::write, UpdateTFBossBarStylePacket::new);

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
	}
}