package twilightforest.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.client.renderer.entity.NagaSegmentRenderer;
import twilightforest.client.renderer.entity.HydraHeadRenderer;
import twilightforest.client.renderer.entity.HydraNeckRenderer;
import twilightforest.client.renderer.entity.SnowQueenIceShieldRenderer;
import com.codex.twilight.client.render.CodexModelLayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BakedMultiPartRenderers {
    private static final Map<ResourceLocation, EntityRenderer<?>> RENDERERS = new HashMap<>();

    private BakedMultiPartRenderers() {
    }

    public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
        RENDERERS.clear();
        RENDERERS.put(TFPart.RENDERER, new NoopRenderer<>(context));
        RENDERERS.put(NagaSegment.RENDERER, new NagaSegmentRenderer<>(context));
        RENDERERS.put(HydraHead.RENDERER, new HydraHeadRenderer<>(context, new HydraHeadModel<>(context.bakeLayer(CodexModelLayers.HYDRA_HEAD))));
        RENDERERS.put(HydraNeck.RENDERER, new HydraNeckRenderer<>(context, new HydraNeckModel(context.bakeLayer(CodexModelLayers.HYDRA_NECK))));
        RENDERERS.put(SnowQueenIceShield.RENDERER, new SnowQueenIceShieldRenderer<>(context));
    }

    public static EntityRenderer<?> lookup(ResourceLocation location) {
        EntityRenderer<?> renderer = RENDERERS.get(location);
        return renderer != null ? renderer : RENDERERS.get(TFPart.RENDERER);
    }

    public static Iterable<Entity> injectTFPartEntities(Iterable<Entity> entities) {
        List<Entity> resolved = new ArrayList<>();
        for (Entity entity : entities) {
            resolved.add(entity);
            if (entity instanceof TFPart.Owner owner) {
                TFPart<?>[] parts = owner.getParts();
                if (parts != null) {
                    for (TFPart<?> part : parts) {
                        if (part != null) {
                            resolved.add(part);
                        }
                    }
                }
            }
        }
        return resolved;
    }
}
