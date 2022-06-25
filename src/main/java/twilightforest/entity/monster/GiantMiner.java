package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageSources;
import twilightforest.init.TFItems;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class GiantMiner extends Monster {

	public GiantMiner(EntityType<? extends GiantMiner> type, Level world) {
		super(type, world);

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			this.setDropChance(slot, 0);
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity attackTarget) {
				return this.mob.getBbWidth() * this.mob.getBbHeight();
			}
		});
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 40.0D);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, spawnDataIn, dataTag);
		populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		populateDefaultEquipmentEnchantments(accessor.getRandom(), difficulty);

		return data;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.GIANT_PICKAXE.get()));
	}

	@Override
	protected void enchantSpawnedWeapon(RandomSource random, float chance) {

	}

	@Override
	protected void enchantSpawnedArmor(RandomSource random, float chance, EquipmentSlot slot) {

	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		entity.hurt(TFDamageSources.ant(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		return super.doHurtTarget(entity);
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 1;
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType reason) {
		List<GiantMiner> giantsNearby = accessor.getEntitiesOfClass(GiantMiner.class, this.getBoundingBox().inflate(100, 10, 100));
		return giantsNearby.size() < 10;
	}

	public static boolean canSpawn(EntityType<? extends GiantMiner> type, ServerLevelAccessor accessor, MobSpawnType reason, BlockPos pos, RandomSource rand) {
		return Monster.checkMonsterSpawnRules(type, accessor, reason, pos, rand) || accessor.getBlockState(pos).is(TFBlocks.WISPY_CLOUD.get()) || accessor.getBlockState(pos).is(TFBlocks.FLUFFY_CLOUD.get());
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}
}
