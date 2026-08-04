package twilightforest.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.util.TFGrassColorModifiers;

/**
 * Adds custom grass color modifier support for Twilight Forest biomes.
 * Replaces NeoForge's enum extension for GrassColorModifier.
 * In 1.21.1, Biome.getGrassColor() applies the vanilla GrassColorModifier internally.
 * We intercept at RETURN and apply our custom modifier if one is registered for this biome.
 * Uses the client level's registry access to look up the biome's ResourceKey.
 */
@Mixin(Biome.class)
public class BiomeMixin {

	@Inject(
		method = "getGrassColor",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$customGrassColor(
		double posX,
		double posZ,
		CallbackInfoReturnable<Integer> cir
	) {
		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.level == null) return;

		Biome self = (Biome) (Object) this;
		Registry<Biome> biomeRegistry = mc.level.registryAccess().registryOrThrow(Registries.BIOME);
		ResourceKey<Biome> key = biomeRegistry.getResourceKey(self).orElse(null);
		if (key != null) {
			TFGrassColorModifiers.GrassColorModifier modifier = TFGrassColorModifiers.getModifier(key);
			if (modifier != null) {
				cir.setReturnValue(modifier.modifyColor(posX, posZ, cir.getReturnValue()));
			}
		}
	}
}