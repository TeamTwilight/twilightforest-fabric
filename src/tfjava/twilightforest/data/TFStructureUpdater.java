package twilightforest.data;

import com.google.common.hash.Hashing;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class TFStructureUpdater implements DataProvider {
	private final String basePath;
	private final PackOutput output;
	private final Path inputRoot;

	public TFStructureUpdater(String basePath, PackOutput output, Object helper) {
		this.basePath = normalizeBasePath(basePath);
		this.output = output;
		this.inputRoot = Path.of("src", "main", "resources", "data", TwilightForestMod.ID, this.basePath);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		if (!Files.isDirectory(this.inputRoot)) {
			return CompletableFuture.completedFuture(null);
		}

		try {
			try (var paths = Files.walk(this.inputRoot)) {
				for (Path path : paths.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".nbt")).toList()) {
					ResourceLocation id = TwilightForestMod.prefix(this.basePath + "/" + this.inputRoot.relativize(path).toString().replace('\\', '/'));
					CompoundTag input = this.read(path);
					CompoundTag updated = updateNBT(input);
					if (!updated.equals(input)) {
						this.writeNBTTo(id, updated, cache);
					}
				}
			}
			return CompletableFuture.completedFuture(null);
		} catch (IOException e) {
			return CompletableFuture.failedFuture(e);
		}
	}

	@Override
	public String getName() {
		return "Update structure files in " + this.basePath;
	}

	private CompoundTag read(Path path) throws IOException {
		return NbtIo.readCompressed(new ByteArrayInputStream(Files.readAllBytes(path)), NbtAccounter.unlimitedHeap());
	}

	private void writeNBTTo(ResourceLocation id, CompoundTag data, CachedOutput cache) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		NbtIo.writeCompressed(data, stream);
		byte[] bytes = stream.toByteArray();
		Path outputPath = this.output.getOutputFolder().resolve("data/" + id.getNamespace() + "/" + id.getPath());
		cache.writeIfNeeded(outputPath, bytes, Hashing.sha1().hashBytes(bytes));
	}

	private static CompoundTag updateNBT(CompoundTag nbt) {
		CompoundTag updated = DataFixTypes.STRUCTURE.updateToCurrentVersion(
			DataFixers.getDataFixer(), nbt, nbt.getInt("DataVersion")
		);
		StructureTemplate template = new StructureTemplate();
		template.load(BuiltInRegistries.BLOCK.asLookup(), updated);
		return template.save(new CompoundTag());
	}

	private static String normalizeBasePath(String basePath) {
		return "structures".equals(basePath) ? "structure" : basePath;
	}
}
