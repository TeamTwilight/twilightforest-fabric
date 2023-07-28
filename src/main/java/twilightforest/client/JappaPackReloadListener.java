package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import twilightforest.TwilightForestMod;

import java.util.function.BooleanSupplier;

//TODO
// I would like to look at migrating the models to using EntityModelJson (https://www.curseforge.com/minecraft/mc-mods/entity-model-json) in the future.
// we can make the pack depend on it to load the new models instead of having them hardcoded here.
// could also shade the mod since I dont trust people to actually download the mod. I can already see the bug reports flooding in, yikes
public class JappaPackReloadListener implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
	public static final ResourceLocation ID = TwilightForestMod.prefix("jappa");

	private static boolean jappaPackLoaded = false;
	public static final JappaPackReloadListener INSTANCE = new JappaPackReloadListener();

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		jappaPackLoaded = Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().anyMatch(pack -> pack.open().getResource(PackType.CLIENT_RESOURCES, TwilightForestMod.prefix("jappa_models.marker")) != null);
	}

	public static void clientSetup() {
		jappaPackLoaded = Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().anyMatch(pack -> pack.open().getResource(PackType.CLIENT_RESOURCES, TwilightForestMod.prefix("jappa_models.marker")) != null);
	}

	public boolean isJappaPackLoaded() {
		return jappaPackLoaded;
	}

	//Avoid using this. Its needed for entity models only due to reload ordering.
	public BooleanSupplier uncachedJappaPackCheck() {
		return () -> Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().anyMatch(pack -> pack.open().getResource(PackType.CLIENT_RESOURCES, TwilightForestMod.prefix("jappa_models.marker")) != null);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}
}
