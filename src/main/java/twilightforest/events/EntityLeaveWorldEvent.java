package twilightforest.events;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityLeaveWorldEvent extends Event {
    private final Level world;
    private final Entity entity;

    public EntityLeaveWorldEvent(Entity entity, Level world)
    {
        this.entity = entity;
        this.world = world;
    }

    public Level getWorld()
    {
        return world;
    }

    public Entity getEntity()
    {
        return entity;
    }
}
