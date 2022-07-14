package twilightforest.capabilities.fan;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.TwilightForestMod;

public interface FeatherFanFallCapability extends Component {

	ResourceLocation ID = TwilightForestMod.prefix("cap_feather_fan_fall");

	void setEntity(LivingEntity entity);

	void update();

	void setFalling(boolean falling);

	boolean getFalling();
}
