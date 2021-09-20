package twilightforest.mixin.plugin;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import net.minecraft.ChatFormatting;

public class TFEarlyRiser implements Runnable {

    @Override
    public void run() {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
        ClassTinkerers.enumBuilder(resolver.mapClassName("intermediary", "net.minecraft.class_1814"), "L"+resolver.mapClassName("intermediary", "net.minecraft.class_124")+";").addEnum("TWILIGHT", ChatFormatting.DARK_GREEN).build();
    }
}
