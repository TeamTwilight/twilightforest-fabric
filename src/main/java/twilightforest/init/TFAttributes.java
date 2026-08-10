package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import twilightforest.TFMain;

public class TFAttributes {

	public static final Attribute CLONE_COUNT = register("clone_count", new RangedAttribute("attribute.name.lich.clone_count", 2, 0, 1024).setSyncable(true));
	public static final Attribute SHIELD_STRENGTH = register("max_shield_strength", new RangedAttribute("attribute.name.lich.shield_strength", 6, 0, 1024).setSyncable(true));
	public static final Attribute MINION_COUNT = register("minion_count", new RangedAttribute("attribute.name.lich.minion_count", 9, 0, 1024).setSyncable(true));

	private static Attribute register(String name, Attribute attribute) {
		return Registry.register(
			BuiltInRegistries.ATTRIBUTE,
			TFMain.prefix(name),
			attribute
		);
	}
}