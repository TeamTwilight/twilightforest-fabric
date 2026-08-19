package twilightforest.asm.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.MinecartSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.fabric.interfaces.extension.IOwnedSpawner;

@Mixin(targets = "net.minecraft.world.entity.vehicle.minecart.MinecartSpawner$1")
public class MinecartSpawner$1Mixin implements IOwnedSpawner {

	@Shadow(aliases = "this$0")
	private MinecartSpawner this$0;

	@Override
	public @Nullable Either<BlockEntity, Entity> twilightforest$getOwner() {
		return Either.right(this.this$0);
	}
}