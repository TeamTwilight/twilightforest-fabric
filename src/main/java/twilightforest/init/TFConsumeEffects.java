package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import twilightforest.TFMain;
import twilightforest.item.effects.StackableEffectConsumeEffect;

public class TFConsumeEffects {

	public static final ConsumeEffect.Type<StackableEffectConsumeEffect> STACKABLE_EFFECTS = register("stackable_effects", new ConsumeEffect.Type<>(StackableEffectConsumeEffect.CODEC, StackableEffectConsumeEffect.STREAM_CODEC));

	private static <T extends ConsumeEffect> ConsumeEffect.Type<T> register(String name, ConsumeEffect.Type<T> type) {
		return Registry.register(
			BuiltInRegistries.CONSUME_EFFECT_TYPE,
			TFMain.prefix(name),
			type
		);
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing consume effects...");
	}
}