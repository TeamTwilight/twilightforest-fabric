package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import twilightforest.TFMain;
import twilightforest.potions.FrostedEffect;

public class TFMobEffects {

	public static final Holder<MobEffect> FROSTY = register("frosted", new FrostedEffect());

	private static Holder<MobEffect> register(String name, MobEffect effect) {
		return Registry.registerForHolder(
			BuiltInRegistries.MOB_EFFECT,
			TFMain.prefix(name),
			effect
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing mob effects...");
	}
}