package carminite;

import carminite.datamaps.DataMapReloadListener;
import carminite.util.ServerLifecycleHooks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Carminite implements ModInitializer {
	public static final String ID = "carminite";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static Identifier prefix(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}

	@Override
	public void onInitialize() {
		ServerLifecycleHooks.init();

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(DataMapReloadListener.INSTANCE);
	}
}