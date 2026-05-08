package twilightforest.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.custom.TravellersModifiersManager;

/**
 * Q34 mixin for {@link Player}: implements the Travellers Wings double-jump
 * modifier. When the player wears {@code twilightforest:travellers_wings} they
 * get one bonus mid-air jump per landing. The bonus jump triggers when the
 * vanilla jump key is pressed in mid-air and consumes the bonus charge until
 * the player touches the ground again.
 */
@Mixin(Player.class)
public abstract class PlayerMixin {

    @Unique
    private boolean codex_twilight$doubleJumpAvailable = false;

    @Unique
    private boolean codex_twilight$wasJumpPressed = false;

    @Inject(method = "jumpFromGround", at = @At("RETURN"))
    private void codex_twilight$travellersHighJump(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.level().isClientSide()) return;
        if (!TravellersModifiersManager.isHighJumpActive(self)) return;
        if (!self.isCrouching()) return;
        net.minecraft.world.phys.Vec3 m = self.getDeltaMovement();
        // Boost vertical impulse by 0.25 (≈+50% jump height when sneak-jumping).
        self.setDeltaMovement(m.x, m.y + 0.25D, m.z);
        self.hasImpulse = true;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void codex_twilight$tickWingsDoubleJump(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.level().isClientSide()) return;
        if (!TravellersModifiersManager.isDoubleJumpActive(self)) {
            this.codex_twilight$doubleJumpAvailable = false;
            this.codex_twilight$wasJumpPressed = false;
            return;
        }
        if (self.onGround() || self.isInWater() || self.onClimbable() || self.isFallFlying()) {
            this.codex_twilight$doubleJumpAvailable = true;
            this.codex_twilight$wasJumpPressed = self.jumping;
            return;
        }
        boolean nowPressed = self.jumping;
        if (nowPressed && !this.codex_twilight$wasJumpPressed && this.codex_twilight$doubleJumpAvailable) {
            this.codex_twilight$doubleJumpAvailable = false;
            // Refund vanilla jump impulse (matches Player.jumpFromGround logic).
            double jumpPower = 0.42D + (self.getDeltaMovement().y * 0.5D);
            net.minecraft.world.phys.Vec3 m = self.getDeltaMovement();
            self.setDeltaMovement(m.x, jumpPower, m.z);
            self.hasImpulse = true;
            self.level().playSound(null, self.getX(), self.getY(), self.getZ(),
                    net.minecraft.sounds.SoundEvents.PLAYER_BREATH,
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.6F, 1.4F);
            self.level().sendBlockUpdated(self.blockPosition(), self.level().getBlockState(self.blockPosition()),
                    self.level().getBlockState(self.blockPosition()), 0);
        }
        this.codex_twilight$wasJumpPressed = nowPressed;
    }
}
