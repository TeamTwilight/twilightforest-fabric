package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.AbstractArrow;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;
import twilightforest.init.TFSounds;

public class SnowQueenIceShield extends TFPart<SnowQueen> {
    public static final ResourceLocation RENDERER = TwilightForestMod.prefix("snowqueen_iceshield");

    private boolean active = true;

    public SnowQueenIceShield(SnowQueen parent) {
        super(parent);
        this.setSize(EntityDimensions.scalable(0.75F, 0.75F));
    }

    @Override
    public ResourceLocation renderer() {
        return RENDERER;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
            return true;
        }
        this.playSound(TFSounds.SNOW_QUEEN_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        return false;
    }

    @Override
    public boolean isInvisible() {
        return !this.active || super.isInvisible();
    }

    public void activate() {
        this.active = true;
        this.setSize(EntityDimensions.scalable(0.75F, 0.75F));
    }

    public void deactivate() {
        this.active = false;
        this.setSize(EntityDimensions.scalable(0.0F, 0.0F));
    }
}
