package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.model.entity.PartEntity;
import twilightforest.extensions.IEntityEx;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void clearMutiparts(Entity entity, CallbackInfo ci) {
        if(((IEntityEx)entity).isMultipartEntity()) {
            for(PartEntity<?> parts : ((IEntityEx) entity).getParts()) {
                parts.discard();
            }
        }
    }
}
