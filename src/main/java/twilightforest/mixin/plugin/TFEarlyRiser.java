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
        ClassTinkerers.enumBuilder(resolver.mapClassName("intermediary", "net.minecraft.class_2582"), String.class, String.class, boolean.class)
                .addEnum("TF_NAGA", "tf_naga", "tfn", true)
                .addEnum("TF_LICH", "tf_lich", "tfl", true)
                .addEnum("TF_MINOSHROOM", "tf_minoshroom", "tfm", true)
                .addEnum("TF_HYDRA", "tf_hydra", "tfh", true)
                .addEnum("TF_PHANTOMS", "tf_phantoms", "tfp", true)
                .addEnum("TF_UR_GHAST", "tf_ur_ghast", "tfg", true)
                .addEnum("TF_ALPHA_YETI", "tf_alpha_yeti", "tfy", true)
                .addEnum("TF_SNOW_QUEEN", "tf_snow_queen", "tfq", true)
                .addEnum("TF_QUEST_RAM", "tf_quest_ram", "tfr", true)
                .build();
    }
}
