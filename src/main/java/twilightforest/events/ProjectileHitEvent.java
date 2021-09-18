package twilightforest.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.HitResult;

public interface ProjectileHitEvent {
    Event<ProjectileHitEvent> PROJECTILE_HIT_EVENT = EventFactory.createArrayBacked(ProjectileHitEvent.class, (projectileHitEvents -> (arrow, result) -> {
        for (ProjectileHitEvent event : projectileHitEvents) {
            event.onHit(arrow, result);
        }
    }));

    void onHit(AbstractArrow abstractArrow, HitResult result);
}
