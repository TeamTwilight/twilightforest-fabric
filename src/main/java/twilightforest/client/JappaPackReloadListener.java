package twilightforest.client;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import twilightforest.TwilightForestMod;

import java.util.Collection;
import java.util.List;

//TODO
// I would like to look at migrating the models to using EntityModelJson (https://www.curseforge.com/minecraft/mc-mods/entity-model-json) in the future.
// we can make the pack depend on it to load the new models instead of having them hardcoded here.
// could also shade the mod since I dont trust people to actually download the mod. I can already see the bug reports flooding in, yikes
public class JappaPackReloadListener implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {

	private static boolean jappaPackLoaded = false;
	public static final JappaPackReloadListener INSTANCE = new JappaPackReloadListener();

	@Override
	public ResourceLocation getFabricId() {
		return TwilightForestMod.prefix(this.getName());
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		jappaPackLoaded = Minecraft.getInstance().getResourceManager().listPacks().anyMatch(pack -> pack.getResource(PackType.CLIENT_RESOURCES, TwilightForestMod.prefix("jappa_models.marker")) != null);
	}

	@Override
	public Collection<ResourceLocation> getFabricDependencies() {
		return List.of();
	}

	public boolean isJappaPackLoaded() {
		return jappaPackLoaded;
	}
}