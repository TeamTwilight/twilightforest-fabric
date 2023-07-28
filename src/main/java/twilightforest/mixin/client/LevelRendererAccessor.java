package twilightforest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
	@Accessor
	int getTicks();

	@Accessor
	int getRainSoundTime();

	@Accessor
	void setRainSoundTime(int time);
}
