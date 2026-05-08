package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

import java.util.List;

/**
 * 1:1 port of upstream {@code twilightforest.network.SyncUncraftingTableConfigPacket}
 * — server pushes the Uncrafting Table config (XP multipliers, blacklists, toggles)
 * so clients can render the GUI consistently.
 *
 * <p>Codex Fabric port note: NF static {@code handle(...)} dropped — client config
 * application lives in {@code CodexNetworking}, mutating {@code TFConfig} fields.</p>
 */
public record SyncUncraftingTableConfigPacket(
	double uncraftingMultiplier, double repairingMultiplier,
	boolean allowShapeless, boolean disableIngredientSwitching, boolean disabledUncrafting, boolean disabledTable,
	List<? extends String> disabledRecipes, boolean flipRecipeList,
	List<? extends String> disabledModids, boolean flipModidList) implements CustomPacketPayload {

	public static final Type<SyncUncraftingTableConfigPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_uncrafting_config"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncUncraftingTableConfigPacket> STREAM_CODEC = CustomPacketPayload.codec((p, buf) -> p.write(buf), SyncUncraftingTableConfigPacket::new);

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(),
			buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
			buf.readList(FriendlyByteBuf::readUtf), buf.readBoolean(),
			buf.readList(FriendlyByteBuf::readUtf), buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier());
		buf.writeDouble(this.repairingMultiplier());
		buf.writeBoolean(this.allowShapeless());
		buf.writeBoolean(this.disableIngredientSwitching());
		buf.writeBoolean(this.disabledUncrafting());
		buf.writeBoolean(this.disabledTable());
		buf.writeCollection(this.disabledRecipes(), FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipRecipeList());
		buf.writeCollection(this.disabledModids(), FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipModidList());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
