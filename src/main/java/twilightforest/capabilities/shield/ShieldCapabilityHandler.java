package twilightforest.capabilities.shield;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.init.TFSounds;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;
import twilightforest.init.TFStats;

public class ShieldCapabilityHandler implements IShieldCapability {
	private int temporaryShields;
	private int permanentShields;
	private LivingEntity host;
	private int timer;
	private int breakTimer;

	public ShieldCapabilityHandler(LivingEntity entity) {
		this.host = entity;
	}

	@Override
	public void update() {
		if (!this.host.level().isClientSide() && temporaryShieldsLeft() > 0 && this.timer-- <= 0 && this.breakTimer <= 0 && (!(this.host instanceof Player player) || !player.isCreative()))
			breakShield();
		if (this.breakTimer > 0)
			this.breakTimer--;
	}

	@Override
	public int shieldsLeft() {
		return this.temporaryShields + this.permanentShields;
	}

	@Override
	public int temporaryShieldsLeft() {
		return this.temporaryShields;
	}

	@Override
	public int permanentShieldsLeft() {
		return this.permanentShields;
	}

	@Override
	public void breakShield() {
		if (this.breakTimer <= 0) {
			// Temp shields should break first before permanent ones. Reset time each time a temp shield is busted.
			if (this.temporaryShields > 0) {
				this.temporaryShields--;
				this.resetTimer();
			} else if (this.permanentShields > 0) {
				this.permanentShields--;
			}

			if (this.host instanceof ServerPlayer player)
				player.awardStat(TFStats.TF_SHIELDS_BROKEN.get());
			this.sendUpdatePacket();
			this.host.level().playSound(null, this.host.blockPosition(), TFSounds.SHIELD_BREAK.get(), SoundSource.PLAYERS, 1.0F, ((this.host.getRandom().nextFloat() - this.host.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.breakTimer = 20;
		}
	}

	@Override
	public void replenishShields() {
		this.setShields(5, true);
		this.host.level().playSound(null, this.host.blockPosition(), TFSounds.SHIELD_ADD.get(), SoundSource.PLAYERS, 1.0F, (this.host.getRandom().nextFloat() - this.host.getRandom().nextFloat()) * 0.2F + 1.0F);
	}

	@Override
	public void setShields(int amount, boolean temp) {
		if (temp) {
			this.temporaryShields = Math.max(amount, 0);
			this.resetTimer();
		} else {
			this.permanentShields = Math.max(amount, 0);
		}

		this.sendUpdatePacket();
	}

	@Override
	public void addShields(int amount, boolean temp) {
		if (temp) {
			if (this.temporaryShields <= 0)
				this.resetTimer(); // Since we add new shields to the stack instead of setting them, no timer reset is needed, unless they start from 0 shields.
			this.temporaryShields = Math.max(this.temporaryShields + amount, 0);
		} else {
			this.permanentShields = Math.max(this.permanentShields + amount, 0);
		}

		sendUpdatePacket();
	}

	void initShields(int temporary, int permanent) {
		this.temporaryShields = Math.max(temporary, 0);
		this.permanentShields = Math.max(permanent, 0);
		this.resetTimer();
	}

	private void resetTimer() {
		this.timer = 240;
	}

	private void sendUpdatePacket() {
		if (this.host instanceof ServerPlayer)
			TFPacketHandler.CHANNEL.sendToClientsTrackingAndSelf(new UpdateShieldPacket(host, this), host);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("tempshields", this.temporaryShieldsLeft());
		tag.putInt("permshields", this.permanentShieldsLeft());
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		this.initShields(tag.getInt("tempshields"), tag.getInt("permshields"));
	}
}
