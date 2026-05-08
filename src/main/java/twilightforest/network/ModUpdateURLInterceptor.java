package twilightforest.network;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.TwilightForestMod;

import java.net.URI;
import java.util.Optional;

public final class ModUpdateURLInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModUpdateURLInterceptor.class);
	private static final String UPDATE_URL = "https://gh.tamaized.com/TeamTwilight/twilightforest/update.json?m=%s&l=%s&v=%s";
	private static boolean bootstrapped;
	private static URI updateUri;

	private ModUpdateURLInterceptor() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		String minecraftVersion = FabricLoader.getInstance().getModContainer("minecraft")
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("unknown");
		String loaderVersion = FabricLoader.getInstance().getModContainer("fabricloader")
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse(FabricLoader.getInstance().getModContainer(TwilightForestMod.ID)
				.map(ModContainer::getMetadata)
				.map(metadata -> metadata.getVersion().getFriendlyString())
				.orElse("unknown"));
		updateUri = URI.create(UPDATE_URL.formatted(minecraftVersion, "Fabric", loaderVersion));
		LOGGER.debug("Twilight Forest update URL prepared for Fabric metadata: {}", updateUri);
	}

	public static Optional<URI> updateUri() {
		return Optional.ofNullable(updateUri);
	}
}
