package carminite.extensions;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public interface IOwnedSpawner {

	@Nullable
	default Either<BlockEntity, Entity> carminite$getOwner() {
		return null;
	}
}