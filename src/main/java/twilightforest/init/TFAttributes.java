package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import twilightforest.TwilightForestMod;

public final class TFAttributes {
    public static final Holder<Attribute> CLONE_COUNT = register("clone_count",
            new RangedAttribute("attribute.name.lich.clone_count", 2.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final Holder<Attribute> SHIELD_STRENGTH = register("max_shield_strength",
            new RangedAttribute("attribute.name.lich.shield_strength", 6.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final Holder<Attribute> MINION_COUNT = register("minion_count",
            new RangedAttribute("attribute.name.lich.minion_count", 9.0D, 0.0D, 1024.0D).setSyncable(true));

    private TFAttributes() {
    }

    public static void bootstrap() {
        CLONE_COUNT.value();
        SHIELD_STRENGTH.value();
        MINION_COUNT.value();
    }

    private static Holder<Attribute> register(String path, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, TwilightForestMod.prefix(path), attribute);
    }
}
