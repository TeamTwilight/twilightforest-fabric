package twilightforest.extensions;

import twilightforest.client.renderer.TFWeatherRenderer;

public interface IEffectsEx {
    void setHandler(TFWeatherRenderer renderer);
    TFWeatherRenderer getHandler();
}
