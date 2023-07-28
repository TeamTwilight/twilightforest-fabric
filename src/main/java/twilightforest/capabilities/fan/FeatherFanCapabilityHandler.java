package twilightforest.capabilities.fan;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateFeatherFanFallPacket;

public class FeatherFanCapabilityHandler implements FeatherFanFallCapability {

	private Player host;
	private boolean falling;

	public FeatherFanCapabilityHandler(Player entity) {
		host = entity;
	}

	@Override
	public void setEntity(Player entity) {
		this.host = entity;
	}

	@Override
	public void update() {
		if (this.getFalling()) {
			if (!this.host.onGround()) {
				this.host.resetFallDistance();
			}

			if (this.host.onGround() || this.host.isSwimming()) {
				this.setFalling(false);
			}
		}
	}

	@Override
	public void setFalling(boolean falling) {
		this.falling = falling;
		if (!this.host.level().isClientSide()) {
			if (this.host instanceof ServerPlayer serverPlayer && serverPlayer.connection != null)
				TFPacketHandler.CHANNEL.sendToClientsTrackingAndSelf(new UpdateFeatherFanFallPacket(this.host.getId(), this), this.host);
		}
	}

	@Override
	public boolean getFalling() {
		return this.falling;
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("featherFanFalling", this.getFalling());
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		this.setFalling(tag.getBoolean("featherFanFalling"));
	}
}
