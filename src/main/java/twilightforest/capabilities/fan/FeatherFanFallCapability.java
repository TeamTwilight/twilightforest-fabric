package twilightforest.capabilities.fan;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import twilightforest.TwilightForestMod;

public interface FeatherFanFallCapability extends INBTSerializable<CompoundTag> {

	ResourceLocation ID = TwilightForestMod.prefix("cap_feather_fan_fall");

	void setEntity(LivingEntity entity);

	void update();

	void setFalling(boolean falling);

	boolean getFalling();
}
