package twilightforest.components.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.network.MovePlayerPacket;
import twilightforest.network.UpdateThrownPacket;

public class YetiThrowAttachment {
	public static final int THROW_COOLDOWN = 200;

	private boolean thrown;
	@Nullable
	private LivingEntity thrower;
	private int throwCooldown;
	private Vec3 throwVector = Vec3.ZERO;

	public void tick(Player player) {
		if (this.thrown && (player.onGround() || player.isSwimming() || player.isInWater())) {
			this.setThrown(player, false, null);
		}
		if (this.throwCooldown > 0) {
			if (!player.level().isClientSide() && this.throwCooldown == THROW_COOLDOWN - 1) {
				player.push(this.throwVector.x(), this.throwVector.y(), this.throwVector.z());
				if (player instanceof ServerPlayer server && ServerPlayNetworking.canSend(server, MovePlayerPacket.TYPE)) {
					ServerPlayNetworking.send(server, new MovePlayerPacket(this.throwVector.x(), this.throwVector.y(), this.throwVector.z()));
				}
				this.throwVector = Vec3.ZERO;
			}
			this.throwCooldown--;
		}
	}

	public boolean getThrown() {
		return this.thrown;
	}

	public void setThrown(Player player, boolean thrown, @Nullable LivingEntity thrower) {
		this.thrown = thrown;
		this.thrower = thrower;
		this.sendUpdatePacket(player);
	}

	@Nullable
	public LivingEntity getThrower() {
		return this.thrower;
	}

	public int getThrowCooldown() {
		return this.throwCooldown;
	}

	public void setThrowCooldown(Player player, int cooldown) {
		this.throwCooldown = cooldown;
		this.sendUpdatePacket(player);
	}

	public void setThrowVector(Vec3 vector) {
		this.throwVector = vector;
	}

	private void sendUpdatePacket(Player player) {
		if (player.level().isClientSide()) return;
		int throwerID = this.thrower != null ? this.thrower.getId() : 0;
		UpdateThrownPacket packet = new UpdateThrownPacket(player.getId(), this.thrown, throwerID, this.throwCooldown);
		for (ServerPlayer watcher : PlayerLookup.tracking(player)) {
			if (ServerPlayNetworking.canSend(watcher, UpdateThrownPacket.TYPE)) ServerPlayNetworking.send(watcher, packet);
		}
		if (player instanceof ServerPlayer self && ServerPlayNetworking.canSend(self, UpdateThrownPacket.TYPE)) {
			ServerPlayNetworking.send(self, packet);
		}
	}
}
