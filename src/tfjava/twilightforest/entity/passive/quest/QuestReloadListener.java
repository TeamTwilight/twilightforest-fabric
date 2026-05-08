package twilightforest.entity.passive.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import tamaized.beanification.Autowired;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

import java.util.Map;

/**
 * 1:1 port of upstream {@code twilightforest.entity.passive.quest.QuestReloadListener} —
 * scans {@code data/<ns>/twilight/quests/questing_ram.json} from every loaded datapack
 * and pushes the parsed {@link QuestingRamContext} into the autowired
 * {@link QuestingRamCurrentContext}. Falls back to {@link QuestingRamContext#FALLBACK}
 * if no datapack provides one.
 */
public class QuestReloadListener extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

	private static QuestingRamCurrentContext questingRamCurrentContext = new QuestingRamCurrentContext();

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public QuestReloadListener() {
		super(GSON, "twilight/quests");
	}

	public static QuestingRamCurrentContext currentContext() {
		return questingRamCurrentContext;
	}

	@Override
	public ResourceLocation getFabricId() {
		return TwilightForestMod.prefix("questing_ram");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
		boolean found = false;
		for (var entry : object.entrySet()) {
			if (entry.getKey().getPath().equals("questing_ram")) {
				questingRamCurrentContext.setContext(QuestingRamContext.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(JsonParseException::new));
				TwilightForestMod.LOGGER.debug("Questing Ram quest set by mod {}", entry.getKey().getNamespace());
				found = true;
			}
		}

		if (!found) {
			TwilightForestMod.LOGGER.error("Questing Ram quest file not found. Defaulting to fallback");
			questingRamCurrentContext.setContext(QuestingRamContext.FALLBACK);
		}
	}
}
