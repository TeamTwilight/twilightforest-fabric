package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import twilightforest.inventory.UncraftingContainer;

public class UncraftingGuiPacket extends ISimplePacket {
    private int type;

    public UncraftingGuiPacket(int type) {
        this.type = type;
    }

    public UncraftingGuiPacket(FriendlyByteBuf buf) {
        type = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(type);
    }

    @Override
    public void onMessage(Player playerEntity) {
        Handler.onMessage(this, (ServerPlayer) playerEntity);
    }

    public static class Handler {

        @SuppressWarnings("Convert2Lambda")
        public static boolean onMessage(UncraftingGuiPacket message, ServerPlayer player) {
            Minecraft.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    AbstractContainerMenu container = player.containerMenu;

                    if (container instanceof UncraftingContainer) {
                        UncraftingContainer uncrafting = (UncraftingContainer) container;

                        switch (message.type) {
                            case 0:
                                uncrafting.unrecipeInCycle++;
                                break;
                            case 1:
                                uncrafting.unrecipeInCycle--;
                                break;
                            case 2:
                                uncrafting.ingredientsInCycle++;
                                break;
                            case 3:
                                uncrafting.ingredientsInCycle--;
                                break;
                            case 4:
                                uncrafting.recipeInCycle++;
                                break;
                            case 5:
                                uncrafting.recipeInCycle--;
                                break;
                        }

                        if (message.type < 4)
                            uncrafting.slotsChanged(uncrafting.tinkerInput);

                        if (message.type >= 4)
                            uncrafting.slotsChanged(uncrafting.assemblyMatrix);
                    }
                }
            });

            return true;
        }
    }
}
