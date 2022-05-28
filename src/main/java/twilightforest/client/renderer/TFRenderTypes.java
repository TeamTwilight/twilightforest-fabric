package twilightforest.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TFRenderTypes extends RenderType {
    public TFRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.Mode mode, int bufferSize, boolean crumbling, boolean sort, Runnable setup, Runnable clear) {
        super(name, vertexFormat, mode, bufferSize, crumbling, sort, setup, clear);
    }

    @Nonnull
    public static RenderType getProtectionBox(ResourceLocation resourceLocation, float t, float t1) {
        return create("protection_box", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                        .setTexturingState(new RenderStateShard.OffsetTexturingStateShard(t, t1))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY)
                        .createCompositeState(false));
    }
}
