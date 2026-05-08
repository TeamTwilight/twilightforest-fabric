package twilightforest.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 1:1 port of upstream {@code twilightforest.entity.projectile.TFArrow}. Common
 * {@link AbstractArrow} parent for TF-specific arrows that wraps an optional
 * "parent arrow" so chain enchant effects (multishot+infinity etc.) carry on.
 * Pickup item defaults to vanilla arrow; on-hit forwards to the parent arrow.
 */
public abstract class TFArrow extends AbstractArrow implements ITFProjectile {

	@Nullable
	protected final AbstractArrow parentArrow;

	public TFArrow(EntityType<? extends TFArrow> type, Level level) {
		super(type, level);
		this.parentArrow = null;
	}

	public TFArrow(EntityType<? extends TFArrow> type, Level level, @Nullable LivingEntity shooter, ItemStack stack, ItemStack weapon) {
		super(type, shooter, level, stack, weapon);
		this.setOwner(shooter);
		if (shooter != null) {
			this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
		}
		this.parentArrow = null;
	}

	public TFArrow(EntityType<? extends TFArrow> type, AbstractArrow parentArrow, ItemStack stack, ItemStack weapon) {
		super(type, (LivingEntity) parentArrow.getOwner(), parentArrow.level(), stack, weapon);
		var shooter = (LivingEntity) parentArrow.getOwner();
		this.setOwner(shooter);
		if (shooter != null) {
			this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
		}
		this.parentArrow = parentArrow;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(Items.ARROW);
	}

	@Override
	public void doPostHurtEffects(LivingEntity target) {
		if (this.parentArrow != null) {
			this.parentArrow.doPostHurtEffects(target);
		}
		super.doPostHurtEffects(target);
	}
}
