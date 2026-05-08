package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class Redcap extends Monster {
    public ItemStack heldPick = TFItemVisuals.withModel(new ItemStack(Items.IRON_PICKAXE), TFItemVisuals.STEELEAF_PICKAXE);
    /** Stack of TNT the redcap can plant near hostile targets. Initially holds 1 TNT;
     * decremented to empty when consumed by {@link twilightforest.entity.ai.goal.RedcapPlantTNTGoal}. */
    public ItemStack heldTNT = new ItemStack(Items.TNT);
    /** Flint & steel the redcap swaps in when it lights pre-existing TNT (used by
     * {@link twilightforest.entity.ai.goal.RedcapLightTNTGoal}). */
    public ItemStack heldFlint = new ItemStack(Items.FLINT_AND_STEEL);
    protected int tntLeft = 1;

    public Redcap(EntityType<? extends Redcap> type, Level level) {
        super(type, level);
    }

    protected int getDisplayModel() {
        return TFItemVisuals.REDCAP_DISPLAY;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isShy() {
        return this.lastHurtByPlayerTime <= 0;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, spawnData);
        this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
        this.populateDefaultEquipmentEnchantments(accessor, accessor.getRandom(), difficulty);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.2F);
        this.setDropChance(EquipmentSlot.FEET, 0.2F);
        return data;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, this.heldPick.copy());
        this.setItemSlot(EquipmentSlot.FEET, TFItemVisuals.withModel(new ItemStack(Items.IRON_BOOTS), TFItemVisuals.IRONWOOD_BOOTS));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.REDCAP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source) {
        return TFSounds.REDCAP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.REDCAP_DEATH;
    }

    public double getMyRidingOffset() {
        return -0.25D;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TNTLeft", this.tntLeft);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.tntLeft = tag.getInt("TNTLeft");
    }
}