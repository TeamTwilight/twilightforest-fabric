package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import twilightforest.TFConfig;

import java.util.List;

public class SyncUncraftingTableConfigPacket implements S2CPacket {

	private final double uncraftingMultiplier;
	private final double repairingMultiplier;
	private final boolean allowShapeless;
	private final boolean disabledUncrafting;
	private final boolean disabledTable;
	private final List<String> disabledRecipes;
	private final boolean flipRecipeList;
	private final List<String> disabledModids;
	private final boolean flipModidList;

	//I think casting is fine in this case. The forge config requires that entries extend string but they should always be strings
	@SuppressWarnings("unchecked")
	public SyncUncraftingTableConfigPacket(double uncraftingMultiplier, double repairingMultiplier, boolean allowShapeless, boolean disabledUncrafting, boolean disabledTable, List<? extends String> disabledRecipes, boolean flipRecipeList, List<? extends String> disabledModids, boolean flipModidList) {
		this.uncraftingMultiplier = uncraftingMultiplier;
		this.repairingMultiplier = repairingMultiplier;
		this.allowShapeless = allowShapeless;
		this.disabledUncrafting = disabledUncrafting;
		this.disabledTable = disabledTable;
		this.disabledRecipes = (List<String>) disabledRecipes;
		this.flipRecipeList = flipRecipeList;
		this.disabledModids = (List<String>) disabledModids;
		this.flipModidList = flipModidList;
	}

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this.uncraftingMultiplier = buf.readDouble();
		this.repairingMultiplier = buf.readDouble();
		this.allowShapeless = buf.readBoolean();
		this.disabledUncrafting = buf.readBoolean();
		this.disabledTable = buf.readBoolean();
		this.disabledRecipes = buf.readList(FriendlyByteBuf::readUtf);
		this.flipRecipeList = buf.readBoolean();
		this.disabledModids = buf.readList(FriendlyByteBuf::readUtf);
		this.flipModidList = buf.readBoolean();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier);
		buf.writeDouble(this.repairingMultiplier);
		buf.writeBoolean(this.allowShapeless);
		buf.writeBoolean(this.disabledUncrafting);
		buf.writeBoolean(this.disabledTable);
		buf.writeCollection(this.disabledRecipes, FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipRecipeList);
		buf.writeCollection(this.disabledModids, FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipModidList);
	}


	@Override
	public void handle(Minecraft client, ClientPacketListener packetListener, PacketSender sender, SimpleChannel channel) {
		client.execute(() -> {
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.set(this.uncraftingMultiplier);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.repairingXpCostMultiplier.set(this.repairingMultiplier);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.allowShapelessUncrafting.set(this.allowShapeless);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingOnly.set(message.disabledUncrafting);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.set(this.disabledTable);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.set(this.disabledRecipes);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.set(this.flipRecipeList);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.set(this.disabledModids);
			TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.set(this.flipModidList);
		});
	}
}
