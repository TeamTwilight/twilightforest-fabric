package twilightforest.capabilities.teleporter_cache;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import twilightforest.world.TFTeleporter;

public record TeleporterCacheCapabilityHandler(Level level) implements TeleporterCacheCapability {

	@Override
	public void readFromNbt(CompoundTag nbt) {
		if (level.dimension().equals(Level.OVERWORLD))
			TFTeleporter.loadLinks(nbt);
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		if (level.dimension().equals(Level.OVERWORLD))
			TFTeleporter.saveLinks(nbt);
	}
}
