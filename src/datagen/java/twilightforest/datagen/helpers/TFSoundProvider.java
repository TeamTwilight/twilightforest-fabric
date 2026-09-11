package twilightforest.datagen.helpers;

import carminite.datagen.SoundDefinition;
import carminite.datagen.SoundDefinitionsProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.datagen.assets.LangGenerator;

public abstract class TFSoundProvider extends SoundDefinitionsProvider {

	protected TFSoundProvider(PackOutput output) {
		super(output, TFCommon.ID);
	}

	public void generateNewSoundWithSubtitle(Holder<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, String subtitle, float volume, float pitch) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, volume, pitch);
	}

	public void generateNewSoundWithSubtitle(Holder<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, String subtitle) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, 1.0F, 1.0F);
	}

	public void generateNewSound(Holder<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle, float volume, float pitch) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(TFCommon.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND).volume(volume).pitch(pitch));
		}
		this.add(event, definition);
	}

	public void generateNewSoundMC(Holder<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(Identifier.withDefaultNamespace(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition);
	}

	public void generateExistingSoundWithSubtitle(Holder<SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.generateExistingSoundWithSubtitle(event, referencedSound, subtitle, 1.0F, 1.0F);
	}

	public void generateExistingSoundWithSubtitle(Holder<SoundEvent> event, SoundEvent referencedSound, String subtitle, float volume, float pitch) {
		this.generateExistingSound(event, referencedSound, subtitle, volume, pitch);
	}

	public void generateSoundWithExistingSubtitle(Holder<SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.add(event, SoundDefinition.definition()
			.subtitle(subtitle)
			.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT)));
	}

	public void generateExistingSound(Holder<SoundEvent> event, SoundEvent referencedSound, @Nullable String subtitle, float volume, float pitch) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		this.add(event, definition
			.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT).volume(volume).pitch(pitch)));
	}

	public void makeStepSound(Holder<SoundEvent> event, SoundEvent referencedSound) {
		this.add(event, SoundDefinition.definition()
			.subtitle("subtitles.block.generic.footsteps")
			.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT)));
	}

	public void makeNewStepjSound(SoundEvent event, String baseSoundDirectory, int numberOfSounds) {
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(TFCommon.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition.subtitle("subtitles.block.generic.footsteps"));
	}

	public void makeNewGenericSound(Holder<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String type) {
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(TFCommon.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, type != null ? definition.subtitle("subtitles.block.generic." + type) : definition);
	}

	public void makeMusicDisc(Holder<SoundEvent> event, String discName) {
		this.add(event, SoundDefinition.definition()
			.with(SoundDefinition.Sound.sound(TFCommon.prefix("music/" + discName), SoundDefinition.SoundType.SOUND)
				.stream()));
	}

	public void generateParrotSound(Holder<SoundEvent> event, Holder<SoundEvent> referencedSound, String subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		this.createSubtitleAndLangEntry(event, definition, subtitle);

		this.add(event, definition
			.with(SoundDefinition.Sound.sound(referencedSound.value().location(), SoundDefinition.SoundType.EVENT).pitch(1.8F).volume(0.6F)));
	}

	private void createSubtitleAndLangEntry(Holder<SoundEvent> event, SoundDefinition definition, String subtitle) {
		String[] splitSoundName = event.value().location().getPath().split("\\.", 3);
		String subtitleKey = "subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2];
		definition.subtitle(subtitleKey);
		LangGenerator.SUBTITLE_GENERATOR.put(subtitleKey, subtitle);
	}
}