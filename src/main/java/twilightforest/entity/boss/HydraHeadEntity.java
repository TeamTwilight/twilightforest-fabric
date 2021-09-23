package twilightforest.entity.boss;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFConstants;

public class HydraHeadEntity extends HydraPartEntity {

	public static final ResourceLocation RENDERER = TFConstants.prefix("hydra_head");

	private static final EntityDataAccessor<Float> DATA_MOUTH_POSITION = SynchedEntityData.defineId(HydraHeadEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_MOUTH_POSITION_LAST = SynchedEntityData.defineId(HydraHeadEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Byte> DATA_STATE = SynchedEntityData.defineId(HydraHeadEntity.class, EntityDataSerializers.BYTE);

	public HydraHeadEntity(HydraEntity hydra) {
		super(hydra, 4F, 4F);
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_MOUTH_POSITION, 0F);
		entityData.define(DATA_MOUTH_POSITION_LAST, 0F);
		entityData.define(DATA_STATE, (byte) 0);
	}

	public float getMouthOpen() {
		return entityData.get(DATA_MOUTH_POSITION);
	}

	public float getMouthOpenLast() {
		return entityData.get(DATA_MOUTH_POSITION_LAST);
	}

	public HydraHeadContainer.State getState() {
		return HydraHeadContainer.State.values()[entityData.get(DATA_STATE)];
	}

	public void setMouthOpen(float openness) {
		entityData.set(DATA_MOUTH_POSITION_LAST, getMouthOpen());
		entityData.set(DATA_MOUTH_POSITION, openness);
	}

	public void setState(HydraHeadContainer.State state) {
		entityData.set(DATA_STATE, (byte) state.ordinal());
	}
}
