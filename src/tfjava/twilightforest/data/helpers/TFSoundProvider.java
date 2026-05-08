package twilightforest.data.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class TFSoundProvider implements DataProvider {
	private final PackOutput output;
	private final Map<String, JsonObject> sounds = new TreeMap<>();

	protected TFSoundProvider(PackOutput output, Object helper) {
		this.output = output;
	}

	public abstract void registerSounds();

	public void generateNewSoundWithSubtitle(SoundEvent event, String baseSoundDirectory, int numberOfSounds, String subtitle, float volume, float pitch) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, volume, pitch);
	}

	public void generateNewSoundWithSubtitle(SoundEvent event, String baseSoundDirectory, int numberOfSounds, String subtitle) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, 1.0F, 1.0F);
	}

	public void generateNewSound(SoundEvent event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle, float volume, float pitch) {
		JsonObject definition = definition(event, subtitle);
		JsonArray entries = sounds(definition);
		for (int i = 1; i <= numberOfSounds; i++) {
			entries.add(sound(TwilightForestMod.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), "sound", volume, pitch, false));
		}
		this.add(event, definition);
	}

	public void generateNewSoundMC(SoundEvent event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle) {
		JsonObject definition = definition(event, subtitle);
		JsonArray entries = sounds(definition);
		for (int i = 1; i <= numberOfSounds; i++) {
			entries.add(sound(ResourceLocation.withDefaultNamespace(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), "sound", 1.0F, 1.0F, false));
		}
		this.add(event, definition);
	}

	public void generateExistingSoundWithSubtitle(SoundEvent event, SoundEvent referencedSound, String subtitle) {
		this.generateExistingSoundWithSubtitle(event, referencedSound, subtitle, 1.0F, 1.0F);
	}

	public void generateExistingSoundWithSubtitle(SoundEvent event, SoundEvent referencedSound, String subtitle, float volume, float pitch) {
		this.generateExistingSound(event, referencedSound, subtitle, volume, pitch);
	}

	public void generateSoundWithExistingSubtitle(SoundEvent event, SoundEvent referencedSound, String subtitle) {
		JsonObject definition = new JsonObject();
		definition.addProperty("subtitle", subtitle);
		sounds(definition).add(sound(referencedSound.getLocation(), "event", 1.0F, 1.0F, false));
		this.add(event, definition);
	}

	public void generateExistingSound(SoundEvent event, SoundEvent referencedSound, @Nullable String subtitle, float volume, float pitch) {
		JsonObject definition = definition(event, subtitle);
		sounds(definition).add(sound(referencedSound.getLocation(), "event", volume, pitch, false));
		this.add(event, definition);
	}

	public void makeStepSound(SoundEvent event, SoundEvent referencedSound) {
		JsonObject definition = new JsonObject();
		definition.addProperty("subtitle", "subtitles.block.generic.footsteps");
		sounds(definition).add(sound(referencedSound.getLocation(), "event", 1.0F, 1.0F, false));
		this.add(event, definition);
	}

	public void makeNewStepjSound(SoundEvent event, String baseSoundDirectory, int numberOfSounds) {
		JsonObject definition = new JsonObject();
		definition.addProperty("subtitle", "subtitles.block.generic.footsteps");
		JsonArray entries = sounds(definition);
		for (int i = 1; i <= numberOfSounds; i++) {
			entries.add(sound(TwilightForestMod.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), "sound", 1.0F, 1.0F, false));
		}
		this.add(event, definition);
	}

	public void makeNewGenericSound(SoundEvent event, String baseSoundDirectory, int numberOfSounds, @Nullable String type) {
		JsonObject definition = new JsonObject();
		if (type != null) {
			definition.addProperty("subtitle", "subtitles.block.generic." + type);
		}
		JsonArray entries = sounds(definition);
		for (int i = 1; i <= numberOfSounds; i++) {
			entries.add(sound(TwilightForestMod.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), "sound", 1.0F, 1.0F, false));
		}
		this.add(event, definition);
	}

	public void makeMusicDisc(SoundEvent event, String discName) {
		JsonObject definition = new JsonObject();
		sounds(definition).add(sound(TwilightForestMod.prefix("music/" + discName), "sound", 1.0F, 1.0F, true));
		this.add(event, definition);
	}

	public void makeMusicPlaylist(SoundEvent event, float volume, String... tracks) {
		JsonObject definition = new JsonObject();
		JsonArray entries = sounds(definition);
		for (String track : tracks) {
			entries.add(sound(TwilightForestMod.prefix("music/" + track), "sound", volume, 1.0F, true));
		}
		this.add(event, definition);
	}

	public void generateParrotSound(SoundEvent event, SoundEvent referencedSound, String subtitle) {
		JsonObject definition = definition(event, subtitle);
		sounds(definition).add(sound(referencedSound.getLocation(), "event", 0.6F, 1.8F, false));
		this.add(event, definition);
	}

	protected void add(SoundEvent event, JsonObject definition) {
		this.sounds.put(event.getLocation().getPath(), definition);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		this.sounds.clear();
		this.registerSounds();
		JsonObject root = new JsonObject();
		this.sounds.forEach(root::add);
		return DataProvider.saveStable(output, root, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(TwilightForestMod.ID).resolve("sounds.json"));
	}

	@Override
	public String getName() {
		return "Twilight Forest Sounds";
	}

	private static JsonObject definition(SoundEvent event, @Nullable String subtitle) {
		JsonObject definition = new JsonObject();
		if (subtitle != null) {
			String subtitleKey = subtitleKey(event);
			definition.addProperty("subtitle", subtitleKey);
			addSubtitleLangEntry(subtitleKey, subtitle);
		}
		return definition;
	}

	private static JsonArray sounds(JsonObject definition) {
		if (!definition.has("sounds")) {
			definition.add("sounds", new JsonArray());
		}
		return definition.getAsJsonArray("sounds");
	}

	private static JsonObject sound(ResourceLocation location, String type, float volume, float pitch, boolean stream) {
		JsonObject sound = new JsonObject();
		sound.addProperty("name", location.toString());
		sound.addProperty("type", type);
		if (volume != 1.0F) {
			sound.addProperty("volume", volume);
		}
		if (pitch != 1.0F) {
			sound.addProperty("pitch", pitch);
		}
		if (stream) {
			sound.addProperty("stream", true);
		}
		return sound;
	}

	private static String subtitleKey(SoundEvent event) {
		String[] splitSoundName = event.getLocation().getPath().split("\\.", 3);
		if (splitSoundName.length >= 3) {
			return "subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2];
		}
		return "subtitles.twilightforest." + event.getLocation().getPath().replace('.', '_');
	}

	@SuppressWarnings("unchecked")
	private static void addSubtitleLangEntry(String key, String subtitle) {
		try {
			Class<?> langGenerator = Class.forName("twilightforest.data.LangGenerator");
			Field field = langGenerator.getField("SUBTITLE_GENERATOR");
			Object value = field.get(null);
			if (value instanceof Map<?, ?> map) {
				((Map<String, String>) map).put(key, subtitle);
			}
		} catch (ReflectiveOperationException ignored) {
			// LangGenerator is ported separately; sounds.json generation still remains complete without it.
		}
	}
}
