package twilightforest.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HangingEntity.class)
public interface HangingEntityAccessor {

	@Invoker("setDirection")
	void twilightforest$setDirection(Direction direction);
}