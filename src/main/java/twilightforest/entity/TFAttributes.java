package twilightforest.entity;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import twilightforest.mixin.ServerPlayerGameModeMixin;

public class TFAttributes {

    public static final Attribute REACH_DISTANCE = register("reach_distance", new RangedAttribute("generic.reachDistance", 5.0D, 0.0D, 1024.0D).setSyncable(true));


    public static void init(){
    }

    private static Attribute register(String id, Attribute attribute) {
        return (Attribute) Registry.register(Registry.ATTRIBUTE, id, attribute);
    }


}
