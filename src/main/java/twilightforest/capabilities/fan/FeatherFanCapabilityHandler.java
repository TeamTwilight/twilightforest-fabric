package twilightforest.capabilities.fan;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateFeatherFanFallPacket;

public class FeatherFanCapabilityHandler implements FeatherFanFallCapability {

	private LivingEntity host;
	private boolean falling;

	@Override
	public void setEntity(LivingEntity entity) {
		this.host = entity;
	}

	@Override
	public void update() {
		if (this.getFalling() && !this.host.isOnGround()) {
			this.host.resetFallDistance();
		}

		if (this.host.isOnGround() || this.host.isSwimming()) {
			this.setFalling(false);
		}
	}

	@Override
	public void setFalling(boolean falling) {
		this.falling = falling;
		if (!this.host.getLevel().isClientSide()) {
			TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.host), new UpdateFeatherFanFallPacket(this.host.getId(), this));
		}
	}

	@Override
	public boolean getFalling() {
		return this.falling;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("featherFanFalling", this.getFalling());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setFalling(nbt.getBoolean("featherFanFalling"));
	}
}
