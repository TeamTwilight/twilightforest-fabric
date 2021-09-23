package twilightforest.potions;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import twilightforest.TFConstants;

public class TFPotions {

	//public static final DeferredRegister<MobEffect> POTIONS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TwilightForestMod.ID);

	public static final MobEffect frosty = Registry.register(Registry.MOB_EFFECT, TFConstants.ID+":frosted", new FrostedPotion(MobEffectCategory.HARMFUL, 0x56CBFD));
}
