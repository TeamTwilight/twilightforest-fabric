package twilightforest.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Boat.Type.class)
public interface BoatTypeAccessor {

	@Mutable
	@Accessor("planks")
	void twilightforest$setPlanks(Block planks);

	@Invoker("<init>")
	static Boat.Type twilightforest$constructor(String name, int ordinal, Block planks, String label) {
		throw new AssertionError();
	}
}