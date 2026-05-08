package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFStats;
import twilightforest.init.TFSounds;

public class FortificationShieldAttachment {
	public static final Codec<FortificationShieldAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("temporary_shields").forGetter(FortificationShieldAttachment::temporaryShieldsLeft),
		Codec.INT.fieldOf("permanent_shields").forGetter(FortificationShieldAttachment::permanentShieldsLeft)
	).apply(instance, FortificationShieldAttachment::new));

	public static final StreamCodec<FriendlyByteBuf, FortificationShieldAttachment> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, FortificationShieldAttachment::temporaryShieldsLeft,
		ByteBufCodecs.INT, FortificationShieldAttachment::permanentShieldsLeft,
		FortificationShieldAttachment::new
	);

	private int temporaryShields;
	private int permanentShields;
	private int timer;

	public FortificationShieldAttachment() {
		this(0, 0);
	}

	public FortificationShieldAttachment(int temporaryShields, int permanentShields) {
		this.temporaryShields = Math.max(temporaryShields, 0);
		this.permanentShields = Math.max(permanentShields, 0);
		this.resetTimer();
	}

	public void tick(LivingEntity entity) {
		if (this.temporaryShieldsLeft() > 0 && !(entity instanceof Player player && player.getAbilities().invulnerable)) {
			if (this.timer <= 0) {
				this.breakShield(entity, true);
			} else if (this.checkLichCrownBonus(entity)) {
				this.timer--;
			}
		}
	}

	private boolean checkLichCrownBonus(LivingEntity entity) {
		return !entity.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN) || (entity.tickCount % 3) != 0;
	}

	public int shieldsLeft() {
		return this.temporaryShields + this.permanentShields;
	}

	public int temporaryShieldsLeft() {
		return this.temporaryShields;
	}

	public int permanentShieldsLeft() {
		return this.permanentShields;
	}

	public void breakShield(LivingEntity entity, boolean expired) {
		if (this.temporaryShields > 0) {
			this.temporaryShields--;
			this.resetTimer();
		} else if (this.permanentShields > 0) {
			this.permanentShields--;
		}
		if (entity instanceof ServerPlayer player && !expired) {
			player.awardStat(Stats.CUSTOM.get(TFStats.TF_SHIELDS_BROKEN));
		}
		entity.level().playSound(null, entity.blockPosition(), expired ? TFSounds.FORTIFICATION_SHIELD_BLOCK : TFSounds.FORTIFICATION_SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.3F + 1.0F);
		TFDataAttachments.set(entity, TFDataAttachments.FORTIFICATION_SHIELDS, this);
	}

	public static void addShieldBreakParticles(DamageSource src, LivingEntity entity) {
		if (!(entity.level() instanceof ServerLevel serverLevel)) return;
		Vec3 pos = src.getSourcePosition();
		if (src.getDirectEntity() instanceof LivingEntity living) pos = living.getEyePosition();
		if (src.getEntity() instanceof TraceableEntity traceable && traceable.getOwner() instanceof LivingEntity living) pos = living.getEyePosition();
		if (pos != null) {
			Vec3 lichPos = entity.position().add(0.0D, entity.getBbHeight() * 0.65D, 0.0D);
			Vec3 offset = pos.subtract(lichPos).multiply(1.0D, 0.0D, 1.0D).normalize();
			pos = lichPos.add(offset.scale(0.55D));
			double sizeRange = 0.85D;
			for (int j = 0; j < 16; ++j) {
				double horizontal = entity.getRandom().nextDouble() - 0.5D;
				double x = sizeRange * offset.z * horizontal;
				double y = sizeRange * (entity.getRandom().nextDouble() - 0.5D);
				double z = sizeRange * offset.x * -horizontal;
				serverLevel.sendParticles(TFParticleType.SHIELD_BREAK, pos.x + x, pos.y + y, pos.z + z, 1, x * 0.5D, y * 0.5D, z * 0.5D, 0.0D);
			}
		} else {
			pos = entity.position().add(0.0D, entity.getBbHeight() * 0.65D, 0.0D);
			for (int j = 0; j < 16; ++j) {
				double x = entity.getRandom().nextDouble() - 0.5D;
				double y = (entity.getRandom().nextDouble() - 0.5D) * 0.25D;
				double z = entity.getRandom().nextDouble() - 0.5D;
				serverLevel.sendParticles(TFParticleType.SHIELD_BREAK, pos.x + x, pos.y + y, pos.z + z, 1, x * 0.33D, y * 0.33D, z * 0.33D, 0.0D);
			}
		}
	}

	public void setShields(LivingEntity entity, int amount, boolean temp) {
		if (temp) {
			this.temporaryShields = Math.clamp(amount, 0, 115);
			this.resetTimer();
		} else {
			this.permanentShields = Math.clamp(amount, 0, 115);
		}
		TFDataAttachments.set(entity, TFDataAttachments.FORTIFICATION_SHIELDS, this);
	}

	public void addShields(LivingEntity entity, int amount, boolean temp) {
		if (temp) {
			if (this.temporaryShields <= 0) this.resetTimer();
			this.temporaryShields = Math.clamp(this.temporaryShields + amount, 0, 115);
		} else {
			this.permanentShields = Math.clamp(this.permanentShields + amount, 0, 115);
		}
		TFDataAttachments.set(entity, TFDataAttachments.FORTIFICATION_SHIELDS, this);
	}

	private void resetTimer() {
		this.timer = 240;
	}
}
