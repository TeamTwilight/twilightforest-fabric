package twilightforest.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TwilightForestMod;
import twilightforest.extensions.IEntityEx;
import twilightforest.world.components.TFTeleporter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityEx {
    @Shadow public Level level;

    @Shadow public abstract boolean isRemoved();

    @Shadow @Nullable protected abstract PortalInfo findDimensionEntryPoint(ServerLevel serverLevel);

    @Shadow public abstract void unRide();

    @Shadow public float yRot;

    @Shadow public abstract EntityType<?> getType();

    @Shadow protected abstract void removeAfterChangingDimensions();

    @Shadow @Nullable public abstract Entity changeDimension(ServerLevel serverLevel);

    @Unique
    private CompoundTag persistentData;

    @Override
    public Entity changeDimension(ServerLevel pServer, TFTeleporter teleporter) {
        if (this.level instanceof ServerLevel && !this.isRemoved()) {
            this.level.getProfiler().push("changeDimension");
            this.unRide();
            this.level.getProfiler().push("reposition");
            PortalInfo portalinfo = teleporter.getPortalInfo((Entity) (Object) this, pServer, this::findDimensionEntryPoint);
            if (portalinfo == null) {
                return null;
            } else {
                Entity transportedEntity = teleporter.placeEntity((Entity) (Object) this, (ServerLevel) this.level, pServer, this.yRot, spawnPortal -> { //Forge: Start vanilla logic
                    this.level.getProfiler().popPush("reloading");
                    Entity entity = this.getType().create(pServer);
                    if (entity != null) {
                        entity.restoreFrom((Entity) (Object) this);
                        entity.moveTo(portalinfo.pos.x, portalinfo.pos.y, portalinfo.pos.z, portalinfo.yRot, entity.getXRot());
                        entity.setDeltaMovement(portalinfo.speed);
                        pServer.addDuringTeleport(entity);
                        if (spawnPortal && pServer.dimension() == Level.END) {
                            ServerLevel.makeObsidianPlatform(pServer);
                        }
                    }
                    return entity;
                }); //Forge: End vanilla logic

                this.removeAfterChangingDimensions();
                this.level.getProfiler().pop();
                ((ServerLevel)this.level).resetEmptyTime();
                pServer.resetEmptyTime();
                this.level.getProfiler().pop();
                return transportedEntity;
            }
        } else {
            return null;
        }
    }

    @Override
    public CompoundTag getPersistentData() {
        if (persistentData == null)
            persistentData = new CompoundTag();
        return persistentData;
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void addEntityData(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        //I don't like using Forge Here Grrr
        if (persistentData != null) compound.put("ForgeData", persistentData);
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getBoolean(Ljava/lang/String;)Z", ordinal = 6))
    public void loadEntityData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("ForgeData", 10)) persistentData = compound.getCompound("ForgeData");
    }
}
