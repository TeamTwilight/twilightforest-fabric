package twilightforest.capabilities.giant_pick;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public interface GiantPickMineCapability extends Component {

	ResourceLocation ID = TwilightForestMod.prefix("giant_pick_mine");

	void setMining(long mining);

	long getMining();

	void setBreaking(boolean breaking);

	boolean getBreaking();

	void setGiantBlockConversion(int giantBlockConversion);

	int getGiantBlockConversion();

	boolean canMakeGiantBlock();
}
