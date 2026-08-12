package carminite.extensions;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

/**
 * Injected into MobSpawnerBlockEntity, MinecartSpawner,and TrialSpawnerBlockEntity via Mixin, and into BaseSpawner in twilightforest.classtweaker
 */
public interface IOwnedSpawner {

	@Nullable
	default Either<BlockEntity, Entity> carminite$getOwner() {
		return null;
	}
}