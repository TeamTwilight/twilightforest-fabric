package twilightforest.capabilities.shield;

import dev.onyxstudios.cca.api.v3.component.Component;
import twilightforest.TwilightForestMod;

import net.minecraft.resources.ResourceLocation;

public interface IShieldCapability extends Component {

	ResourceLocation ID = TwilightForestMod.prefix("cap_shield");

	void update();

	int shieldsLeft();

	int temporaryShieldsLeft();

	int permanentShieldsLeft();

	void breakShield();

	void replenishShields();

	void setShields(int amount, boolean temp);

    void addShields(int amount, boolean temp);
}
