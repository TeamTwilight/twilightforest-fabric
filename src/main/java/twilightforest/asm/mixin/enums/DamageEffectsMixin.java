package twilightforest.asm.mixin.enums;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.init.TFSounds;

@Mixin(DamageEffects.class)
public enum DamageEffectsMixin {
	TWILIGHTFOREST_PINCH("pinch", TFSounds.PINCH_BEETLE_ATTACK.value())
	;

	@Shadow
	DamageEffectsMixin(String id, SoundEvent sound) {
	}
}