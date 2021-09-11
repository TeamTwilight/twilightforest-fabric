package twilightforest.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.TwilightForestMod;
import twilightforest.extensions.IEntityEx;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.TFTeleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements IEntityEx {
    @Shadow private boolean isChangingDimension;

    @Shadow public abstract ServerLevel getLevel();

    @Shadow public boolean wonGame;

    @Shadow public ServerGamePacketListenerImpl connection;

    @Shadow private boolean seenCredits;

    @Shadow @Final public MinecraftServer server;

    @Shadow @Nullable private Vec3 enteredNetherPosition;

    @Shadow private int lastSentFood;

    @Shadow private float lastSentHealth;

    @Shadow private int lastSentExp;

    @Shadow protected abstract void triggerDimensionChangeTriggers(ServerLevel serverLevel);

    @Shadow public abstract void moveTo(double d, double e, double f);

    @Shadow public abstract void setLevel(ServerLevel serverLevel);

    @Shadow protected abstract void createEndPlatform(ServerLevel serverLevel, BlockPos blockPos);

    @Shadow @Nullable protected abstract PortalInfo findDimensionEntryPoint(ServerLevel serverLevel);

    @Shadow @Final public ServerPlayerGameMode gameMode;

    @Override
    public Entity changeDimension(ServerLevel pServer, TFTeleporter teleporter) {
        this.isChangingDimension = true;
        ServerLevel serverlevel = this.getLevel();
        ResourceKey<Level> resourcekey = serverlevel.dimension();

        LevelData leveldata = pServer.getLevelData();
        this.connection.send(new ClientboundRespawnPacket(pServer.dimensionType(), pServer.dimension(), BiomeManager.obfuscateSeed(pServer.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), pServer.isDebug(), pServer.isFlat(), true));
        this.connection.send(new ClientboundChangeDifficultyPacket(leveldata.getDifficulty(), leveldata.isDifficultyLocked()));
        PlayerList playerlist = this.server.getPlayerList();
        playerlist.sendPlayerPermissionLevel((ServerPlayer) (Object) this);
        WorldUtil.removeEntityComplete(serverlevel, (Entity) (Object) this, true); //Forge: the player entity is moved to the new world, NOT cloned. So keep the data alive with no matching invalidate call.
        ((Entity)(Object)this).unsetRemoved();
        PortalInfo portalinfo = teleporter.getPortalInfo((Entity) (Object) this, pServer, this::findDimensionEntryPoint);
        if (portalinfo != null) {
            Entity e = teleporter.placeEntity((Entity) (Object) this, serverlevel, pServer, ((Entity)(Object)this).getYRot(), spawnPortal -> {//Forge: Start vanilla logic
                serverlevel.getProfiler().push("moving");
                if (resourcekey == Level.OVERWORLD && pServer.dimension() == Level.NETHER) {
                    this.enteredNetherPosition = ((Entity)(Object)this).position();
                } else if (spawnPortal && pServer.dimension() == Level.END) {
                    this.createEndPlatform(pServer, new BlockPos(portalinfo.pos));
                }

                serverlevel.getProfiler().pop();
                serverlevel.getProfiler().push("placing");
                this.setLevel(pServer);
                pServer.addDuringPortalTeleport((ServerPlayer) (Object) this);
                ((Entity)(Object)this).setYRot(portalinfo.yRot);
                ((Entity)(Object)this).setXRot(portalinfo.xRot);

                this.moveTo(portalinfo.pos.x, portalinfo.pos.y, portalinfo.pos.z);
                serverlevel.getProfiler().pop();
                this.triggerDimensionChangeTriggers(serverlevel);
                return (Entity) (Object) this;//forge: this is part of the ITeleporter patch
            });//Forge: End vanilla logic
            if (e != (Object) this) throw new java.lang.IllegalArgumentException(String.format("Teleporter %s returned not the player entity but instead %s, expected PlayerEntity %s", teleporter, e, this));
            this.connection.send(new ClientboundPlayerAbilitiesPacket(((Player)(Object)this).getAbilities()));
            playerlist.sendLevelInfo((ServerPlayer) (Object) this, pServer);
            playerlist.sendAllPlayerInfo((ServerPlayer) (Object) this);

            for(MobEffectInstance mobeffectinstance : ((LivingEntity)(Object)this).getActiveEffects()) {
                this.connection.send(new ClientboundUpdateMobEffectPacket(((Entity)(Object)this).getId(), mobeffectinstance));
            }

            this.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
            this.lastSentExp = -1;
            this.lastSentHealth = -1.0F;
            this.lastSentFood = -1;
            //net.minecraftforge.fmllegacy.hooks.BasicEventHooks.firePlayerChangedDimensionEvent(this, resourcekey, pServer.dimension());
            }

            return (Entity) (Object) this;
    }
}
