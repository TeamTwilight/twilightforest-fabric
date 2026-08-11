package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import twilightforest.TFMain;
import twilightforest.potions.FrostedEffect;

public class TFMobEffects {

	public static final MobEffect FROSTY = register("frosted", new FrostedEffect());

	private static MobEffect register(String name, MobEffect effect) {
		return Registry.register(
			BuiltInRegistries.MOB_EFFECT,
			TFMain.prefix(name),
			effect
		);
	}
}