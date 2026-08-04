package twilightforest.mixin;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fixes ShaderInstance to support mod-namespaced shader paths.
 * In 1.21.1, the ShaderInstance constructor uses ResourceLocation.withDefaultNamespace()
 * which always forces the "minecraft" namespace. TFShaders registers shaders with
 * modid:path format (e.g. "twilightforest:aurora/aurora"), which the constructor
 * turns into "shaders/core/twilightforest:aurora/aurora.json".
 * This mixin extracts the mod namespace from after the "shaders/core/" prefix
 * and creates a proper ResourceLocation with the mod's namespace.
 */
@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {

	@Redirect(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
		)
	)
	private ResourceLocation twilightforest$fixShaderNamespace(String location) {
		// path = "shaders/core/twilightforest:aurora/aurora.json"
		// extract namespace between "shaders/core/" and the next ":"
		String prefix = "shaders/core/";
		if (location.startsWith(prefix)) {
			String afterPrefix = location.substring(prefix.length()); // "twilightforest:aurora/aurora.json"
			int colonIdx = afterPrefix.indexOf(':');
			if (colonIdx > 0) {
				String namespace = afterPrefix.substring(0, colonIdx); // "twilightforest"
				String shaderPath = afterPrefix.substring(colonIdx + 1); // "aurora/aurora.json"
				return ResourceLocation.fromNamespaceAndPath(namespace, prefix + shaderPath);
			}
		}

		return ResourceLocation.withDefaultNamespace(location);
	}
}