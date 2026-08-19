package twilightforest.fabric.interfaces.extension;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public interface IOwnedSpawner {

	@Nullable
	default Either<BlockEntity, Entity> twilightforest$getOwner() {
		return null;
	}
}