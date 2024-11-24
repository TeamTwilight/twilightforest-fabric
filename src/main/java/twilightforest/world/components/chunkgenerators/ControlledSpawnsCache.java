package twilightforest.world.components.chunkgenerators;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;

import java.util.List;
import java.util.WeakHashMap;

public class ControlledSpawnsCache extends SimplePreparableReloadListener<Object> implements IdentifiableResourceReloadListener {
	public static WeakHashMap<ChunkGeneratorTwilight, List<Structure>> CONTROLLED_SPAWNS = new WeakHashMap<>();
	public static void reloadSpawnsCache() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ControlledSpawnsCache());
	}
	@Override
	protected Object prepare(ResourceManager manager, ProfilerFiller filler) {
		return 0;
	}
	@Override
	protected void apply(Object obj, ResourceManager manager, ProfilerFiller filler) {
		CONTROLLED_SPAWNS.clear();
	}

	@Override
	public ResourceLocation getFabricId() {
		return TwilightForestMod.prefix("spawnscache");
	}
}