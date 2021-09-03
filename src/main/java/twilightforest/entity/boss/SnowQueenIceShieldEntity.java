package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPartEntity;

public class SnowQueenIceShieldEntity extends TFPartEntity<SnowQueenEntity> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("snowqueen_iceshield");

    public SnowQueenIceShieldEntity(SnowQueenEntity parent) {
        super(parent);
        dimensions = EntityDimensions.scalable(0.75F, 0.75F);
    }

	@Environment(EnvType.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
    public boolean hurt(DamageSource source, float amount) {
        playSound(TFSounds.SNOW_QUEEN_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void defineSynchedData() {

    }
}
