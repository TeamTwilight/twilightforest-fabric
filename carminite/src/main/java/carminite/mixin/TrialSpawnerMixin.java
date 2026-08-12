package carminite.mixin;

import carminite.extensions.IOwnedSpawner;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TrialSpawner.class)
public class TrialSpawnerMixin implements IOwnedSpawner {

	@Shadow
	@Final
	private TrialSpawner.StateAccessor stateAccessor;

	@Override
	public @Nullable Either<BlockEntity, Entity> carminite$getOwner() {
		if (this.stateAccessor instanceof TrialSpawnerBlockEntity be) {
			return Either.left(be);
		}
		return null;
	}
}