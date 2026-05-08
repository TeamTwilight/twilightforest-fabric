package twilightforest.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.network.SpawnCharmPacket} — server
 * pushes a charm-of-keeping consumption animation: spawns the {@link
 * twilightforest.entity.CharmEffect} orbiter on the client and plays the
 * referenced sound at camera position.
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} dropped — the dual-mode
 * behavior (Totem-style item-activation vs CharmEffect orbiter, gated by
 * {@code TFConfig.spawnCharmAnimationAsTotem}) is wired in {@code CodexNetworking}
 * client side.</p>
 */
public record SpawnCharmPacket(ItemStack charm, ResourceKey<SoundEvent> event) implements CustomPacketPayload {

	public static final Type<SpawnCharmPacket> TYPE = new Type<>(TwilightForestMod.prefix("spawn_charm"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnCharmPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), SpawnCharmPacket::new);

	public SpawnCharmPacket(RegistryFriendlyByteBuf buf) {
		this(ItemStack.STREAM_CODEC.decode(buf), buf.readResourceKey(Registries.SOUND_EVENT));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		ItemStack.STREAM_CODEC.encode(buf, this.charm());
		buf.writeResourceKey(this.event());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
