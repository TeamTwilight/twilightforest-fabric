package twilightforest.asm;

import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BiomeSpecialEffects.GrassColorModifier.class)
abstract class GrassColorModifierMixin {

	@Shadow
	public abstract int modifyColor(double x, double z, int grassColor);
}