package twilightforest.capabilities.thrown;

import io.github.fabricators_of_create.porting_lib.extensions.INBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

public interface YetiThrowCapability extends INBTSerializable<CompoundTag> {

	ResourceLocation ID = TwilightForestMod.prefix("cap_thrown");

	void setEntity(LivingEntity entity);

	void update();

	boolean getThrown();

	void setThrown(boolean thrown, @Nullable LivingEntity thrower);

	@Nullable
	LivingEntity getThrower();

	int getThrowCooldown();

	void setThrowCooldown(int cooldown);
}
