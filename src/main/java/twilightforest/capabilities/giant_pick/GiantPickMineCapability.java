package twilightforest.capabilities.giant_pick;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import twilightforest.TwilightForestMod;

public interface GiantPickMineCapability extends INBTSerializable<CompoundTag> {

	ResourceLocation ID = TwilightForestMod.prefix("giant_pick_mine");

	void setMining(long mining);

	long getMining();
}
