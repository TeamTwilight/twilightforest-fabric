package twilightforest.client;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import twilightforest.TwilightForestMod;

import java.util.function.BooleanSupplier;

public class JappaPackReloadListener implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
    public static final ResourceLocation ID = TwilightForestMod.prefix("jappa");

    private static boolean jappaPackLoaded = false;
    public static final JappaPackReloadListener INSTANCE = new JappaPackReloadListener();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        jappaPackLoaded = isJappaPackSelected();
    }

    public static void clientSetup() {
        jappaPackLoaded = isJappaPackSelected();
    }

    public boolean isJappaPackLoaded() {
        return jappaPackLoaded;
    }

    public BooleanSupplier uncachedJappaPackCheck() {
        return JappaPackReloadListener::isJappaPackSelected;
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    private static boolean isJappaPackSelected() {
        return Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream()
                .anyMatch(pack -> pack.open().getResource(PackType.CLIENT_RESOURCES, TwilightForestMod.prefix("jappa_models.marker")) != null);
    }
}