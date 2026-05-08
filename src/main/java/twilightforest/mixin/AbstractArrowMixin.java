package twilightforest.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.EnderBowItem;

import java.util.List;

/**
 * Q34 mixin for {@link AbstractArrow}: implements the Travellers Vest /
 * Goggles "arrow magnetism" modifier. Arrows fired by an entity wearing the
 * appropriate piece subtly home onto the nearest valid living target within
 * a 12-block sphere, with a 5% velocity blend per tick toward the target.
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    private static final TagKey<EntityType<?>> CODEX_TWILIGHT_COMMON_BOSSES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", "bosses"));

    @Inject(method = "tick", at = @At("HEAD"))
    private void codex_twilight$arrowMagnetism(CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        if (self.level().isClientSide()) return;
        if (self.inGround) return;
        if (!(self.getOwner() instanceof LivingEntity owner)) return;

        // Agile Ranger (gloves) — once on spawn, tighten the firing spread by
        // re-normalising the arrow to a clean direction with no jitter (only if
        // the owner was moving when shooting — moving accuracy is the modifier's
        // signature). Implemented via single-tick scrub on first tick.
        if (self.tickCount == 1 && TravellersModifiersManager.isAgileRangerActive(owner)
                && owner.getDeltaMovement().horizontalDistance() > 0.05D) {
            Vec3 m = self.getDeltaMovement();
            double speed = m.length();
            if (speed > 0.1D) {
                Vec3 lookDir = owner.getLookAngle();
                self.setDeltaMovement(lookDir.scale(speed));
            }
        }

        if (!TravellersModifiersManager.isArrowMagnetismActive(owner)) return;
        // Throttle the AABB scan to every other tick — arrows fly fast enough that
        // half-resolution homing is indistinguishable but halves CPU cost.
        if ((self.tickCount & 1) != 0) return;

        Vec3 here = self.position();
        AABB scan = new AABB(here, here).inflate(12.0D);
        List<LivingEntity> candidates = self.level().getEntitiesOfClass(LivingEntity.class, scan,
                e -> e != owner && e.isAlive() && !e.isInvisible() && e.attackable());
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity c : candidates) {
            double d = c.position().distanceToSqr(here);
            if (d < bestDist) {
                bestDist = d;
                best = c;
            }
        }
        if (best == null) return;

        Vec3 toTarget = best.getEyePosition().subtract(here).normalize();
        Vec3 currentMotion = self.getDeltaMovement();
        double speed = currentMotion.length();
        if (speed < 0.1D) return;
        Vec3 currentDir = currentMotion.normalize();
        Vec3 blended = currentDir.scale(0.95D).add(toTarget.scale(0.05D)).normalize().scale(speed);
        self.setDeltaMovement(blended);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void codex_twilight$enderBowSwap(EntityHitResult result, CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        if (!self.getTags().contains(EnderBowItem.KEY)) {
            return;
        }
        if (!(self.getOwner() instanceof Player player) || !(result.getEntity() instanceof LivingEntity living)) {
            return;
        }
        if (player == living || living.getType().is(CODEX_TWILIGHT_COMMON_BOSSES)) {
            return;
        }

        double sourceX = player.getX();
        double sourceY = player.getY();
        double sourceZ = player.getZ();
        float sourceYaw = player.getYRot();
        float sourcePitch = player.getXRot();
        Entity playerVehicle = player.getVehicle();

        player.setYRot(living.getYRot());
        player.teleportTo(living.getX(), living.getY(), living.getZ());
        player.invulnerableTime = 40;
        player.level().broadcastEntityEvent(player, (byte) 46);
        if (living.isPassenger() && living.getVehicle() != null) {
            player.startRiding(living.getVehicle(), true);
            living.stopRiding();
        }
        player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

        living.setYRot(sourceYaw);
        living.setXRot(sourcePitch);
        living.teleportTo(sourceX, sourceY, sourceZ);
        living.level().broadcastEntityEvent(living, (byte) 46);
        if (playerVehicle != null) {
            living.startRiding(playerVehicle, true);
            player.stopRiding();
        }
        living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
    }
}
