package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import twilightforest.block.TFPortalBlock;

public class TFPortalAttachment {
	public static final int MAX_TICKS = 60;
	public static final Codec<TFPortalAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.BOOL.optionalFieldOf("inside_portal", false).forGetter(TFPortalAttachment::isInsidePortal),
		Codec.INT.optionalFieldOf("portal_timer", 0).forGetter(TFPortalAttachment::getPortalTimer)
	).apply(instance, TFPortalAttachment::new));

	protected boolean isInsidePortal;
	protected int portalTimer;

	public TFPortalAttachment() {
		this(false, 0);
	}

	public TFPortalAttachment(boolean isInsidePortal, int portalTimer) {
		this.isInsidePortal = isInsidePortal;
		this.portalTimer = Math.clamp(portalTimer, 0, MAX_TICKS);
	}

	public void setInPortal(boolean inPortal) {
		this.isInsidePortal = inPortal;
	}

	public boolean isInsidePortal() {
		return this.isInsidePortal;
	}

	public int getPortalTimer() {
		return this.portalTimer;
	}

	public void tick(Player player) {
		if (this.isInsidePortal()) {
			this.portalTimer = Math.min(this.portalTimer + 1, MAX_TICKS);
			if (!player.isInWall()) {
				BlockPos pos = player.blockPosition();
				if (!(player.level().getBlockState(pos).getBlock() instanceof TFPortalBlock) && !(player.level().getBlockState(pos.below()).getBlock() instanceof TFPortalBlock)) {
					this.isInsidePortal = false;
				}
			}
		} else if (this.portalTimer > 0) {
			this.portalTimer -= 2;
		}
	}
}
