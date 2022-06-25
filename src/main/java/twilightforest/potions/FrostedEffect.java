package twilightforest.potions;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import java.util.UUID;

public class FrostedEffect extends MobEffect {

	public static final UUID MODIFIER_UUID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9");

	public FrostedEffect(MobEffectCategory category, int color) {
		super(category, color);

		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, FrostedEffect.MODIFIER_UUID.toString(), -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
