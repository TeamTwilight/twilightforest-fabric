package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;

public final class TFDimension {
    public static final ResourceLocation DIMENSION = TwilightForestMod.prefix("twilight_forest");
    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);
    public static final ResourceLocation DIMENSION_RENDERER = TwilightForestMod.prefix("renderer");

    private TFDimension() {
    }

    public static boolean isTwilightPortalDestination(Level level) {
        return DIMENSION.equals(level.dimension().location());
    }

    public static boolean isTwilightWorldOnClient(Level clientWorld) {
        return TwilightForestMod.ID.equals(clientWorld.dimension().location().getNamespace()) || isTwilightPortalDestination(clientWorld);
    }
}
