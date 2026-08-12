package carminite.mixin;

import carminite.extensions.IOwnedSpawner;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.level.block.entity.SpawnerBlockEntity$1")
public class SpawnerBlockEntity$1Mixin implements IOwnedSpawner {

	@Shadow(aliases = "this$0")
	private SpawnerBlockEntity this$0;

	@Override
	public @Nullable Either<BlockEntity, Entity> carminite$getOwner() {
		return Either.left(this.this$0);
	}
}