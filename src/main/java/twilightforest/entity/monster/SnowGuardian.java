package twilightforest.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItemVisuals;
import twilightforest.init.TFSounds;

import java.util.List;

public class SnowGuardian extends BaseIceMob {
    public SnowGuardian(EntityType<? extends SnowGuardian> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MAX_HEALTH, 10.0D);
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData);
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        this.populateDefaultEquipmentEnchantments(level, level.getRandom(), difficulty);
        return data;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        int type = random.nextInt(4);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(this.makeItemForSlot(EquipmentSlot.MAINHAND, type)));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(this.makeItemForSlot(EquipmentSlot.CHEST, type)));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.makeItemForSlot(EquipmentSlot.HEAD, type)));
    }

    private Item makeItemForSlot(EquipmentSlot slot, int type) {
        return switch (slot) {
            case MAINHAND -> switch (type) {
                case 1 -> Items.IRON_SWORD;
                case 2, 3 -> Items.DIAMOND_SWORD;
                default -> Items.STONE_SWORD;
            };
            case CHEST -> switch (type) {
                case 1 -> Items.CHAINMAIL_CHESTPLATE;
                case 2 -> Items.IRON_CHESTPLATE;
                case 3 -> Items.LEATHER_CHESTPLATE;
                default -> Items.LEATHER_CHESTPLATE;
            };
            case HEAD -> switch (type) {
                case 1 -> Items.CHAINMAIL_HELMET;
                case 2 -> Items.IRON_HELMET;
                case 3 -> Items.LEATHER_HELMET;
                default -> Items.LEATHER_HELMET;
            };
            default -> Items.AIR;
        };
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.SNOW_GUARDIAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.SNOW_GUARDIAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.SNOW_GUARDIAN_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.8F;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }
}