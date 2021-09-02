package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import twilightforest.extensions.IEntityEx;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin implements IEntityEx {
}
