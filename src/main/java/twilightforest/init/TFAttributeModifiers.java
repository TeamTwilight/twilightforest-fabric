package twilightforest.init;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import twilightforest.TFMain;

public class TFAttributeModifiers {
	public static final AttributeModifier TRAVELLERS_HIGH_STEP = new AttributeModifier(TFMain.prefix("travellers_gear.boots_high_step"), 0.5F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE = new AttributeModifier(TFMain.prefix("travellers_gear.boots_double_jump_safe_fall_distance"), 2F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_SWIFT_SWIM = new AttributeModifier(TFMain.prefix("travellers_gear.vest_fast_swimming"), 1F, AttributeModifier.Operation.ADD_VALUE);

	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_OXYGEN = new AttributeModifier(TFMain.prefix("travellers_gear.goggles_aquatic_agility_oxygen"), 3.0F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_MINING = new AttributeModifier(TFMain.prefix("travellers_gear.goggles_aquatic_agility_mining"), 4.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

	public static final Identifier STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION = TFMain.prefix("travellers_gear.boots_straight_ahead");
}
