package twilightforest.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

import javax.annotation.Nullable;

@Mixin(ServerLevel.class)
@Debug(export = true)
public abstract class ServerLevelMixinPatch {

    @Unique
    int cachedID;

    @Shadow public abstract LevelEntityGetter<Entity> getEntities();
    @Shadow public Int2ObjectMap<EnderDragonPart> dragonParts;

    @Inject(method = "getEntityOrPart", at=@At(value = "RETURN", ordinal = 1), cancellable = true)
    public void multipartFromIDTEST(int i, CallbackInfoReturnable<Entity> cir){
        Entity entity = (Entity)this.getEntities().get(i);
        if (cir.getReturnValue() == null) {
            cir.setReturnValue(ASMHooks.multipartFromID(entity, i));
        }
    }
}
