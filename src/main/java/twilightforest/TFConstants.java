package twilightforest;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

import java.util.Locale;

public class TFConstants {

    // TODO: might be a good idea to find proper spots for all of these? also remove redundants
    public static final String ID = "twilightforest";

    private static final String MODEL_DIR = "textures/model/";
    private static final String GUI_DIR = "textures/gui/";
    private static final String ENVIRO_DIR = "textures/environment/";
    private static final String ARMOR_DIR = "textures/armor/";

    public static final Rarity rarity = ClassTinkerers.getEnum(Rarity.class, "TWILIGHT");

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(ID, name.toLowerCase(Locale.ROOT));
    }

    public static ResourceLocation getModelTexture(String name) {
        return new ResourceLocation(ID, MODEL_DIR + name);
    }

    public static ResourceLocation getGuiTexture(String name) {
        return new ResourceLocation(ID, GUI_DIR + name);
    }

    public static ResourceLocation getEnvTexture(String name) {
        return new ResourceLocation(ID, ENVIRO_DIR + name);
    }

    public static ResourceLocation getArmorTexture(String name) {
        return new ResourceLocation(ID, ARMOR_DIR + name);
    }

    public static Rarity getRarity() {
        return rarity != null ? rarity : Rarity.EPIC;
    }
}
