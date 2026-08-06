package twilightforest.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Boat.Type.class)
public interface BoatTypeAccessor {

	@Mutable
	@Accessor("planks")
	void twilightforest$setPlanks(Block planks);
}