package twilightforest.capabilities.teleporter_cache;

import net.minecraft.nbt.CompoundTag;
import twilightforest.world.TFTeleporter;

public class TeleporterCacheCapabilityHandler implements TeleporterCacheCapability {

	@Override
	public void readFromNbt(CompoundTag nbt) {
		TFTeleporter.loadLinks(nbt);
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		TFTeleporter.saveLinks(nbt);
	}
}
