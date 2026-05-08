package twilightforest.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.codec.TFGrassColorModifierCodec;

/**
 * Keeps {@link BiomeSpecialEffects.GrassColorModifier#CODEC} routed through a tiny
 * Codex hook. The hook now delegates unchanged because the five Twilight Forest
 * values are real Manningham Mills enum subclasses, not vanilla fallback aliases.
 *
 * <p>This runs after the vanilla {@code <clinit>} so the wrap captures the original
 * codec — which has already enumerated the extended {@code values()} array, including
 * our 5 TF entries.</p>
 *
 * <p>Pair this with the {@code mutable field ... CODEC} entry in
 * {@code codex_twilight.accesswidener}.</p>
 */
@Mixin(BiomeSpecialEffects.GrassColorModifier.class)
public abstract class GrassColorModifierCodecMixin {

    @Mutable
    @Shadow
    @Final
    public static Codec<BiomeSpecialEffects.GrassColorModifier> CODEC;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void codex$wrapCodec(CallbackInfo ci) {
        CODEC = TFGrassColorModifierCodec.wrap(CODEC);
    }
}
