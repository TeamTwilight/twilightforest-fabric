package twilightforest.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

// [VanillaCopy] of EntityDamageSource, but the entity can be null.
// the damage source that doesnt have a null entity now ends in .entity
public class NullableEntityDamageSource extends DamageSource {
	@Nullable
	protected final Entity entity;
	private boolean isThorns;

	public NullableEntityDamageSource(String source, @Nullable Entity entity) {
		super(source);
		this.entity = entity;
	}

	public NullableEntityDamageSource setThorns() {
		this.isThorns = true;
		return this;
	}

	public boolean isThorns() {
		return this.isThorns;
	}

	@Nullable
	public Entity getEntity() {
		return this.entity;
	}

	public Component getLocalizedDeathMessage(LivingEntity living) {
		String s = "death.attack." + this.msgId;
		if (this.entity != null) {
			ItemStack itemstack = this.entity instanceof LivingEntity attacker ? attacker.getMainHandItem() : ItemStack.EMPTY;
			return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? Component.translatable(s + ".item", living.getDisplayName(), this.entity.getDisplayName(), itemstack.getDisplayName()) : Component.translatable(s + ".entity", living.getDisplayName(), this.entity.getDisplayName());
		} else {
			return Component.translatable(s, living.getDisplayName());
		}
	}

	public boolean scalesWithDifficulty() {
		return this.entity instanceof LivingEntity && !(this.entity instanceof Player);
	}

	@Nullable
	public Vec3 getSourcePosition() {
		return this.entity.position();
	}

	public String toString() {
		return "NullableEntityDamageSource (" + this.entity + ")";
	}
}
