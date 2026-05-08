package twilightforest.mixin.client;

import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.resources.MapDecorationTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MapRenderer.class)
public interface MapRendererAccessor {
	@Accessor("decorationTextures")
	MapDecorationTextureManager twilightforest$getDecorationTextures();
}
