package twilightforest.entity;

import net.minecraft.network.FriendlyByteBuf;
import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import io.github.fabricators_of_create.porting_lib.entity.PartEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;

import java.util.Objects;

public abstract class TFPart<T extends Entity> extends PartEntity<T> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("noop");

	protected EntityDimensions realSize;

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

	public TFPart(T parent) {
		super(parent);
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Environment(EnvType.CLIENT)
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
		updateLastPos();
		super.tick();
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getX() + (this.interpTargetX - this.getX()) / (double) this.newPosRotationIncrements;
			double d2 = this.getY() + (this.interpTargetY - this.getY()) / (double) this.newPosRotationIncrements;
			double d4 = this.getZ() + (this.interpTargetZ - this.getZ()) / (double) this.newPosRotationIncrements;
			double d6 = Mth.wrapDegrees(this.interpTargetYaw - (double) this.getYRot());
			this.setYRot((float) ((double) this.getYRot() + d6 / (double) this.newPosRotationIncrements));
			this.setXRot((float) ((double) this.getXRot() + (this.interpTargetPitch - (double) this.getXRot()) / (double) this.newPosRotationIncrements));
			--this.newPosRotationIncrements;
			this.setPos(d0, d2, d4);
			this.setRot(this.getYRot(), this.getXRot());
		}

		while (getYRot() - this.yRotO < -180F) this.yRotO -= 360F;
		while (getYRot() - this.yRotO >= 180F) this.yRotO += 360F;

		while (this.renderYawOffset - this.prevRenderYawOffset < -180F) this.prevRenderYawOffset -= 360F;
		while (this.renderYawOffset - this.prevRenderYawOffset >= 180F) this.prevRenderYawOffset += 360F;

		while (getXRot() - this.xRotO < -180F) this.xRotO -= 360F;
		while (getXRot() - this.xRotO >= 180F) this.xRotO += 360F;
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
	public EntityDimensions getDimensions(Pose poseIn) {
		return realSize;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public void setId(int id) {
		super.setId(id + 1);
	}

	public void writeData(FriendlyByteBuf buffer) {
		buffer.writeDouble(this.getX());
		buffer.writeDouble(this.getY());
		buffer.writeDouble(this.getZ());
		buffer.writeFloat(this.getYRot());
		buffer.writeFloat(this.getXRot());
		buffer.writeFloat(this.dimensions.width);
		buffer.writeFloat(this.dimensions.height);
		buffer.writeBoolean(this.dimensions.fixed);

	}

	public void readData(FriendlyByteBuf buffer) {
		Vec3 vec = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		this.setPositionAndRotationDirect(vec.x(), vec.y(), vec.z(), buffer.readFloat(), buffer.readFloat(), 3);
		final float w = buffer.readFloat();
		final float h = buffer.readFloat();
		this.setSize(buffer.readBoolean() ? EntityDimensions.fixed(w, h) : EntityDimensions.scalable(w, h));
		this.refreshDimensions();
	}

	public static void assignPartIDs(Entity parent) {
		PartEntity<?>[] parts = ((MultiPartEntity)parent).getParts();
		for (int i = 0, partsLength = Objects.requireNonNull(parts).length; i < partsLength; i++) {
			PartEntity<?> part = parts[i];
			part.setId(parent.getId() + i);
		}
	}
}
