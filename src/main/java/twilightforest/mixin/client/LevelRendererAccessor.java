package twilightforest.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
	@Accessor
	int getTicks();

	@Accessor
	int getRainSoundTime();

	@Accessor
	void setRainSoundTime(int time);
}
