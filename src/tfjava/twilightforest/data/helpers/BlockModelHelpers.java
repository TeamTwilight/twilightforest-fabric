package twilightforest.data.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class BlockModelHelpers implements DataProvider {
	protected final PackOutput output;
	private final Path sourceAssetRoot;

	protected BlockModelHelpers(PackOutput output) {
		this.output = output;
		this.sourceAssetRoot = Path.of("src", "main", "resources", "assets", TwilightForestMod.ID);
	}

	protected CompletableFuture<?> copyJsonTree(CachedOutput cache, String assetKind) {
		Path sourceDir = this.sourceAssetRoot.resolve(assetKind.replace('/', java.io.File.separatorChar));
		if (!Files.isDirectory(sourceDir)) {
			return CompletableFuture.completedFuture(null);
		}

		PackOutput.PathProvider pathProvider = this.output.createPathProvider(PackOutput.Target.RESOURCE_PACK, assetKind);
		try (Stream<Path> paths = Files.walk(sourceDir)) {
			return CompletableFuture.allOf(paths
				.filter(Files::isRegularFile)
				.filter(path -> path.getFileName().toString().endsWith(".json"))
				.sorted(Comparator.comparing(Path::toString))
				.map(path -> this.saveJson(cache, pathProvider, assetKind, sourceDir, path))
				.toArray(CompletableFuture[]::new));
		} catch (IOException e) {
			throw new IllegalStateException("Failed to scan " + sourceDir, e);
		}
	}

	private CompletableFuture<?> saveJson(CachedOutput cache, PackOutput.PathProvider pathProvider, String assetKind, Path sourceDir, Path sourcePath) {
		ResourceLocation id = toId(sourceDir, sourcePath);
		Path targetPath = pathProvider.json(id);
		try (Reader reader = Files.newBufferedReader(sourcePath)) {
			JsonElement json = JsonParser.parseReader(reader);
			return DataProvider.saveStable(cache, json, targetPath);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to copy " + TwilightForestMod.ID + "/" + assetKind + " JSON " + sourcePath, e);
		}
	}

	private static ResourceLocation toId(Path sourceDir, Path sourcePath) {
		Path relative = sourceDir.relativize(sourcePath);
		String path = relative.toString().replace('\\', '/');
		if (!path.endsWith(".json")) {
			throw new IllegalArgumentException("Expected JSON path: " + sourcePath);
		}
		path = path.substring(0, path.length() - ".json".length());
		return ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, Objects.requireNonNull(path));
	}
}
