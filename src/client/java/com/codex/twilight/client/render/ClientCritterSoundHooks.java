package com.codex.twilight.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import twilightforest.init.TFSounds;

public final class ClientCritterSoundHooks {
	private ClientCritterSoundHooks() {
	}

	public static void stopCicada() {
		Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.getLocation(), SoundSource.NEUTRAL);
	}
}
