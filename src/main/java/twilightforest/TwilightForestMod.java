package twilightforest;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class TwilightForestMod {
    public static final String ID = "codex_twilight";
    public static final String TF_ID = "twilightforest";
    public static final Logger LOGGER = LogUtils.getLogger();

    private TwilightForestMod() {
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(TF_ID, path);
    }

    public static ResourceLocation getModelTexture(String path) {
        return prefix("textures/entity/" + path);
    }

    public static ResourceLocation getEnvTexture(String path) {
        return prefix("textures/environment/" + path);
    }

    public static ResourceLocation getGuiTexture(String path) {
        return prefix("textures/gui/" + path);
    }
}
