package carminite.extensions;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface IBucketPickupExtension {
	private BucketPickup self() {
		return (BucketPickup) this;
	}

	default Optional<SoundEvent> carminite$getPickupSound(BlockState state) {
		return self().getPickupSound();
	}
}