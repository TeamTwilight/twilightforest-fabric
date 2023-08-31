package twilightforest.capabilities.thrown;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

public interface YetiThrowCapability extends Component {

	ResourceLocation ID = TwilightForestMod.prefix("cap_thrown");

	void setEntity(LivingEntity entity);

	void update();

	boolean getThrown();

	void setThrown(boolean thrown, @Nullable LivingEntity thrower);

	@Nullable
	LivingEntity getThrower();

	int getThrowCooldown();

	void setThrowCooldown(int cooldown);

	void setThrowVector(Vec3 vector);
}
