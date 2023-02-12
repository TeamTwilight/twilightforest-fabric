package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import twilightforest.TFConfig;

import java.util.List;
import java.util.function.Supplier;

public class SyncUncraftingTableConfigPacket {

	private final double uncraftingMultiplier;
	private final double repairingMultiplier;
	private final boolean allowShapeless;
	private final boolean disabled;
	private final List<String> disabledRecipes;
	private final boolean flipRecipeList;
	private final List<String> disabledModids;
	private final boolean flipModidList;

	//I think casting is fine in this case. The forge config requires that entries extend string but they should always be strings
	@SuppressWarnings("unchecked")
	public SyncUncraftingTableConfigPacket(double uncraftingMultiplier, double repairingMultiplier, boolean allowShapeless, boolean disabled, List<? extends String> disabledRecipes, boolean flipRecipeList, List<? extends String> disabledModids, boolean flipModidList) {
		this.uncraftingMultiplier = uncraftingMultiplier;
		this.repairingMultiplier = repairingMultiplier;
		this.allowShapeless = allowShapeless;
		this.disabled = disabled;
		this.disabledRecipes = (List<String>) disabledRecipes;
		this.flipRecipeList = flipRecipeList;
		this.disabledModids = (List<String>) disabledModids;
		this.flipModidList = flipModidList;
	}

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this.uncraftingMultiplier = buf.readDouble();
		this.repairingMultiplier = buf.readDouble();
		this.allowShapeless = buf.readBoolean();
		this.disabled = buf.readBoolean();
		this.disabledRecipes = buf.readList(FriendlyByteBuf::readUtf);
		this.flipRecipeList = buf.readBoolean();
		this.disabledModids = buf.readList(FriendlyByteBuf::readUtf);
		this.flipModidList = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier);
		buf.writeDouble(this.repairingMultiplier);
		buf.writeBoolean(this.allowShapeless);
		buf.writeBoolean(this.disabled);
		buf.writeCollection(this.disabledRecipes, FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipRecipeList);
		buf.writeCollection(this.disabledModids, FriendlyByteBuf::writeUtf);
		buf.writeBoolean(this.flipModidList);
	}

	public static class Handler {

		public static boolean onMessage(SyncUncraftingTableConfigPacket message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.set(message.uncraftingMultiplier);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.repairingXpCostMultiplier.set(message.repairingMultiplier);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.allowShapelessUncrafting.set(message.allowShapeless);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncrafting.set(message.disabled);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.set(message.disabledRecipes);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.set(message.flipRecipeList);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.set(message.disabledModids);
				TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.set(message.flipModidList);
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
