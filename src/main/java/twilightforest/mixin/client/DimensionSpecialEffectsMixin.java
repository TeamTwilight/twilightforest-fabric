package twilightforest.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.extensions.IEffectsEx;

import net.minecraft.client.renderer.DimensionSpecialEffects;

@Mixin(DimensionSpecialEffects.class)
public class DimensionSpecialEffectsMixin implements IEffectsEx {
    @Unique
    private TFWeatherRenderer tfWeatherRenderer;

    @Override
    public void setHandler(TFWeatherRenderer renderer) {
        this.tfWeatherRenderer = tfWeatherRenderer = renderer;
    }

    @Override
    public TFWeatherRenderer getHandler() {
        return this.tfWeatherRenderer;
    }
}
