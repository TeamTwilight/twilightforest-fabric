package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;

public class SnowGuardian extends BaseIceMob {

	public SnowGuardian(EntityType<? extends SnowGuardian> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.MAX_HEALTH, 10.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_GUARDIAN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_GUARDIAN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_GUARDIAN_DEATH;
	}

	@Override
	public float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F;
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		int type = random.nextInt(4);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(this.makeItemForSlot(EquipmentSlot.MAINHAND, type)));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(this.makeItemForSlot(EquipmentSlot.CHEST, type)));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.makeItemForSlot(EquipmentSlot.HEAD, type)));
	}

	private Item makeItemForSlot(EquipmentSlot slot, int type) {
		switch (slot) {
			case MAINHAND:
			default:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_sword;
					case 1:
						return TFItems.steeleaf_sword;
					case 2:
					case 3:
						return TFItems.knightmetal_sword;
				}
			case FEET:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_boots;
					case 1:
						return TFItems.steeleaf_boots;
					case 2:
						return TFItems.knightmetal_boots;
					case 3:
						return TFItems.arctic_boots;
				}
			case LEGS:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_leggings;
					case 1:
						return TFItems.steeleaf_leggings;
					case 2:
						return TFItems.knightmetal_leggings;
					case 3:
						return TFItems.arctic_leggings;
				}
			case CHEST:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_chestplate;
					case 1:
						return TFItems.steeleaf_chestplate;
					case 2:
						return TFItems.knightmetal_chestplate;
					case 3:
						return TFItems.arctic_chestplate;
				}
			case HEAD:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_helmet;
					case 1:
						return TFItems.steeleaf_helmet;
					case 2:
						return TFItems.knightmetal_helmet;
					case 3:
						return TFItems.arctic_helmet;
				}
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		SpawnGroupData data = super.finalizeSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
		this.populateDefaultEquipmentSlots(difficulty);
		this.populateDefaultEquipmentEnchantments(difficulty);
		return data;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.level.isClientSide) {
			for (int i = 0; i < 3; i++) {
				float px = (this.random.nextFloat() - this.random.nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F;
				float pz = (this.random.nextFloat() - this.random.nextFloat()) * 0.3F;

				level.addParticle(TFParticleType.SNOW_GUARDIAN, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
			}
		}
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 8;
	}
}
