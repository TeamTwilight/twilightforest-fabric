package twilightforest.capabilities.thrown;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateThrownPacket;

public class YetiThrowCapabilityHandler implements YetiThrowCapability {

	private boolean thrown;
	private LivingEntity host;
	@Nullable
	private LivingEntity thrower;
	private int throwCooldown;

	public YetiThrowCapabilityHandler(LivingEntity entity) {
		host = entity;
	}

	@Override
	public void setEntity(LivingEntity entity) {
		this.host = entity;
	}

	@Override
	public void update() {
		if (this.host.isOnGround() || this.host.isSwimming()) {
			if (this.getThrown()) {
				this.setThrown(false, null);
			}
			if (this.throwCooldown > 0) {
				this.throwCooldown--;
			}
		}
	}

	@Override
	public boolean getThrown() {
		return this.thrown;
	}

	@Override
	public void setThrown(boolean thrown, @Nullable LivingEntity thrower) {
		this.thrown = thrown;
		this.thrower = thrower;
		this.sendUpdatePacket();
	}

	@Override
	public @Nullable LivingEntity getThrower() {
		return this.thrower;
	}

	@Override
	public int getThrowCooldown() {
		return this.throwCooldown;
	}

	@Override
	public void setThrowCooldown(int cooldown) {
		this.throwCooldown = cooldown;
		this.sendUpdatePacket();
	}

	private void sendUpdatePacket() {
		if (!this.host.getLevel().isClientSide()) {
			TFPacketHandler.CHANNEL.sendToClientsTrackingAndSelf(new UpdateThrownPacket(this.host, this), this.host);
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("yetiThrown", this.getThrown());
		tag.putInt("throwCooldown", this.getThrowCooldown());
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		this.setThrown(nbt.getBoolean("yetiThrown"), null);
		this.setThrowCooldown(nbt.getInt("throwCooldown"));
	}
}
