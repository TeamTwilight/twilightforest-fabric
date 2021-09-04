package twilightforest.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

//Heavily inspired by Vanilla's AdvancementProvider
public record AdvancementProvider(DataGenerator generator) implements DataProvider {

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	@Override
	public void run(HashCache cache) {
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (adv) -> {
			if (!set.add(adv.getId()))
				throw new IllegalStateException("Duplicate advancement " + adv.getId());

			Path path1 = createPath(path, adv);

			try {
				DataProvider.save(GSON, cache, adv.deconstruct().serializeToJson(), path1);
			} catch (IOException ioexception) {
				TwilightForestMod.LOGGER.error("Couldn't save advancement {}", path1, ioexception);
			}
		};

		new AdvancementGenerator().accept(consumer);
		//new PatchouliAdvancementGenerator().accept(consumer);
	}

	private static Path createPath(Path path, Advancement advancement) {
		String pref = advancement.getId().getNamespace();
		return path.resolve("data/" + pref + "/advancements/" + advancement.getId().getPath() + ".json");
	}

	@Override
	public String getName() {
		return "Twilight Forest Advancements";
	}
}
