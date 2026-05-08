package twilightforest.entity.boss.bar;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import twilightforest.network.TFBossBarPacket;

/**
 * 1:1 port of upstream {@code twilightforest.entity.boss.bar.ServerTFBossBar} —
 * server-side {@link ServerBossEvent} extension that carries an arbitrary
 * 24-bit RGB color (vs vanilla's fixed 8-color enum) and pushes custom add /
 * style-update payloads to the client.
 *
 * <p>Codex Fabric port note: upstream's
 * {@code player.connection.send(new TFBossBarPacket.AddTFBossBarPacket(this))} is
 * replaced with Fabric's
 * {@link ServerPlayNetworking#send(ServerPlayer, net.minecraft.network.protocol.common.custom.CustomPacketPayload)}
 * helper — equivalent dispatch with auto-wrapping into a
 * {@code ClientboundCustomPayloadPacket}. Same for the style-update branch.</p>
 */
public class ServerTFBossBar extends ServerBossEvent {
	private int color;

	public ServerTFBossBar(Component name, int color, BossBarOverlay overlay) {
		super(name, BossBarColor.WHITE, overlay);
		this.color = color;
	}

	public int getBarColor() {
		return this.color;
	}

	@Override
	public void addPlayer(ServerPlayer player) {
		if (this.players.add(player) && this.isVisible()) {
			ServerPlayNetworking.send(player, new TFBossBarPacket.AddTFBossBarPacket(this));
		}
	}

	public void updateStyle(int color, BossBarOverlay overlay, boolean allowLerp) {
		boolean change = false;
		if (this.color != color) {
			this.color = color;
			change = true;
		}
		if (this.overlay != overlay) {
			this.overlay = overlay;
			change = true;
		}
		if (change) this.players.forEach(serverPlayer -> ServerPlayNetworking.send(serverPlayer, new TFBossBarPacket.UpdateTFBossBarStylePacket(this, allowLerp)));
	}
}
