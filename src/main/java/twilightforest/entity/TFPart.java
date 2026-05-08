package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Objects;

public abstract class TFPart<T extends Entity> extends Entity {
    public interface Owner {
        TFPart<?>[] getParts();
    }

    public static final ResourceLocation RENDERER = TwilightForestMod.prefix("noop");

    private final T parent;
    protected EntityDimensions realSize = EntityDimensions.fixed(1.0F, 1.0F);

    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    public float renderYawOffset;
    public float prevRenderYawOffset;

    public int deathTime;
    public int hurtTime;

    protected TFPart(T parent) {
        super(parent.getType(), parent.level());
        this.parent = parent;
        this.noPhysics = true;
        this.noCulling = true;
    }

    public T getParent() {
        return this.parent;
    }

    public ResourceLocation renderer() {
        return RENDERER;
    }

    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }

    @Override
    public void tick() {
        this.updateLastPos();
        super.tick();
        if (this.newPosRotationIncrements > 0) {
            double x = this.getX() + (this.interpTargetX - this.getX()) / (double) this.newPosRotationIncrements;
            double y = this.getY() + (this.interpTargetY - this.getY()) / (double) this.newPosRotationIncrements;
            double z = this.getZ() + (this.interpTargetZ - this.getZ()) / (double) this.newPosRotationIncrements;
            double yaw = Mth.wrapDegrees(this.interpTargetYaw - (double) this.getYRot());
            this.setYRot((float) ((double) this.getYRot() + yaw / (double) this.newPosRotationIncrements));
            this.setXRot((float) ((double) this.getXRot() + (this.interpTargetPitch - (double) this.getXRot()) / (double) this.newPosRotationIncrements));
            --this.newPosRotationIncrements;
            this.setPos(x, y, z);
            this.setRot(this.getYRot(), this.getXRot());
        }

        while (this.getYRot() - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while (this.getYRot() - this.yRotO >= 180.0F) this.yRotO += 360.0F;
        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) this.prevRenderYawOffset -= 360.0F;
        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) this.prevRenderYawOffset += 360.0F;
        while (this.getXRot() - this.xRotO < -180.0F) this.xRotO -= 360.0F;
        while (this.getXRot() - this.xRotO >= 180.0F) this.xRotO += 360.0F;
    }

    public final void updateLastPos() {
        this.moveTo(this.getX(), this.getY(), this.getZ());
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.tickCount++;
    }

    protected void setSize(EntityDimensions size) {
        this.realSize = size;
        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.realSize;
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return this.getParent().isCurrentlyGlowing();
    }

    @Override
    public boolean isInvisible() {
        return this.getParent().isInvisible();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.getParent().hurt(source, amount);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return this.getParent().interact(player, hand);
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent().is(entity);
    }

    @Override
    public void setId(int id) {
        super.setId(id + 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    public boolean shouldSave() {
        return false;
    }

    @Override
    public boolean saveAsPassenger(CompoundTag tag) {
        return false;
    }

    public UpdateTFMultipartPacket.PartDataHolder writeData() {
        return new UpdateTFMultipartPacket.PartDataHolder(
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYRot(),
                this.getXRot(),
                this.realSize.width(),
                this.realSize.height(),
                this.realSize.fixed(),
                this.getEntityData().packDirty());
    }

    public void readData(UpdateTFMultipartPacket.PartDataHolder data) {
        this.setPositionAndRotationDirect(data.x(), data.y(), data.z(), data.yRot(), data.xRot(), 3);
        this.setSize(data.fixed() ? EntityDimensions.fixed(data.width(), data.height()) : EntityDimensions.scalable(data.width(), data.height()));
        if (data.data() != null) {
            this.getEntityData().assignValues(data.data());
        }
        this.refreshDimensions();
    }

    public static void assignPartIDs(Entity parent) {
        if (parent instanceof Owner owner) {
            TFPart<?>[] parts = owner.getParts();
            for (int i = 0; i < Objects.requireNonNull(parts).length; i++) {
                parts[i].setId(parent.getId() + i);
            }
        }
    }
}
