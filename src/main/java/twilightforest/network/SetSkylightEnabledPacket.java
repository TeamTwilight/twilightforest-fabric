package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class SetSkylightEnabledPacket extends ISimplePacket {

    private final boolean enabled;

    public SetSkylightEnabledPacket(boolean enabled) {
        this.enabled = enabled;
    }

    public SetSkylightEnabledPacket(FriendlyByteBuf buf) {
        enabled = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    @Override
    public void onMessage(Player playerEntity) {
        Handler.onMessage(this);
    }

    public static class Handler {
        public static boolean onMessage(SetSkylightEnabledPacket message) {
            // FIXME UNUSED AND CLEAR ALL
            // TwilightForestDimension.setSkylightEnabled(message.enabled);
            return true;
        }
    }
}
