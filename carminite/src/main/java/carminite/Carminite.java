package carminite;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
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
	}
}