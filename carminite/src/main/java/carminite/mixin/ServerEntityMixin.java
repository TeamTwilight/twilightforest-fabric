package carminite.mixin;

import carminite.hooks.EventHooks;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow
	@Final
	private Entity entity;

	@Inject(
		method = "addPairing(Lnet/minecraft/server/level/ServerPlayer;)V",
		at = @At("TAIL")
	)
	private void carminite$onStartEntityTracking(
		ServerPlayer player,
		CallbackInfo ci
	) {
		EventHooks.onStartEntityTracking(this.entity, player);
	}

	@Inject(
		method = "removePairing(Lnet/minecraft/server/level/ServerPlayer;)V",
		at = @At("TAIL")
	)
	private void carminite$onStopEntityTracking(
		ServerPlayer player,
		CallbackInfo ci
	) {
		EventHooks.onStopEntityTracking(this.entity, player);
	}

	@Inject(
		method = "sendPairingData(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V",
			shift = At.Shift.AFTER,
			ordinal = 0
		)
	)
	private void carminite$sendComplexSpawnData(
		ServerPlayer player,
		Consumer<Packet<?>> broadcast,
		CallbackInfo ci
	) {
		this.entity.carminite$sendPairingData(player, customPacketPayload -> broadcast.accept(new ClientboundCustomPayloadPacket(customPacketPayload)));
	}
}